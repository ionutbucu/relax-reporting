package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportParamRepository;
import de.adis_portal.server.reporting.service.ReportParamService;
import de.adis_portal.server.reporting.service.dto.ReportParamDTO;
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
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.ReportParam}.
 */
@RestController
@RequestMapping("/api/report-params")
public class ReportParamResource {

    private final Logger log = LoggerFactory.getLogger(ReportParamResource.class);

    private static final String ENTITY_NAME = "relaxReportingReportParam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportParamService reportParamService;

    private final ReportParamRepository reportParamRepository;

    public ReportParamResource(ReportParamService reportParamService, ReportParamRepository reportParamRepository) {
        this.reportParamService = reportParamService;
        this.reportParamRepository = reportParamRepository;
    }

    /**
     * {@code POST  /report-params} : Create a new reportParam.
     *
     * @param reportParamDTO the reportParamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportParamDTO, or with status {@code 400 (Bad Request)} if the reportParam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportParamDTO> createReportParam(@RequestBody ReportParamDTO reportParamDTO) throws URISyntaxException {
        log.debug("REST request to save ReportParam : {}", reportParamDTO);
        if (reportParamDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportParam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportParamDTO = reportParamService.save(reportParamDTO);
        return ResponseEntity.created(new URI("/api/report-params/" + reportParamDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportParamDTO.getId().toString()))
            .body(reportParamDTO);
    }

    /**
     * {@code PUT  /report-params/:id} : Updates an existing reportParam.
     *
     * @param id the id of the reportParamDTO to save.
     * @param reportParamDTO the reportParamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportParamDTO,
     * or with status {@code 400 (Bad Request)} if the reportParamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportParamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportParamDTO> updateReportParam(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportParamDTO reportParamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportParam : {}, {}", id, reportParamDTO);
        if (reportParamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportParamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportParamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportParamDTO = reportParamService.update(reportParamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportParamDTO.getId().toString()))
            .body(reportParamDTO);
    }

    /**
     * {@code PATCH  /report-params/:id} : Partial updates given fields of an existing reportParam, field will ignore if it is null
     *
     * @param id the id of the reportParamDTO to save.
     * @param reportParamDTO the reportParamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportParamDTO,
     * or with status {@code 400 (Bad Request)} if the reportParamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportParamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportParamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportParamDTO> partialUpdateReportParam(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportParamDTO reportParamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportParam partially : {}, {}", id, reportParamDTO);
        if (reportParamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportParamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportParamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportParamDTO> result = reportParamService.partialUpdate(reportParamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportParamDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-params} : get all the reportParams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportParams in body.
     */
    @GetMapping("")
    public List<ReportParamDTO> getAllReportParams() {
        log.debug("REST request to get all ReportParams");
        return reportParamService.findAll();
    }

    /**
     * {@code GET  /report-params/:id} : get the "id" reportParam.
     *
     * @param id the id of the reportParamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportParamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportParamDTO> getReportParam(@PathVariable("id") Long id) {
        log.debug("REST request to get ReportParam : {}", id);
        Optional<ReportParamDTO> reportParamDTO = reportParamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportParamDTO);
    }

    /**
     * {@code DELETE  /report-params/:id} : delete the "id" reportParam.
     *
     * @param id the id of the reportParamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportParam(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReportParam : {}", id);
        reportParamService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
