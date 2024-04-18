package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportDataSourceAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportDataSource;
import de.adis_portal.server.reporting.repository.ReportDataSourceRepository;
import de.adis_portal.server.reporting.service.dto.ReportDataSourceDTO;
import de.adis_portal.server.reporting.service.mapper.ReportDataSourceMapper;
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
 * Integration tests for the {@link ReportDataSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportDataSourceResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-data-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportDataSourceRepository reportDataSourceRepository;

    @Autowired
    private ReportDataSourceMapper reportDataSourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportDataSourceMockMvc;

    private ReportDataSource reportDataSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDataSource createEntity(EntityManager em) {
        ReportDataSource reportDataSource = new ReportDataSource()
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .user(DEFAULT_USER)
            .password(DEFAULT_PASSWORD);
        return reportDataSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDataSource createUpdatedEntity(EntityManager em) {
        ReportDataSource reportDataSource = new ReportDataSource()
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .user(UPDATED_USER)
            .password(UPDATED_PASSWORD);
        return reportDataSource;
    }

    @BeforeEach
    public void initTest() {
        reportDataSource = createEntity(em);
    }

    @Test
    @Transactional
    void createReportDataSource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);
        var returnedReportDataSourceDTO = om.readValue(
            restReportDataSourceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDataSourceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportDataSourceDTO.class
        );

        // Validate the ReportDataSource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportDataSource = reportDataSourceMapper.toEntity(returnedReportDataSourceDTO);
        assertReportDataSourceUpdatableFieldsEquals(returnedReportDataSource, getPersistedReportDataSource(returnedReportDataSource));
    }

    @Test
    @Transactional
    void createReportDataSourceWithExistingId() throws Exception {
        // Create the ReportDataSource with an existing ID
        reportDataSource.setId(1L);
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportDataSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDataSourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportDataSources() throws Exception {
        // Initialize the database
        reportDataSourceRepository.saveAndFlush(reportDataSource);

        // Get all the reportDataSourceList
        restReportDataSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDataSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getReportDataSource() throws Exception {
        // Initialize the database
        reportDataSourceRepository.saveAndFlush(reportDataSource);

        // Get the reportDataSource
        restReportDataSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, reportDataSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportDataSource.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingReportDataSource() throws Exception {
        // Get the reportDataSource
        restReportDataSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportDataSource() throws Exception {
        // Initialize the database
        reportDataSourceRepository.saveAndFlush(reportDataSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDataSource
        ReportDataSource updatedReportDataSource = reportDataSourceRepository.findById(reportDataSource.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportDataSource are not directly saved in db
        em.detach(updatedReportDataSource);
        updatedReportDataSource.type(UPDATED_TYPE).url(UPDATED_URL).user(UPDATED_USER).password(UPDATED_PASSWORD);
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(updatedReportDataSource);

        restReportDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDataSourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDataSourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportDataSourceToMatchAllProperties(updatedReportDataSource);
    }

    @Test
    @Transactional
    void putNonExistingReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setId(longCount.incrementAndGet());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDataSourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDataSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setId(longCount.incrementAndGet());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDataSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDataSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setId(longCount.incrementAndGet());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDataSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDataSourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportDataSourceWithPatch() throws Exception {
        // Initialize the database
        reportDataSourceRepository.saveAndFlush(reportDataSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDataSource using partial update
        ReportDataSource partialUpdatedReportDataSource = new ReportDataSource();
        partialUpdatedReportDataSource.setId(reportDataSource.getId());

        partialUpdatedReportDataSource.type(UPDATED_TYPE).url(UPDATED_URL);

        restReportDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportDataSource))
            )
            .andExpect(status().isOk());

        // Validate the ReportDataSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDataSourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportDataSource, reportDataSource),
            getPersistedReportDataSource(reportDataSource)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportDataSourceWithPatch() throws Exception {
        // Initialize the database
        reportDataSourceRepository.saveAndFlush(reportDataSource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDataSource using partial update
        ReportDataSource partialUpdatedReportDataSource = new ReportDataSource();
        partialUpdatedReportDataSource.setId(reportDataSource.getId());

        partialUpdatedReportDataSource.type(UPDATED_TYPE).url(UPDATED_URL).user(UPDATED_USER).password(UPDATED_PASSWORD);

        restReportDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDataSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportDataSource))
            )
            .andExpect(status().isOk());

        // Validate the ReportDataSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDataSourceUpdatableFieldsEquals(
            partialUpdatedReportDataSource,
            getPersistedReportDataSource(partialUpdatedReportDataSource)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setId(longCount.incrementAndGet());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDataSourceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDataSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setId(longCount.incrementAndGet());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDataSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDataSourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setId(longCount.incrementAndGet());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDataSourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportDataSourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportDataSource() throws Exception {
        // Initialize the database
        reportDataSourceRepository.saveAndFlush(reportDataSource);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportDataSource
        restReportDataSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportDataSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportDataSourceRepository.count();
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

    protected ReportDataSource getPersistedReportDataSource(ReportDataSource reportDataSource) {
        return reportDataSourceRepository.findById(reportDataSource.getId()).orElseThrow();
    }

    protected void assertPersistedReportDataSourceToMatchAllProperties(ReportDataSource expectedReportDataSource) {
        assertReportDataSourceAllPropertiesEquals(expectedReportDataSource, getPersistedReportDataSource(expectedReportDataSource));
    }

    protected void assertPersistedReportDataSourceToMatchUpdatableProperties(ReportDataSource expectedReportDataSource) {
        assertReportDataSourceAllUpdatablePropertiesEquals(
            expectedReportDataSource,
            getPersistedReportDataSource(expectedReportDataSource)
        );
    }
}
