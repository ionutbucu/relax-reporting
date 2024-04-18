package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportScheduleAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportSchedule;
import de.adis_portal.server.reporting.repository.ReportScheduleRepository;
import de.adis_portal.server.reporting.service.dto.ReportScheduleDTO;
import de.adis_portal.server.reporting.service.mapper.ReportScheduleMapper;
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
 * Integration tests for the {@link ReportScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportScheduleResourceIT {

    private static final String DEFAULT_CRON = "AAAAAAAAAA";
    private static final String UPDATED_CRON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportScheduleRepository reportScheduleRepository;

    @Autowired
    private ReportScheduleMapper reportScheduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportScheduleMockMvc;

    private ReportSchedule reportSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSchedule createEntity(EntityManager em) {
        ReportSchedule reportSchedule = new ReportSchedule().cron(DEFAULT_CRON);
        return reportSchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSchedule createUpdatedEntity(EntityManager em) {
        ReportSchedule reportSchedule = new ReportSchedule().cron(UPDATED_CRON);
        return reportSchedule;
    }

    @BeforeEach
    public void initTest() {
        reportSchedule = createEntity(em);
    }

    @Test
    @Transactional
    void createReportSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);
        var returnedReportScheduleDTO = om.readValue(
            restReportScheduleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportScheduleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportScheduleDTO.class
        );

        // Validate the ReportSchedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportSchedule = reportScheduleMapper.toEntity(returnedReportScheduleDTO);
        assertReportScheduleUpdatableFieldsEquals(returnedReportSchedule, getPersistedReportSchedule(returnedReportSchedule));
    }

    @Test
    @Transactional
    void createReportScheduleWithExistingId() throws Exception {
        // Create the ReportSchedule with an existing ID
        reportSchedule.setId(1L);
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCronIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportSchedule.setCron(null);

        // Create the ReportSchedule, which fails.
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        restReportScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportSchedules() throws Exception {
        // Initialize the database
        reportScheduleRepository.saveAndFlush(reportSchedule);

        // Get all the reportScheduleList
        restReportScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].cron").value(hasItem(DEFAULT_CRON)));
    }

    @Test
    @Transactional
    void getReportSchedule() throws Exception {
        // Initialize the database
        reportScheduleRepository.saveAndFlush(reportSchedule);

        // Get the reportSchedule
        restReportScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, reportSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportSchedule.getId().intValue()))
            .andExpect(jsonPath("$.cron").value(DEFAULT_CRON));
    }

    @Test
    @Transactional
    void getNonExistingReportSchedule() throws Exception {
        // Get the reportSchedule
        restReportScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportSchedule() throws Exception {
        // Initialize the database
        reportScheduleRepository.saveAndFlush(reportSchedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSchedule
        ReportSchedule updatedReportSchedule = reportScheduleRepository.findById(reportSchedule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportSchedule are not directly saved in db
        em.detach(updatedReportSchedule);
        updatedReportSchedule.cron(UPDATED_CRON);
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(updatedReportSchedule);

        restReportScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportScheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportScheduleToMatchAllProperties(updatedReportSchedule);
    }

    @Test
    @Transactional
    void putNonExistingReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setId(longCount.incrementAndGet());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setId(longCount.incrementAndGet());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setId(longCount.incrementAndGet());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportScheduleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportScheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportScheduleWithPatch() throws Exception {
        // Initialize the database
        reportScheduleRepository.saveAndFlush(reportSchedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSchedule using partial update
        ReportSchedule partialUpdatedReportSchedule = new ReportSchedule();
        partialUpdatedReportSchedule.setId(reportSchedule.getId());

        restReportScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportSchedule))
            )
            .andExpect(status().isOk());

        // Validate the ReportSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportScheduleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportSchedule, reportSchedule),
            getPersistedReportSchedule(reportSchedule)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportScheduleWithPatch() throws Exception {
        // Initialize the database
        reportScheduleRepository.saveAndFlush(reportSchedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSchedule using partial update
        ReportSchedule partialUpdatedReportSchedule = new ReportSchedule();
        partialUpdatedReportSchedule.setId(reportSchedule.getId());

        partialUpdatedReportSchedule.cron(UPDATED_CRON);

        restReportScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportSchedule))
            )
            .andExpect(status().isOk());

        // Validate the ReportSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportScheduleUpdatableFieldsEquals(partialUpdatedReportSchedule, getPersistedReportSchedule(partialUpdatedReportSchedule));
    }

    @Test
    @Transactional
    void patchNonExistingReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setId(longCount.incrementAndGet());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportScheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setId(longCount.incrementAndGet());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setId(longCount.incrementAndGet());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportScheduleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportScheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportSchedule() throws Exception {
        // Initialize the database
        reportScheduleRepository.saveAndFlush(reportSchedule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportSchedule
        restReportScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportSchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportScheduleRepository.count();
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

    protected ReportSchedule getPersistedReportSchedule(ReportSchedule reportSchedule) {
        return reportScheduleRepository.findById(reportSchedule.getId()).orElseThrow();
    }

    protected void assertPersistedReportScheduleToMatchAllProperties(ReportSchedule expectedReportSchedule) {
        assertReportScheduleAllPropertiesEquals(expectedReportSchedule, getPersistedReportSchedule(expectedReportSchedule));
    }

    protected void assertPersistedReportScheduleToMatchUpdatableProperties(ReportSchedule expectedReportSchedule) {
        assertReportScheduleAllUpdatablePropertiesEquals(expectedReportSchedule, getPersistedReportSchedule(expectedReportSchedule));
    }
}
