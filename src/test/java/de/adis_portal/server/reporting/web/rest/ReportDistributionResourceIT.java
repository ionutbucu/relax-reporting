package de.adis_portal.server.reporting.web.rest;

import static de.adis_portal.server.reporting.domain.ReportDistributionAsserts.*;
import static de.adis_portal.server.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adis_portal.server.reporting.IntegrationTest;
import de.adis_portal.server.reporting.domain.ReportDistribution;
import de.adis_portal.server.reporting.repository.ReportDistributionRepository;
import de.adis_portal.server.reporting.service.dto.ReportDistributionDTO;
import de.adis_portal.server.reporting.service.mapper.ReportDistributionMapper;
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
 * Integration tests for the {@link ReportDistributionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportDistributionResourceIT {

    private static final String DEFAULT_EMAIL = "wH51U#@`W(f";
    private static final String UPDATED_EMAIL = "t&XZAw@m=";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-distributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportDistributionRepository reportDistributionRepository;

    @Autowired
    private ReportDistributionMapper reportDistributionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportDistributionMockMvc;

    private ReportDistribution reportDistribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDistribution createEntity(EntityManager em) {
        ReportDistribution reportDistribution = new ReportDistribution().email(DEFAULT_EMAIL).description(DEFAULT_DESCRIPTION);
        return reportDistribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDistribution createUpdatedEntity(EntityManager em) {
        ReportDistribution reportDistribution = new ReportDistribution().email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);
        return reportDistribution;
    }

    @BeforeEach
    public void initTest() {
        reportDistribution = createEntity(em);
    }

    @Test
    @Transactional
    void createReportDistribution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);
        var returnedReportDistributionDTO = om.readValue(
            restReportDistributionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDistributionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportDistributionDTO.class
        );

        // Validate the ReportDistribution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportDistribution = reportDistributionMapper.toEntity(returnedReportDistributionDTO);
        assertReportDistributionUpdatableFieldsEquals(
            returnedReportDistribution,
            getPersistedReportDistribution(returnedReportDistribution)
        );
    }

    @Test
    @Transactional
    void createReportDistributionWithExistingId() throws Exception {
        // Create the ReportDistribution with an existing ID
        reportDistribution.setId(1L);
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportDistributionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDistributionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportDistribution.setEmail(null);

        // Create the ReportDistribution, which fails.
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        restReportDistributionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDistributionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportDistributions() throws Exception {
        // Initialize the database
        reportDistributionRepository.saveAndFlush(reportDistribution);

        // Get all the reportDistributionList
        restReportDistributionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDistribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getReportDistribution() throws Exception {
        // Initialize the database
        reportDistributionRepository.saveAndFlush(reportDistribution);

        // Get the reportDistribution
        restReportDistributionMockMvc
            .perform(get(ENTITY_API_URL_ID, reportDistribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportDistribution.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingReportDistribution() throws Exception {
        // Get the reportDistribution
        restReportDistributionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportDistribution() throws Exception {
        // Initialize the database
        reportDistributionRepository.saveAndFlush(reportDistribution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDistribution
        ReportDistribution updatedReportDistribution = reportDistributionRepository.findById(reportDistribution.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportDistribution are not directly saved in db
        em.detach(updatedReportDistribution);
        updatedReportDistribution.email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(updatedReportDistribution);

        restReportDistributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDistributionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDistributionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportDistributionToMatchAllProperties(updatedReportDistribution);
    }

    @Test
    @Transactional
    void putNonExistingReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setId(longCount.incrementAndGet());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDistributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDistributionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDistributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setId(longCount.incrementAndGet());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDistributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDistributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setId(longCount.incrementAndGet());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDistributionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDistributionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportDistributionWithPatch() throws Exception {
        // Initialize the database
        reportDistributionRepository.saveAndFlush(reportDistribution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDistribution using partial update
        ReportDistribution partialUpdatedReportDistribution = new ReportDistribution();
        partialUpdatedReportDistribution.setId(reportDistribution.getId());

        partialUpdatedReportDistribution.email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);

        restReportDistributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDistribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportDistribution))
            )
            .andExpect(status().isOk());

        // Validate the ReportDistribution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDistributionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportDistribution, reportDistribution),
            getPersistedReportDistribution(reportDistribution)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportDistributionWithPatch() throws Exception {
        // Initialize the database
        reportDistributionRepository.saveAndFlush(reportDistribution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDistribution using partial update
        ReportDistribution partialUpdatedReportDistribution = new ReportDistribution();
        partialUpdatedReportDistribution.setId(reportDistribution.getId());

        partialUpdatedReportDistribution.email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);

        restReportDistributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDistribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportDistribution))
            )
            .andExpect(status().isOk());

        // Validate the ReportDistribution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDistributionUpdatableFieldsEquals(
            partialUpdatedReportDistribution,
            getPersistedReportDistribution(partialUpdatedReportDistribution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setId(longCount.incrementAndGet());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDistributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDistributionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDistributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setId(longCount.incrementAndGet());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDistributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDistributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setId(longCount.incrementAndGet());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDistributionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportDistributionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportDistribution() throws Exception {
        // Initialize the database
        reportDistributionRepository.saveAndFlush(reportDistribution);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportDistribution
        restReportDistributionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportDistribution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportDistributionRepository.count();
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

    protected ReportDistribution getPersistedReportDistribution(ReportDistribution reportDistribution) {
        return reportDistributionRepository.findById(reportDistribution.getId()).orElseThrow();
    }

    protected void assertPersistedReportDistributionToMatchAllProperties(ReportDistribution expectedReportDistribution) {
        assertReportDistributionAllPropertiesEquals(expectedReportDistribution, getPersistedReportDistribution(expectedReportDistribution));
    }

    protected void assertPersistedReportDistributionToMatchUpdatableProperties(ReportDistribution expectedReportDistribution) {
        assertReportDistributionAllUpdatablePropertiesEquals(
            expectedReportDistribution,
            getPersistedReportDistribution(expectedReportDistribution)
        );
    }
}
