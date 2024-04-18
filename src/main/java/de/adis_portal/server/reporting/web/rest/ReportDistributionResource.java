package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportDistributionRepository;
import de.adis_portal.server.reporting.service.ReportDistributionService;
import de.adis_portal.server.reporting.service.dto.ReportDistributionDTO;
import de.adis_portal.server.reporting.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.ReportDistribution}.
 */
@RestController
@RequestMapping("/api/report-distributions")
public class ReportDistributionResource {

    private final Logger log = LoggerFactory.getLogger(ReportDistributionResource.class);

    private static final String ENTITY_NAME = "relaxReportingReportDistribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportDistributionService reportDistributionService;

    private final ReportDistributionRepository reportDistributionRepository;

    public ReportDistributionResource(
        ReportDistributionService reportDistributionService,
        ReportDistributionRepository reportDistributionRepository
    ) {
        this.reportDistributionService = reportDistributionService;
        this.reportDistributionRepository = reportDistributionRepository;
    }

    /**
     * {@code POST  /report-distributions} : Create a new reportDistribution.
     *
     * @param reportDistributionDTO the reportDistributionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDistributionDTO, or with status {@code 400 (Bad Request)} if the reportDistribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportDistributionDTO> createReportDistribution(@Valid @RequestBody ReportDistributionDTO reportDistributionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportDistribution : {}", reportDistributionDTO);
        if (reportDistributionDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportDistribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportDistributionDTO = reportDistributionService.save(reportDistributionDTO);
        return ResponseEntity.created(new URI("/api/report-distributions/" + reportDistributionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportDistributionDTO.getId().toString()))
            .body(reportDistributionDTO);
    }

    /**
     * {@code PUT  /report-distributions/:id} : Updates an existing reportDistribution.
     *
     * @param id the id of the reportDistributionDTO to save.
     * @param reportDistributionDTO the reportDistributionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDistributionDTO,
     * or with status {@code 400 (Bad Request)} if the reportDistributionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDistributionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDistributionDTO> updateReportDistribution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportDistributionDTO reportDistributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportDistribution : {}, {}", id, reportDistributionDTO);
        if (reportDistributionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDistributionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDistributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportDistributionDTO = reportDistributionService.update(reportDistributionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDistributionDTO.getId().toString()))
            .body(reportDistributionDTO);
    }

    /**
     * {@code PATCH  /report-distributions/:id} : Partial updates given fields of an existing reportDistribution, field will ignore if it is null
     *
     * @param id the id of the reportDistributionDTO to save.
     * @param reportDistributionDTO the reportDistributionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDistributionDTO,
     * or with status {@code 400 (Bad Request)} if the reportDistributionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDistributionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDistributionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportDistributionDTO> partialUpdateReportDistribution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportDistributionDTO reportDistributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportDistribution partially : {}, {}", id, reportDistributionDTO);
        if (reportDistributionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDistributionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDistributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportDistributionDTO> result = reportDistributionService.partialUpdate(reportDistributionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDistributionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-distributions} : get all the reportDistributions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportDistributions in body.
     */
    @GetMapping("")
    public List<ReportDistributionDTO> getAllReportDistributions() {
        log.debug("REST request to get all ReportDistributions");
        return reportDistributionService.findAll();
    }

    /**
     * {@code GET  /report-distributions/:id} : get the "id" reportDistribution.
     *
     * @param id the id of the reportDistributionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDistributionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDistributionDTO> getReportDistribution(@PathVariable("id") Long id) {
        log.debug("REST request to get ReportDistribution : {}", id);
        Optional<ReportDistributionDTO> reportDistributionDTO = reportDistributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDistributionDTO);
    }

    /**
     * {@code DELETE  /report-distributions/:id} : delete the "id" reportDistribution.
     *
     * @param id the id of the reportDistributionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportDistribution(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReportDistribution : {}", id);
        reportDistributionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
