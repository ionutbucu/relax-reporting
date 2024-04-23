package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportDistribution;
import de.adis_portal.server.reporting.repository.ReportDistributionRepository;
import de.adis_portal.server.reporting.service.dto.ReportDistributionDTO;
import de.adis_portal.server.reporting.service.mapper.ReportDistributionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportDistribution}.
 */
@Service
@Transactional
public class ReportDistributionService {

    private final Logger log = LoggerFactory.getLogger(ReportDistributionService.class);

    private final ReportDistributionRepository reportDistributionRepository;

    private final ReportDistributionMapper reportDistributionMapper;

    public ReportDistributionService(
        ReportDistributionRepository reportDistributionRepository,
        ReportDistributionMapper reportDistributionMapper
    ) {
        this.reportDistributionRepository = reportDistributionRepository;
        this.reportDistributionMapper = reportDistributionMapper;
    }

    /**
     * Save a reportDistribution.
     *
     * @param reportDistributionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDistributionDTO save(ReportDistributionDTO reportDistributionDTO) {
        log.debug("Request to save ReportDistribution : {}", reportDistributionDTO);
        ReportDistribution reportDistribution = reportDistributionMapper.toEntity(reportDistributionDTO);
        reportDistribution = reportDistributionRepository.save(reportDistribution);
        return reportDistributionMapper.toDto(reportDistribution);
    }

    /**
     * Update a reportDistribution.
     *
     * @param reportDistributionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDistributionDTO update(ReportDistributionDTO reportDistributionDTO) {
        log.debug("Request to update ReportDistribution : {}", reportDistributionDTO);
        ReportDistribution reportDistribution = reportDistributionMapper.toEntity(reportDistributionDTO);
        reportDistribution.setIsPersisted();
        reportDistribution = reportDistributionRepository.save(reportDistribution);
        return reportDistributionMapper.toDto(reportDistribution);
    }

    /**
     * Partially update a reportDistribution.
     *
     * @param reportDistributionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportDistributionDTO> partialUpdate(ReportDistributionDTO reportDistributionDTO) {
        log.debug("Request to partially update ReportDistribution : {}", reportDistributionDTO);

        return reportDistributionRepository
            .findById(reportDistributionDTO.getRid())
            .map(existingReportDistribution -> {
                reportDistributionMapper.partialUpdate(existingReportDistribution, reportDistributionDTO);

                return existingReportDistribution;
            })
            .map(reportDistributionRepository::save)
            .map(reportDistributionMapper::toDto);
    }

    /**
     * Get all the reportDistributions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDistributionDTO> findAll() {
        log.debug("Request to get all ReportDistributions");
        return reportDistributionRepository
            .findAll()
            .stream()
            .map(reportDistributionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reportDistribution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportDistributionDTO> findOne(String id) {
        log.debug("Request to get ReportDistribution : {}", id);
        return reportDistributionRepository.findById(id).map(reportDistributionMapper::toDto);
    }

    /**
     * Delete the reportDistribution by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ReportDistribution : {}", id);
        reportDistributionRepository.deleteById(id);
    }
}
