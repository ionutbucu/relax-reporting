package de.adis_portal.server.reporting.service;

import de.adis_portal.server.reporting.domain.ReportDataSource;
import de.adis_portal.server.reporting.repository.ReportDataSourceRepository;
import de.adis_portal.server.reporting.service.dto.ReportDataSourceDTO;
import de.adis_portal.server.reporting.service.mapper.ReportDataSourceMapper;
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
 * Service Implementation for managing {@link de.adis_portal.server.reporting.domain.ReportDataSource}.
 */
@Service
@Transactional
public class ReportDataSourceService {

    private final Logger log = LoggerFactory.getLogger(ReportDataSourceService.class);

    private final ReportDataSourceRepository reportDataSourceRepository;

    private final ReportDataSourceMapper reportDataSourceMapper;

    public ReportDataSourceService(ReportDataSourceRepository reportDataSourceRepository, ReportDataSourceMapper reportDataSourceMapper) {
        this.reportDataSourceRepository = reportDataSourceRepository;
        this.reportDataSourceMapper = reportDataSourceMapper;
    }

    /**
     * Save a reportDataSource.
     *
     * @param reportDataSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDataSourceDTO save(ReportDataSourceDTO reportDataSourceDTO) {
        log.debug("Request to save ReportDataSource : {}", reportDataSourceDTO);
        ReportDataSource reportDataSource = reportDataSourceMapper.toEntity(reportDataSourceDTO);
        reportDataSource = reportDataSourceRepository.save(reportDataSource);
        return reportDataSourceMapper.toDto(reportDataSource);
    }

    /**
     * Update a reportDataSource.
     *
     * @param reportDataSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDataSourceDTO update(ReportDataSourceDTO reportDataSourceDTO) {
        log.debug("Request to update ReportDataSource : {}", reportDataSourceDTO);
        ReportDataSource reportDataSource = reportDataSourceMapper.toEntity(reportDataSourceDTO);
        reportDataSource = reportDataSourceRepository.save(reportDataSource);
        return reportDataSourceMapper.toDto(reportDataSource);
    }

    /**
     * Partially update a reportDataSource.
     *
     * @param reportDataSourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportDataSourceDTO> partialUpdate(ReportDataSourceDTO reportDataSourceDTO) {
        log.debug("Request to partially update ReportDataSource : {}", reportDataSourceDTO);

        return reportDataSourceRepository
            .findById(reportDataSourceDTO.getId())
            .map(existingReportDataSource -> {
                reportDataSourceMapper.partialUpdate(existingReportDataSource, reportDataSourceDTO);

                return existingReportDataSource;
            })
            .map(reportDataSourceRepository::save)
            .map(reportDataSourceMapper::toDto);
    }

    /**
     * Get all the reportDataSources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDataSourceDTO> findAll() {
        log.debug("Request to get all ReportDataSources");
        return reportDataSourceRepository
            .findAll()
            .stream()
            .map(reportDataSourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the reportDataSources where Report is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDataSourceDTO> findAllWhereReportIsNull() {
        log.debug("Request to get all reportDataSources where Report is null");
        return StreamSupport.stream(reportDataSourceRepository.findAll().spliterator(), false)
            .filter(reportDataSource -> reportDataSource.getReport() == null)
            .map(reportDataSourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reportDataSource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportDataSourceDTO> findOne(Long id) {
        log.debug("Request to get ReportDataSource : {}", id);
        return reportDataSourceRepository.findById(id).map(reportDataSourceMapper::toDto);
    }

    /**
     * Delete the reportDataSource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReportDataSource : {}", id);
        reportDataSourceRepository.deleteById(id);
    }
}
