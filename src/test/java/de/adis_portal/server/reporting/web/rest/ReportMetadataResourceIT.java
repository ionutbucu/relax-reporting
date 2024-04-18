package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportMetadataAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportMetadata;
import de.adis_portal.server.reporting.repository.ReportMetadataRepository;
import de.adis_portal.server.reporting.service.dto.ReportMetadataDTO;
import de.adis_portal.server.reporting.service.mapper.ReportMetadataMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReportMetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportMetadataResourceIT {

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportMetadataRepository reportMetadataRepository;

    @Autowired
    private ReportMetadataMapper reportMetadataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMetadataMockMvc;

    private ReportMetadata reportMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportMetadata createEntity(EntityManager em) {
        ReportMetadata reportMetadata = new ReportMetadata().metadata(DEFAULT_METADATA);
        return reportMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportMetadata createUpdatedEntity(EntityManager em) {
        ReportMetadata reportMetadata = new ReportMetadata().metadata(UPDATED_METADATA);
        return reportMetadata;
    }

    @BeforeEach
    public void initTest() {
        reportMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createReportMetadata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);
        var returnedReportMetadataDTO = om.readValue(
            restReportMetadataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportMetadataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportMetadataDTO.class
        );

        // Validate the ReportMetadata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportMetadata = reportMetadataMapper.toEntity(returnedReportMetadataDTO);
        assertReportMetadataUpdatableFieldsEquals(returnedReportMetadata, getPersistedReportMetadata(returnedReportMetadata));
    }

    @Test
    @Transactional
    void createReportMetadataWithExistingId() throws Exception {
        // Create the ReportMetadata with an existing ID
        reportMetadata.setId(1L);
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportMetadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        // Get all the reportMetadataList
        restReportMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())));
    }

    @Test
    @Transactional
    void getReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        // Get the reportMetadata
        restReportMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, reportMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportMetadata.getId().intValue()))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReportMetadata() throws Exception {
        // Get the reportMetadata
        restReportMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportMetadata
        ReportMetadata updatedReportMetadata = reportMetadataRepository.findById(reportMetadata.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportMetadata are not directly saved in db
        em.detach(updatedReportMetadata);
        updatedReportMetadata.metadata(UPDATED_METADATA);
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(updatedReportMetadata);

        restReportMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportMetadataToMatchAllProperties(updatedReportMetadata);
    }

    @Test
    @Transactional
    void putNonExistingReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setId(longCount.incrementAndGet());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setId(longCount.incrementAndGet());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setId(longCount.incrementAndGet());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMetadataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportMetadataWithPatch() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportMetadata using partial update
        ReportMetadata partialUpdatedReportMetadata = new ReportMetadata();
        partialUpdatedReportMetadata.setId(reportMetadata.getId());

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ReportMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportMetadataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportMetadata, reportMetadata),
            getPersistedReportMetadata(reportMetadata)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportMetadataWithPatch() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportMetadata using partial update
        ReportMetadata partialUpdatedReportMetadata = new ReportMetadata();
        partialUpdatedReportMetadata.setId(reportMetadata.getId());

        partialUpdatedReportMetadata.metadata(UPDATED_METADATA);

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ReportMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportMetadataUpdatableFieldsEquals(partialUpdatedReportMetadata, getPersistedReportMetadata(partialUpdatedReportMetadata));
    }

    @Test
    @Transactional
    void patchNonExistingReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setId(longCount.incrementAndGet());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setId(longCount.incrementAndGet());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setId(longCount.incrementAndGet());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMetadataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportMetadata
        restReportMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportMetadataRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ReportMetadata getPersistedReportMetadata(ReportMetadata reportMetadata) {
        return reportMetadataRepository.findById(reportMetadata.getId()).orElseThrow();
    }

    protected void assertPersistedReportMetadataToMatchAllProperties(ReportMetadata expectedReportMetadata) {
        assertReportMetadataAllPropertiesEquals(expectedReportMetadata, getPersistedReportMetadata(expectedReportMetadata));
    }

    protected void assertPersistedReportMetadataToMatchUpdatableProperties(ReportMetadata expectedReportMetadata) {
        assertReportMetadataAllUpdatablePropertiesEquals(expectedReportMetadata, getPersistedReportMetadata(expectedReportMetadata));
    }
}
