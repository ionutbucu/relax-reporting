package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportExecution;
import de.adis_portal.server.reporting.repository.ReportExecutionRepository;
import de.adis_portal.server.reporting.service.dto.ReportExecutionDTO;
import de.adis_portal.server.reporting.service.mapper.ReportExecutionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportExecution}.
 */
@Service
@Transactional
public class ReportExecutionService {

    private final Logger log = LoggerFactory.getLogger(ReportExecutionService.class);

    private final ReportExecutionRepository reportExecutionRepository;

    private final ReportExecutionMapper reportExecutionMapper;

    public ReportExecutionService(ReportExecutionRepository reportExecutionRepository, ReportExecutionMapper reportExecutionMapper) {
        this.reportExecutionRepository = reportExecutionRepository;
        this.reportExecutionMapper = reportExecutionMapper;
    }

    /**
     * Save a reportExecution.
     *
     * @param reportExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportExecutionDTO save(ReportExecutionDTO reportExecutionDTO) {
        log.debug("Request to save ReportExecution : {}", reportExecutionDTO);
        ReportExecution reportExecution = reportExecutionMapper.toEntity(reportExecutionDTO);
        reportExecution = reportExecutionRepository.save(reportExecution);
        return reportExecutionMapper.toDto(reportExecution);
    }

    /**
     * Update a reportExecution.
     *
     * @param reportExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportExecutionDTO update(ReportExecutionDTO reportExecutionDTO) {
        log.debug("Request to update ReportExecution : {}", reportExecutionDTO);
        ReportExecution reportExecution = reportExecutionMapper.toEntity(reportExecutionDTO);
        reportExecution = reportExecutionRepository.save(reportExecution);
        return reportExecutionMapper.toDto(reportExecution);
    }

    /**
     * Partially update a reportExecution.
     *
     * @param reportExecutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportExecutionDTO> partialUpdate(ReportExecutionDTO reportExecutionDTO) {
        log.debug("Request to partially update ReportExecution : {}", reportExecutionDTO);

        return reportExecutionRepository
            .findById(reportExecutionDTO.getId())
            .map(existingReportExecution -> {
                reportExecutionMapper.partialUpdate(existingReportExecution, reportExecutionDTO);

                return existingReportExecution;
            })
            .map(reportExecutionRepository::save)
            .map(reportExecutionMapper::toDto);
    }

    /**
     * Get all the reportExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportExecutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportExecutions");
        return reportExecutionRepository.findAll(pageable).map(reportExecutionMapper::toDto);
    }

    /**
     * Get one reportExecution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportExecutionDTO> findOne(Long id) {
        log.debug("Request to get ReportExecution : {}", id);
        return reportExecutionRepository.findById(id).map(reportExecutionMapper::toDto);
    }

    /**
     * Delete the reportExecution by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReportExecution : {}", id);
        reportExecutionRepository.deleteById(id);
    }
}
