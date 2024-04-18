package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportColumnMappingRepository;
import de.adis_portal.server.reporting.service.ReportColumnMappingService;
import de.adis_portal.server.reporting.service.dto.ReportColumnMappingDTO;
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
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.ReportColumnMapping}.
 */
@RestController
@RequestMapping("/api/report-column-mappings")
public class ReportColumnMappingResource {

    private final Logger log = LoggerFactory.getLogger(ReportColumnMappingResource.class);

    private static final String ENTITY_NAME = "relaxReportingReportColumnMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportColumnMappingService reportColumnMappingService;

    private final ReportColumnMappingRepository reportColumnMappingRepository;

    public ReportColumnMappingResource(
        ReportColumnMappingService reportColumnMappingService,
        ReportColumnMappingRepository reportColumnMappingRepository
    ) {
        this.reportColumnMappingService = reportColumnMappingService;
        this.reportColumnMappingRepository = reportColumnMappingRepository;
    }

    /**
     * {@code POST  /report-column-mappings} : Create a new reportColumnMapping.
     *
     * @param reportColumnMappingDTO the reportColumnMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportColumnMappingDTO, or with status {@code 400 (Bad Request)} if the reportColumnMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportColumnMappingDTO> createReportColumnMapping(@RequestBody ReportColumnMappingDTO reportColumnMappingDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportColumnMapping : {}", reportColumnMappingDTO);
        if (reportColumnMappingDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportColumnMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportColumnMappingDTO = reportColumnMappingService.save(reportColumnMappingDTO);
        return ResponseEntity.created(new URI("/api/report-column-mappings/" + reportColumnMappingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportColumnMappingDTO.getId().toString()))
            .body(reportColumnMappingDTO);
    }

    /**
     * {@code PUT  /report-column-mappings/:id} : Updates an existing reportColumnMapping.
     *
     * @param id the id of the reportColumnMappingDTO to save.
     * @param reportColumnMappingDTO the reportColumnMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportColumnMappingDTO,
     * or with status {@code 400 (Bad Request)} if the reportColumnMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportColumnMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportColumnMappingDTO> updateReportColumnMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportColumnMappingDTO reportColumnMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportColumnMapping : {}, {}", id, reportColumnMappingDTO);
        if (reportColumnMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportColumnMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportColumnMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportColumnMappingDTO = reportColumnMappingService.update(reportColumnMappingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportColumnMappingDTO.getId().toString()))
            .body(reportColumnMappingDTO);
    }

    /**
     * {@code PATCH  /report-column-mappings/:id} : Partial updates given fields of an existing reportColumnMapping, field will ignore if it is null
     *
     * @param id the id of the reportColumnMappingDTO to save.
     * @param reportColumnMappingDTO the reportColumnMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportColumnMappingDTO,
     * or with status {@code 400 (Bad Request)} if the reportColumnMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportColumnMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportColumnMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportColumnMappingDTO> partialUpdateReportColumnMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportColumnMappingDTO reportColumnMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportColumnMapping partially : {}, {}", id, reportColumnMappingDTO);
        if (reportColumnMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportColumnMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportColumnMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportColumnMappingDTO> result = reportColumnMappingService.partialUpdate(reportColumnMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportColumnMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-column-mappings} : get all the reportColumnMappings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportColumnMappings in body.
     */
    @GetMapping("")
    public List<ReportColumnMappingDTO> getAllReportColumnMappings() {
        log.debug("REST request to get all ReportColumnMappings");
        return reportColumnMappingService.findAll();
    }

    /**
     * {@code GET  /report-column-mappings/:id} : get the "id" reportColumnMapping.
     *
     * @param id the id of the reportColumnMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportColumnMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportColumnMappingDTO> getReportColumnMapping(@PathVariable("id") Long id) {
        log.debug("REST request to get ReportColumnMapping : {}", id);
        Optional<ReportColumnMappingDTO> reportColumnMappingDTO = reportColumnMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportColumnMappingDTO);
    }

    /**
     * {@code DELETE  /report-column-mappings/:id} : delete the "id" reportColumnMapping.
     *
     * @param id the id of the reportColumnMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportColumnMapping(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReportColumnMapping : {}", id);
        reportColumnMappingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
