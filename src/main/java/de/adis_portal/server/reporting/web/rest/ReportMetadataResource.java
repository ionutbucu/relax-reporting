package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportMetadataRepository;
import de.adis_portal.server.reporting.service.ReportMetadataService;
import de.adis_portal.server.reporting.service.dto.ReportMetadataDTO;
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
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.ReportMetadata}.
 */
@RestController
@RequestMapping("/api/report-metadata")
public class ReportMetadataResource {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataResource.class);

    private static final String ENTITY_NAME = "relaxReportingReportMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportMetadataService reportMetadataService;

    private final ReportMetadataRepository reportMetadataRepository;

    public ReportMetadataResource(ReportMetadataService reportMetadataService, ReportMetadataRepository reportMetadataRepository) {
        this.reportMetadataService = reportMetadataService;
        this.reportMetadataRepository = reportMetadataRepository;
    }

    /**
     * {@code POST  /report-metadata} : Create a new reportMetadata.
     *
     * @param reportMetadataDTO the reportMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportMetadataDTO, or with status {@code 400 (Bad Request)} if the reportMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportMetadataDTO> createReportMetadata(@RequestBody ReportMetadataDTO reportMetadataDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportMetadata : {}", reportMetadataDTO);
        if (reportMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportMetadataDTO = reportMetadataService.save(reportMetadataDTO);
        return ResponseEntity.created(new URI("/api/report-metadata/" + reportMetadataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportMetadataDTO.getId().toString()))
            .body(reportMetadataDTO);
    }

    /**
     * {@code PUT  /report-metadata/:id} : Updates an existing reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to save.
     * @param reportMetadataDTO the reportMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the reportMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportMetadataDTO> updateReportMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportMetadataDTO reportMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportMetadata : {}, {}", id, reportMetadataDTO);
        if (reportMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportMetadataDTO = reportMetadataService.update(reportMetadataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportMetadataDTO.getId().toString()))
            .body(reportMetadataDTO);
    }

    /**
     * {@code PATCH  /report-metadata/:id} : Partial updates given fields of an existing reportMetadata, field will ignore if it is null
     *
     * @param id the id of the reportMetadataDTO to save.
     * @param reportMetadataDTO the reportMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the reportMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportMetadataDTO> partialUpdateReportMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportMetadataDTO reportMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportMetadata partially : {}, {}", id, reportMetadataDTO);
        if (reportMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportMetadataDTO> result = reportMetadataService.partialUpdate(reportMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-metadata} : get all the reportMetadata.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportMetadata in body.
     */
    @GetMapping("")
    public List<ReportMetadataDTO> getAllReportMetadata(@RequestParam(name = "filter", required = false) String filter) {
        if ("report-is-null".equals(filter)) {
            log.debug("REST request to get all ReportMetadatas where report is null");
            return reportMetadataService.findAllWhereReportIsNull();
        }
        log.debug("REST request to get all ReportMetadata");
        return reportMetadataService.findAll();
    }

    /**
     * {@code GET  /report-metadata/:id} : get the "id" reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportMetadataDTO> getReportMetadata(@PathVariable("id") Long id) {
        log.debug("REST request to get ReportMetadata : {}", id);
        Optional<ReportMetadataDTO> reportMetadataDTO = reportMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportMetadataDTO);
    }

    /**
     * {@code DELETE  /report-metadata/:id} : delete the "id" reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportMetadata(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReportMetadata : {}", id);
        reportMetadataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
