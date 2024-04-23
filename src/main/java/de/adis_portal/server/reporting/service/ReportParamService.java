package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportParam;
import de.adis_portal.server.reporting.repository.ReportParamRepository;
import de.adis_portal.server.reporting.service.dto.ReportParamDTO;
import de.adis_portal.server.reporting.service.mapper.ReportParamMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportParam}.
 */
@Service
@Transactional
public class ReportParamService {

    private final Logger log = LoggerFactory.getLogger(ReportParamService.class);

    private final ReportParamRepository reportParamRepository;

    private final ReportParamMapper reportParamMapper;

    public ReportParamService(ReportParamRepository reportParamRepository, ReportParamMapper reportParamMapper) {
        this.reportParamRepository = reportParamRepository;
        this.reportParamMapper = reportParamMapper;
    }

    /**
     * Save a reportParam.
     *
     * @param reportParamDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportParamDTO save(ReportParamDTO reportParamDTO) {
        log.debug("Request to save ReportParam : {}", reportParamDTO);
        ReportParam reportParam = reportParamMapper.toEntity(reportParamDTO);
        reportParam = reportParamRepository.save(reportParam);
        return reportParamMapper.toDto(reportParam);
    }

    /**
     * Update a reportParam.
     *
     * @param reportParamDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportParamDTO update(ReportParamDTO reportParamDTO) {
        log.debug("Request to update ReportParam : {}", reportParamDTO);
        ReportParam reportParam = reportParamMapper.toEntity(reportParamDTO);
        reportParam.setIsPersisted();
        reportParam = reportParamRepository.save(reportParam);
        return reportParamMapper.toDto(reportParam);
    }

    /**
     * Partially update a reportParam.
     *
     * @param reportParamDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportParamDTO> partialUpdate(ReportParamDTO reportParamDTO) {
        log.debug("Request to partially update ReportParam : {}", reportParamDTO);

        return reportParamRepository
            .findById(reportParamDTO.getRid())
            .map(existingReportParam -> {
                reportParamMapper.partialUpdate(existingReportParam, reportParamDTO);

                return existingReportParam;
            })
            .map(reportParamRepository::save)
            .map(reportParamMapper::toDto);
    }

    /**
     * Get all the reportParams.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportParamDTO> findAll() {
        log.debug("Request to get all ReportParams");
        return reportParamRepository.findAll().stream().map(reportParamMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reportParam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportParamDTO> findOne(String id) {
        log.debug("Request to get ReportParam : {}", id);
        return reportParamRepository.findById(id).map(reportParamMapper::toDto);
    }

    /**
     * Delete the reportParam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ReportParam : {}", id);
        reportParamRepository.deleteById(id);
    }
}
