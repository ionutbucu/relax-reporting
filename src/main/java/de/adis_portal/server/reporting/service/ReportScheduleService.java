package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportSchedule;
import de.adis_portal.server.reporting.repository.ReportScheduleRepository;
import de.adis_portal.server.reporting.service.dto.ReportScheduleDTO;
import de.adis_portal.server.reporting.service.mapper.ReportScheduleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportSchedule}.
 */
@Service
@Transactional
public class ReportScheduleService {

    private final Logger log = LoggerFactory.getLogger(ReportScheduleService.class);

    private final ReportScheduleRepository reportScheduleRepository;

    private final ReportScheduleMapper reportScheduleMapper;

    public ReportScheduleService(ReportScheduleRepository reportScheduleRepository, ReportScheduleMapper reportScheduleMapper) {
        this.reportScheduleRepository = reportScheduleRepository;
        this.reportScheduleMapper = reportScheduleMapper;
    }

    /**
     * Save a reportSchedule.
     *
     * @param reportScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportScheduleDTO save(ReportScheduleDTO reportScheduleDTO) {
        log.debug("Request to save ReportSchedule : {}", reportScheduleDTO);
        ReportSchedule reportSchedule = reportScheduleMapper.toEntity(reportScheduleDTO);
        reportSchedule = reportScheduleRepository.save(reportSchedule);
        return reportScheduleMapper.toDto(reportSchedule);
    }

    /**
     * Update a reportSchedule.
     *
     * @param reportScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportScheduleDTO update(ReportScheduleDTO reportScheduleDTO) {
        log.debug("Request to update ReportSchedule : {}", reportScheduleDTO);
        ReportSchedule reportSchedule = reportScheduleMapper.toEntity(reportScheduleDTO);
        reportSchedule.setIsPersisted();
        reportSchedule = reportScheduleRepository.save(reportSchedule);
        return reportScheduleMapper.toDto(reportSchedule);
    }

    /**
     * Partially update a reportSchedule.
     *
     * @param reportScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportScheduleDTO> partialUpdate(ReportScheduleDTO reportScheduleDTO) {
        log.debug("Request to partially update ReportSchedule : {}", reportScheduleDTO);

        return reportScheduleRepository
            .findById(reportScheduleDTO.getRid())
            .map(existingReportSchedule -> {
                reportScheduleMapper.partialUpdate(existingReportSchedule, reportScheduleDTO);

                return existingReportSchedule;
            })
            .map(reportScheduleRepository::save)
            .map(reportScheduleMapper::toDto);
    }

    /**
     * Get all the reportSchedules.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportScheduleDTO> findAll() {
        log.debug("Request to get all ReportSchedules");
        return reportScheduleRepository
            .findAll()
            .stream()
            .map(reportScheduleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reportSchedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportScheduleDTO> findOne(String id) {
        log.debug("Request to get ReportSchedule : {}", id);
        return reportScheduleRepository.findById(id).map(reportScheduleMapper::toDto);
    }

    /**
     * Delete the reportSchedule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ReportSchedule : {}", id);
        reportScheduleRepository.deleteById(id);
    }
}
