package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportExecutionAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportExecution;
import de.adis_portal.server.reporting.repository.ReportExecutionRepository;
import de.adis_portal.server.reporting.service.dto.ReportExecutionDTO;
import de.adis_portal.server.reporting.service.mapper.ReportExecutionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ReportExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportExecutionResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ERROR = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportExecutionRepository reportExecutionRepository;

    @Autowired
    private ReportExecutionMapper reportExecutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportExecutionMockMvc;

    private ReportExecution reportExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportExecution createEntity(EntityManager em) {
        ReportExecution reportExecution = new ReportExecution().date(DEFAULT_DATE).error(DEFAULT_ERROR).url(DEFAULT_URL).user(DEFAULT_USER);
        return reportExecution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportExecution createUpdatedEntity(EntityManager em) {
        ReportExecution reportExecution = new ReportExecution().date(UPDATED_DATE).error(UPDATED_ERROR).url(UPDATED_URL).user(UPDATED_USER);
        return reportExecution;
    }

    @BeforeEach
    public void initTest() {
        reportExecution = createEntity(em);
    }

    @Test
    @Transactional
    void createReportExecution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);
        var returnedReportExecutionDTO = om.readValue(
            restReportExecutionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportExecutionDTO.class
        );

        // Validate the ReportExecution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportExecution = reportExecutionMapper.toEntity(returnedReportExecutionDTO);
        assertReportExecutionUpdatableFieldsEquals(returnedReportExecution, getPersistedReportExecution(returnedReportExecution));
    }

    @Test
    @Transactional
    void createReportExecutionWithExistingId() throws Exception {
        // Create the ReportExecution with an existing ID
        reportExecution.setId(1L);
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportExecution.setDate(null);

        // Create the ReportExecution, which fails.
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        restReportExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportExecutions() throws Exception {
        // Initialize the database
        reportExecutionRepository.saveAndFlush(reportExecution);

        // Get all the reportExecutionList
        restReportExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)));
    }

    @Test
    @Transactional
    void getReportExecution() throws Exception {
        // Initialize the database
        reportExecutionRepository.saveAndFlush(reportExecution);

        // Get the reportExecution
        restReportExecutionMockMvc
            .perform(get(ENTITY_API_URL_ID, reportExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportExecution.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.error").value(DEFAULT_ERROR))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER));
    }

    @Test
    @Transactional
    void getNonExistingReportExecution() throws Exception {
        // Get the reportExecution
        restReportExecutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportExecution() throws Exception {
        // Initialize the database
        reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution
        ReportExecution updatedReportExecution = reportExecutionRepository.findById(reportExecution.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportExecution are not directly saved in db
        em.detach(updatedReportExecution);
        updatedReportExecution.date(UPDATED_DATE).error(UPDATED_ERROR).url(UPDATED_URL).user(UPDATED_USER);
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(updatedReportExecution);

        restReportExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportExecutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportExecutionToMatchAllProperties(updatedReportExecution);
    }

    @Test
    @Transactional
    void putNonExistingReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportExecutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportExecutionWithPatch() throws Exception {
        // Initialize the database
        reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution using partial update
        ReportExecution partialUpdatedReportExecution = new ReportExecution();
        partialUpdatedReportExecution.setId(reportExecution.getId());

        partialUpdatedReportExecution.date(UPDATED_DATE).error(UPDATED_ERROR).url(UPDATED_URL);

        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportExecution))
            )
            .andExpect(status().isOk());

        // Validate the ReportExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportExecutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportExecution, reportExecution),
            getPersistedReportExecution(reportExecution)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportExecutionWithPatch() throws Exception {
        // Initialize the database
        reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution using partial update
        ReportExecution partialUpdatedReportExecution = new ReportExecution();
        partialUpdatedReportExecution.setId(reportExecution.getId());

        partialUpdatedReportExecution.date(UPDATED_DATE).error(UPDATED_ERROR).url(UPDATED_URL).user(UPDATED_USER);

        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportExecution))
            )
            .andExpect(status().isOk());

        // Validate the ReportExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportExecutionUpdatableFieldsEquals(
            partialUpdatedReportExecution,
            getPersistedReportExecution(partialUpdatedReportExecution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportExecutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportExecution() throws Exception {
        // Initialize the database
        reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportExecution
        restReportExecutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportExecution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportExecutionRepository.count();
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

    protected ReportExecution getPersistedReportExecution(ReportExecution reportExecution) {
        return reportExecutionRepository.findById(reportExecution.getId()).orElseThrow();
    }

    protected void assertPersistedReportExecutionToMatchAllProperties(ReportExecution expectedReportExecution) {
        assertReportExecutionAllPropertiesEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
    }

    protected void assertPersistedReportExecutionToMatchUpdatableProperties(ReportExecution expectedReportExecution) {
        assertReportExecutionAllUpdatablePropertiesEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
    }
}
