package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportDataSourceRepository;
import de.adis_portal.server.reporting.service.ReportDataSourceService;
import de.adis_portal.server.reporting.service.dto.ReportDataSourceDTO;
import de.adis_portal.server.reporting.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.ReportDataSource}.
 */
@RestController
@RequestMapping("/api/report-data-sources")
public class ReportDataSourceResource {

    private final Logger log = LoggerFactory.getLogger(ReportDataSourceResource.class);

    private static final String ENTITY_NAME = "relaxReportingReportDataSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportDataSourceService reportDataSourceService;

    private final ReportDataSourceRepository reportDataSourceRepository;

    public ReportDataSourceResource(
        ReportDataSourceService reportDataSourceService,
        ReportDataSourceRepository reportDataSourceRepository
    ) {
        this.reportDataSourceService = reportDataSourceService;
        this.reportDataSourceRepository = reportDataSourceRepository;
    }

    /**
     * {@code POST  /report-data-sources} : Create a new reportDataSource.
     *
     * @param reportDataSourceDTO the reportDataSourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDataSourceDTO, or with status {@code 400 (Bad Request)} if the reportDataSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportDataSourceDTO> createReportDataSource(@RequestBody ReportDataSourceDTO reportDataSourceDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportDataSource : {}", reportDataSourceDTO);
        if (reportDataSourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportDataSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportDataSourceDTO = reportDataSourceService.save(reportDataSourceDTO);
        return ResponseEntity.created(new URI("/api/report-data-sources/" + reportDataSourceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportDataSourceDTO.getId().toString()))
            .body(reportDataSourceDTO);
    }

    /**
     * {@code PUT  /report-data-sources/:id} : Updates an existing reportDataSource.
     *
     * @param id the id of the reportDataSourceDTO to save.
     * @param reportDataSourceDTO the reportDataSourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDataSourceDTO,
     * or with status {@code 400 (Bad Request)} if the reportDataSourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDataSourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDataSourceDTO> updateReportDataSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportDataSourceDTO reportDataSourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportDataSource : {}, {}", id, reportDataSourceDTO);
        if (reportDataSourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDataSourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDataSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportDataSourceDTO = reportDataSourceService.update(reportDataSourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDataSourceDTO.getId().toString()))
            .body(reportDataSourceDTO);
    }

    /**
     * {@code PATCH  /report-data-sources/:id} : Partial updates given fields of an existing reportDataSource, field will ignore if it is null
     *
     * @param id the id of the reportDataSourceDTO to save.
     * @param reportDataSourceDTO the reportDataSourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDataSourceDTO,
     * or with status {@code 400 (Bad Request)} if the reportDataSourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDataSourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDataSourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportDataSourceDTO> partialUpdateReportDataSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportDataSourceDTO reportDataSourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportDataSource partially : {}, {}", id, reportDataSourceDTO);
        if (reportDataSourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDataSourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDataSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportDataSourceDTO> result = reportDataSourceService.partialUpdate(reportDataSourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDataSourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-data-sources} : get all the reportDataSources.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportDataSources in body.
     */
    @GetMapping("")
    public List<ReportDataSourceDTO> getAllReportDataSources(@RequestParam(name = "filter", required = false) String filter) {
        if ("report-is-null".equals(filter)) {
            log.debug("REST request to get all ReportDataSources where report is null");
            return reportDataSourceService.findAllWhereReportIsNull();
        }
        log.debug("REST request to get all ReportDataSources");
        return reportDataSourceService.findAll();
    }

    /**
     * {@code GET  /report-data-sources/:id} : get the "id" reportDataSource.
     *
     * @param id the id of the reportDataSourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDataSourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDataSourceDTO> getReportDataSource(@PathVariable("id") Long id) {
        log.debug("REST request to get ReportDataSource : {}", id);
        Optional<ReportDataSourceDTO> reportDataSourceDTO = reportDataSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDataSourceDTO);
    }

    /**
     * {@code DELETE  /report-data-sources/:id} : delete the "id" reportDataSource.
     *
     * @param id the id of the reportDataSourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportDataSource(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReportDataSource : {}", id);
        reportDataSourceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
