package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportParamAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportParam;
import de.adis_portal.server.reporting.repository.ReportParamRepository;
import de.adis_portal.server.reporting.service.dto.ReportParamDTO;
import de.adis_portal.server.reporting.service.mapper.ReportParamMapper;
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
 * Integration tests for the {@link ReportParamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportParamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSION_RULE = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSION_RULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-params";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportParamRepository reportParamRepository;

    @Autowired
    private ReportParamMapper reportParamMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportParamMockMvc;

    private ReportParam reportParam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportParam createEntity(EntityManager em) {
        ReportParam reportParam = new ReportParam()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
            .conversionRule(DEFAULT_CONVERSION_RULE);
        return reportParam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportParam createUpdatedEntity(EntityManager em) {
        ReportParam reportParam = new ReportParam()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .conversionRule(UPDATED_CONVERSION_RULE);
        return reportParam;
    }

    @BeforeEach
    public void initTest() {
        reportParam = createEntity(em);
    }

    @Test
    @Transactional
    void createReportParam() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);
        var returnedReportParamDTO = om.readValue(
            restReportParamMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportParamDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportParamDTO.class
        );

        // Validate the ReportParam in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportParam = reportParamMapper.toEntity(returnedReportParamDTO);
        assertReportParamUpdatableFieldsEquals(returnedReportParam, getPersistedReportParam(returnedReportParam));
    }

    @Test
    @Transactional
    void createReportParamWithExistingId() throws Exception {
        // Create the ReportParam with an existing ID
        reportParam.setId(1L);
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportParamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportParamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportParams() throws Exception {
        // Initialize the database
        reportParamRepository.saveAndFlush(reportParam);

        // Get all the reportParamList
        restReportParamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportParam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].conversionRule").value(hasItem(DEFAULT_CONVERSION_RULE)));
    }

    @Test
    @Transactional
    void getReportParam() throws Exception {
        // Initialize the database
        reportParamRepository.saveAndFlush(reportParam);

        // Get the reportParam
        restReportParamMockMvc
            .perform(get(ENTITY_API_URL_ID, reportParam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportParam.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.conversionRule").value(DEFAULT_CONVERSION_RULE));
    }

    @Test
    @Transactional
    void getNonExistingReportParam() throws Exception {
        // Get the reportParam
        restReportParamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportParam() throws Exception {
        // Initialize the database
        reportParamRepository.saveAndFlush(reportParam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportParam
        ReportParam updatedReportParam = reportParamRepository.findById(reportParam.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportParam are not directly saved in db
        em.detach(updatedReportParam);
        updatedReportParam.name(UPDATED_NAME).type(UPDATED_TYPE).value(UPDATED_VALUE).conversionRule(UPDATED_CONVERSION_RULE);
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(updatedReportParam);

        restReportParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportParamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportParamDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportParamToMatchAllProperties(updatedReportParam);
    }

    @Test
    @Transactional
    void putNonExistingReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setId(longCount.incrementAndGet());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportParamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setId(longCount.incrementAndGet());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setId(longCount.incrementAndGet());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportParamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportParamDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportParamWithPatch() throws Exception {
        // Initialize the database
        reportParamRepository.saveAndFlush(reportParam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportParam using partial update
        ReportParam partialUpdatedReportParam = new ReportParam();
        partialUpdatedReportParam.setId(reportParam.getId());

        partialUpdatedReportParam.name(UPDATED_NAME).value(UPDATED_VALUE);

        restReportParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportParam))
            )
            .andExpect(status().isOk());

        // Validate the ReportParam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportParamUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportParam, reportParam),
            getPersistedReportParam(reportParam)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportParamWithPatch() throws Exception {
        // Initialize the database
        reportParamRepository.saveAndFlush(reportParam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportParam using partial update
        ReportParam partialUpdatedReportParam = new ReportParam();
        partialUpdatedReportParam.setId(reportParam.getId());

        partialUpdatedReportParam.name(UPDATED_NAME).type(UPDATED_TYPE).value(UPDATED_VALUE).conversionRule(UPDATED_CONVERSION_RULE);

        restReportParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportParam))
            )
            .andExpect(status().isOk());

        // Validate the ReportParam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportParamUpdatableFieldsEquals(partialUpdatedReportParam, getPersistedReportParam(partialUpdatedReportParam));
    }

    @Test
    @Transactional
    void patchNonExistingReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setId(longCount.incrementAndGet());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportParamDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setId(longCount.incrementAndGet());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setId(longCount.incrementAndGet());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportParamMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportParamDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportParam() throws Exception {
        // Initialize the database
        reportParamRepository.saveAndFlush(reportParam);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportParam
        restReportParamMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportParam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportParamRepository.count();
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

    protected ReportParam getPersistedReportParam(ReportParam reportParam) {
        return reportParamRepository.findById(reportParam.getId()).orElseThrow();
    }

    protected void assertPersistedReportParamToMatchAllProperties(ReportParam expectedReportParam) {
        assertReportParamAllPropertiesEquals(expectedReportParam, getPersistedReportParam(expectedReportParam));
    }

    protected void assertPersistedReportParamToMatchUpdatableProperties(ReportParam expectedReportParam) {
        assertReportParamAllUpdatablePropertiesEquals(expectedReportParam, getPersistedReportParam(expectedReportParam));
    }
}
