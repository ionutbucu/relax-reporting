package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportMetadata;
import de.adis_portal.server.reporting.repository.ReportMetadataRepository;
import de.adis_portal.server.reporting.service.dto.ReportMetadataDTO;
import de.adis_portal.server.reporting.service.mapper.ReportMetadataMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportMetadata}.
 */
@Service
@Transactional
public class ReportMetadataService {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataService.class);

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataMapper reportMetadataMapper;

    public ReportMetadataService(ReportMetadataRepository reportMetadataRepository, ReportMetadataMapper reportMetadataMapper) {
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataMapper = reportMetadataMapper;
    }

    /**
     * Save a reportMetadata.
     *
     * @param reportMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportMetadataDTO save(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to save ReportMetadata : {}", reportMetadataDTO);
        ReportMetadata reportMetadata = reportMetadataMapper.toEntity(reportMetadataDTO);
        reportMetadata = reportMetadataRepository.save(reportMetadata);
        return reportMetadataMapper.toDto(reportMetadata);
    }

    /**
     * Update a reportMetadata.
     *
     * @param reportMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportMetadataDTO update(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to update ReportMetadata : {}", reportMetadataDTO);
        ReportMetadata reportMetadata = reportMetadataMapper.toEntity(reportMetadataDTO);
        reportMetadata.setIsPersisted();
        reportMetadata = reportMetadataRepository.save(reportMetadata);
        return reportMetadataMapper.toDto(reportMetadata);
    }

    /**
     * Partially update a reportMetadata.
     *
     * @param reportMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportMetadataDTO> partialUpdate(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to partially update ReportMetadata : {}", reportMetadataDTO);

        return reportMetadataRepository
            .findById(reportMetadataDTO.getRid())
            .map(existingReportMetadata -> {
                reportMetadataMapper.partialUpdate(existingReportMetadata, reportMetadataDTO);

                return existingReportMetadata;
            })
            .map(reportMetadataRepository::save)
            .map(reportMetadataMapper::toDto);
    }

    /**
     * Get all the reportMetadata.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportMetadataDTO> findAll() {
        log.debug("Request to get all ReportMetadata");
        return reportMetadataRepository
            .findAll()
            .stream()
            .map(reportMetadataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the reportMetadata where Report is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportMetadataDTO> findAllWhereReportIsNull() {
        log.debug("Request to get all reportMetadata where Report is null");
        return StreamSupport.stream(reportMetadataRepository.findAll().spliterator(), false)
            .filter(reportMetadata -> reportMetadata.getReport() == null)
            .map(reportMetadataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reportMetadata by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportMetadataDTO> findOne(String id) {
        log.debug("Request to get ReportMetadata : {}", id);
        return reportMetadataRepository.findById(id).map(reportMetadataMapper::toDto);
    }

    /**
     * Delete the reportMetadata by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ReportMetadata : {}", id);
        reportMetadataRepository.deleteById(id);
    }
}
