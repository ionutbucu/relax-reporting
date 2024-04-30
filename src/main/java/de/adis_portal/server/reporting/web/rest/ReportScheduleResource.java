package de.adis_portal.server.reporting.web.rest;

import de.adis_portal.server.reporting.repository.ReportScheduleRepository;
import de.adis_portal.server.reporting.service.ReportScheduleService;
import de.adis_portal.server.reporting.service.dto.ReportScheduleDTO;
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
 * REST controller for managing {@link de.adis_portal.server.reporting.domain.ReportSchedule}.
 */
@RestController
@RequestMapping("/api/report-schedules")
public class ReportScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ReportScheduleResource.class);

    private static final String ENTITY_NAME = "relaxReportingReportSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportScheduleService reportScheduleService;

    private final ReportScheduleRepository reportScheduleRepository;

    public ReportScheduleResource(ReportScheduleService reportScheduleService, ReportScheduleRepository reportScheduleRepository) {
        this.reportScheduleService = reportScheduleService;
        this.reportScheduleRepository = reportScheduleRepository;
    }

    /**
     * {@code POST  /report-schedules} : Create a new reportSchedule.
     *
     * @param reportScheduleDTO the reportScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportScheduleDTO, or with status {@code 400 (Bad Request)} if the reportSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportScheduleDTO> createReportSchedule(@Valid @RequestBody ReportScheduleDTO reportScheduleDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportSchedule : {}", reportScheduleDTO);
        if (reportScheduleRepository.existsById(reportScheduleDTO.getRid())) {
            throw new BadRequestAlertException("reportSchedule already exists", ENTITY_NAME, "idexists");
        }
        reportScheduleDTO = reportScheduleService.save(reportScheduleDTO);
        return ResponseEntity.created(new URI("/api/report-schedules/" + reportScheduleDTO.getRid()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportScheduleDTO.getRid()))
            .body(reportScheduleDTO);
    }

    /**
     * {@code PUT  /report-schedules/:rid} : Updates an existing reportSchedule.
     *
     * @param rid the id of the reportScheduleDTO to save.
     * @param reportScheduleDTO the reportScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the reportScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public ResponseEntity<ReportScheduleDTO> updateReportSchedule(
        @PathVariable(value = "rid", required = false) final String rid,
        @Valid @RequestBody ReportScheduleDTO reportScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportSchedule : {}, {}", rid, reportScheduleDTO);
        if (reportScheduleDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportScheduleDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportScheduleRepository.existsById(rid)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportScheduleDTO = reportScheduleService.update(reportScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportScheduleDTO.getRid()))
            .body(reportScheduleDTO);
    }

    /**
     * {@code PATCH  /report-schedules/:rid} : Partial updates given fields of an existing reportSchedule, field will ignore if it is null
     *
     * @param rid the id of the reportScheduleDTO to save.
     * @param reportScheduleDTO the reportScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the reportScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportScheduleDTO> partialUpdateReportSchedule(
        @PathVariable(value = "rid", required = false) final String rid,
        @NotNull @RequestBody ReportScheduleDTO reportScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportSchedule partially : {}, {}", rid, reportScheduleDTO);
        if (reportScheduleDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportScheduleDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportScheduleRepository.existsById(rid)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportScheduleDTO> result = reportScheduleService.partialUpdate(reportScheduleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportScheduleDTO.getRid())
        );
    }

    /**
     * {@code GET  /report-schedules} : get all the reportSchedules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportSchedules in body.
     */
    @GetMapping("")
    public List<ReportScheduleDTO> getAllReportSchedules() {
        log.debug("REST request to get all ReportSchedules");
        return reportScheduleService.findAll();
    }

    /**
     * {@code GET  /report-schedules/:id} : get the "id" reportSchedule.
     *
     * @param id the id of the reportScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportScheduleDTO> getReportSchedule(@PathVariable("id") String id) {
        log.debug("REST request to get ReportSchedule : {}", id);
        Optional<ReportScheduleDTO> reportScheduleDTO = reportScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportScheduleDTO);
    }

    /**
     * {@code DELETE  /report-schedules/:id} : delete the "id" reportSchedule.
     *
     * @param id the id of the reportScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportSchedule(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportSchedule : {}", id);
        reportScheduleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
