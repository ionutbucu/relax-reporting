package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportColumnMappingAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportColumnMapping;
import de.adis_portal.server.reporting.repository.ReportColumnMappingRepository;
import de.adis_portal.server.reporting.service.dto.ReportColumnMappingDTO;
import de.adis_portal.server.reporting.service.mapper.ReportColumnMappingMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReportColumnMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportColumnMappingResourceIT {

    private static final String DEFAULT_SOURCE_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_COLUMN_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SOURCE_COLUMN_INDEX = 1;
    private static final Integer UPDATED_SOURCE_COLUMN_INDEX = 2;

    private static final String DEFAULT_COLUMN_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LANG = "AAAAAAAAAA";
    private static final String UPDATED_LANG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-column-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportColumnMappingRepository reportColumnMappingRepository;

    @Autowired
    private ReportColumnMappingMapper reportColumnMappingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportColumnMappingMockMvc;

    private ReportColumnMapping reportColumnMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportColumnMapping createEntity(EntityManager em) {
        ReportColumnMapping reportColumnMapping = new ReportColumnMapping()
            .rid(UUID.randomUUID().toString())
            .sourceColumnName(DEFAULT_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(DEFAULT_SOURCE_COLUMN_INDEX)
            .columnTitle(DEFAULT_COLUMN_TITLE)
            .lang(DEFAULT_LANG);
        return reportColumnMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportColumnMapping createUpdatedEntity(EntityManager em) {
        ReportColumnMapping reportColumnMapping = new ReportColumnMapping()
            .rid(UUID.randomUUID().toString())
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);
        return reportColumnMapping;
    }

    @BeforeEach
    public void initTest() {
        reportColumnMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createReportColumnMapping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);
        var returnedReportColumnMappingDTO = om.readValue(
            restReportColumnMappingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportColumnMappingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportColumnMappingDTO.class
        );

        // Validate the ReportColumnMapping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportColumnMapping = reportColumnMappingMapper.toEntity(returnedReportColumnMappingDTO);
        assertReportColumnMappingUpdatableFieldsEquals(
            returnedReportColumnMapping,
            getPersistedReportColumnMapping(returnedReportColumnMapping)
        );
    }

    @Test
    @Transactional
    void createReportColumnMappingWithExistingId() throws Exception {
        // Create the ReportColumnMapping with an existing ID
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportColumnMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportColumnMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportColumnMappings() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);

        // Get all the reportColumnMappingList
        restReportColumnMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=rid,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].rid").value(hasItem(reportColumnMapping.getRid())))
            .andExpect(jsonPath("$.[*].sourceColumnName").value(hasItem(DEFAULT_SOURCE_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].sourceColumnIndex").value(hasItem(DEFAULT_SOURCE_COLUMN_INDEX)))
            .andExpect(jsonPath("$.[*].columnTitle").value(hasItem(DEFAULT_COLUMN_TITLE)))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG)));
    }

    @Test
    @Transactional
    void getReportColumnMapping() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);

        // Get the reportColumnMapping
        restReportColumnMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, reportColumnMapping.getRid()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.rid").value(reportColumnMapping.getRid()))
            .andExpect(jsonPath("$.sourceColumnName").value(DEFAULT_SOURCE_COLUMN_NAME))
            .andExpect(jsonPath("$.sourceColumnIndex").value(DEFAULT_SOURCE_COLUMN_INDEX))
            .andExpect(jsonPath("$.columnTitle").value(DEFAULT_COLUMN_TITLE))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG));
    }

    @Test
    @Transactional
    void getNonExistingReportColumnMapping() throws Exception {
        // Get the reportColumnMapping
        restReportColumnMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportColumnMapping() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportColumnMapping
        ReportColumnMapping updatedReportColumnMapping = reportColumnMappingRepository.findById(reportColumnMapping.getRid()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportColumnMapping are not directly saved in db
        em.detach(updatedReportColumnMapping);
        updatedReportColumnMapping
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(updatedReportColumnMapping);

        restReportColumnMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportColumnMappingDTO.getRid())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportColumnMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportColumnMappingToMatchAllProperties(updatedReportColumnMapping);
    }

    @Test
    @Transactional
    void putNonExistingReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportColumnMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportColumnMappingDTO.getRid())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportColumnMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportColumnMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportColumnMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportColumnMappingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportColumnMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportColumnMappingWithPatch() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportColumnMapping using partial update
        ReportColumnMapping partialUpdatedReportColumnMapping = new ReportColumnMapping();
        partialUpdatedReportColumnMapping.setRid(reportColumnMapping.getRid());

        partialUpdatedReportColumnMapping
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);

        restReportColumnMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportColumnMapping.getRid())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportColumnMapping))
            )
            .andExpect(status().isOk());

        // Validate the ReportColumnMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportColumnMappingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportColumnMapping, reportColumnMapping),
            getPersistedReportColumnMapping(reportColumnMapping)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportColumnMappingWithPatch() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportColumnMapping using partial update
        ReportColumnMapping partialUpdatedReportColumnMapping = new ReportColumnMapping();
        partialUpdatedReportColumnMapping.setRid(reportColumnMapping.getRid());

        partialUpdatedReportColumnMapping
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);

        restReportColumnMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportColumnMapping.getRid())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportColumnMapping))
            )
            .andExpect(status().isOk());

        // Validate the ReportColumnMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportColumnMappingUpdatableFieldsEquals(
            partialUpdatedReportColumnMapping,
            getPersistedReportColumnMapping(partialUpdatedReportColumnMapping)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportColumnMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportColumnMappingDTO.getRid())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportColumnMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportColumnMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportColumnMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportColumnMappingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportColumnMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportColumnMapping() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.saveAndFlush(reportColumnMapping);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportColumnMapping
        restReportColumnMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportColumnMapping.getRid()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportColumnMappingRepository.count();
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

    protected ReportColumnMapping getPersistedReportColumnMapping(ReportColumnMapping reportColumnMapping) {
        return reportColumnMappingRepository.findById(reportColumnMapping.getRid()).orElseThrow();
    }

    protected void assertPersistedReportColumnMappingToMatchAllProperties(ReportColumnMapping expectedReportColumnMapping) {
        assertReportColumnMappingAllPropertiesEquals(
            expectedReportColumnMapping,
            getPersistedReportColumnMapping(expectedReportColumnMapping)
        );
    }

    protected void assertPersistedReportColumnMappingToMatchUpdatableProperties(ReportColumnMapping expectedReportColumnMapping) {
        assertReportColumnMappingAllUpdatablePropertiesEquals(
            expectedReportColumnMapping,
            getPersistedReportColumnMapping(expectedReportColumnMapping)
        );
    }
}
