package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.Report;
import de.adis_portal.server.reporting.domain.ReportDataSource;
import de.adis_portal.server.reporting.domain.ReportMetadata;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
import de.adis_portal.server.reporting.service.dto.ReportDataSourceDTO;
import de.adis_portal.server.reporting.service.dto.ReportMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "datasource", source = "datasource", qualifiedByName = "reportDataSourceId")
    @Mapping(target = "metadata", source = "metadata", qualifiedByName = "reportMetadataId")
    ReportDTO toDto(Report s);

    @Named("reportDataSourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDataSourceDTO toDtoReportDataSourceId(ReportDataSource reportDataSource);

    @Named("reportMetadataId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportMetadataDTO toDtoReportMetadataId(ReportMetadata reportMetadata);
}
