package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportColumnMapping;
import de.adis_portal.server.reporting.repository.ReportColumnMappingRepository;
import de.adis_portal.server.reporting.service.dto.ReportColumnMappingDTO;
import de.adis_portal.server.reporting.service.mapper.ReportColumnMappingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportColumnMapping}.
 */
@Service
@Transactional
public class ReportColumnMappingService {

    private final Logger log = LoggerFactory.getLogger(ReportColumnMappingService.class);

    private final ReportColumnMappingRepository reportColumnMappingRepository;

    private final ReportColumnMappingMapper reportColumnMappingMapper;

    public ReportColumnMappingService(
        ReportColumnMappingRepository reportColumnMappingRepository,
        ReportColumnMappingMapper reportColumnMappingMapper
    ) {
        this.reportColumnMappingRepository = reportColumnMappingRepository;
        this.reportColumnMappingMapper = reportColumnMappingMapper;
    }

    /**
     * Save a reportColumnMapping.
     *
     * @param reportColumnMappingDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportColumnMappingDTO save(ReportColumnMappingDTO reportColumnMappingDTO) {
        log.debug("Request to save ReportColumnMapping : {}", reportColumnMappingDTO);
        ReportColumnMapping reportColumnMapping = reportColumnMappingMapper.toEntity(reportColumnMappingDTO);
        reportColumnMapping = reportColumnMappingRepository.save(reportColumnMapping);
        return reportColumnMappingMapper.toDto(reportColumnMapping);
    }

    /**
     * Update a reportColumnMapping.
     *
     * @param reportColumnMappingDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportColumnMappingDTO update(ReportColumnMappingDTO reportColumnMappingDTO) {
        log.debug("Request to update ReportColumnMapping : {}", reportColumnMappingDTO);
        ReportColumnMapping reportColumnMapping = reportColumnMappingMapper.toEntity(reportColumnMappingDTO);
        reportColumnMapping.setIsPersisted();
        reportColumnMapping = reportColumnMappingRepository.save(reportColumnMapping);
        return reportColumnMappingMapper.toDto(reportColumnMapping);
    }

    /**
     * Partially update a reportColumnMapping.
     *
     * @param reportColumnMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportColumnMappingDTO> partialUpdate(ReportColumnMappingDTO reportColumnMappingDTO) {
        log.debug("Request to partially update ReportColumnMapping : {}", reportColumnMappingDTO);

        return reportColumnMappingRepository
            .findById(reportColumnMappingDTO.getRid())
            .map(existingReportColumnMapping -> {
                reportColumnMappingMapper.partialUpdate(existingReportColumnMapping, reportColumnMappingDTO);

                return existingReportColumnMapping;
            })
            .map(reportColumnMappingRepository::save)
            .map(reportColumnMappingMapper::toDto);
    }

    /**
     * Get all the reportColumnMappings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportColumnMappingDTO> findAll() {
        log.debug("Request to get all ReportColumnMappings");
        return reportColumnMappingRepository
            .findAll()
            .stream()
            .map(reportColumnMappingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reportColumnMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportColumnMappingDTO> findOne(String id) {
        log.debug("Request to get ReportColumnMapping : {}", id);
        return reportColumnMappingRepository.findById(id).map(reportColumnMappingMapper::toDto);
    }

    /**
     * Delete the reportColumnMapping by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ReportColumnMapping : {}", id);
        reportColumnMappingRepository.deleteById(id);
    }
}
