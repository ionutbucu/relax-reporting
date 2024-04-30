package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportRepository;
import de.adis_portal.server.reporting.service.ReportService;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.Report}.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private static final String ENTITY_NAME = "relaxReportingReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportService reportService;

    private final ReportRepository reportRepository;

    public ReportResource(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    /**
     * {@code POST  /reports} : Create a new report.
     *
     * @param reportDTO the reportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDTO, or with status {@code 400 (Bad Request)} if the report has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody ReportDTO reportDTO) throws URISyntaxException {
        log.debug("REST request to save Report : {}", reportDTO);
        if (reportRepository.existsById(reportDTO.getRid())) {
            throw new BadRequestAlertException("report already exists", ENTITY_NAME, "idexists");
        }
        reportDTO = reportService.save(reportDTO);
        return ResponseEntity.created(new URI("/api/reports/" + reportDTO.getRid()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportDTO.getRid()))
            .body(reportDTO);
    }

    /**
     * {@code PUT  /reports/:rid} : Updates an existing report.
     *
     * @param rid the id of the reportDTO to save.
     * @param reportDTO the reportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDTO,
     * or with status {@code 400 (Bad Request)} if the reportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public ResponseEntity<ReportDTO> updateReport(
        @PathVariable(value = "rid", required = false) final String rid,
        @Valid @RequestBody ReportDTO reportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Report : {}, {}", rid, reportDTO);
        if (reportDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRepository.existsById(rid)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportDTO = reportService.update(reportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDTO.getRid()))
            .body(reportDTO);
    }

    /**
     * {@code PATCH  /reports/:rid} : Partial updates given fields of an existing report, field will ignore if it is null
     *
     * @param rid the id of the reportDTO to save.
     * @param reportDTO the reportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDTO,
     * or with status {@code 400 (Bad Request)} if the reportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportDTO> partialUpdateReport(
        @PathVariable(value = "rid", required = false) final String rid,
        @NotNull @RequestBody ReportDTO reportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Report partially : {}, {}", rid, reportDTO);
        if (reportDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRepository.existsById(rid)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportDTO> result = reportService.partialUpdate(reportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDTO.getRid())
        );
    }

    /**
     * {@code GET  /reports} : get all the reports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportDTO>> getAllReports(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Reports");
        Page<ReportDTO> page = reportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reports/:id} : get the "id" report.
     *
     * @param id the id of the reportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable("id") String id) {
        log.debug("REST request to get Report : {}", id);
        Optional<ReportDTO> reportDTO = reportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDTO);
    }

    /**
     * {@code DELETE  /reports/:id} : delete the "id" report.
     *
     * @param id the id of the reportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable("id") String id) {
        log.debug("REST request to delete Report : {}", id);
        reportService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
