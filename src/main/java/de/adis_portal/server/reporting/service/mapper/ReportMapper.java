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
    @Mapping(target = "datasource", source = "datasource", qualifiedByName = "reportDataSourceRid")
    @Mapping(target = "metadata", source = "metadata", qualifiedByName = "reportMetadataRid")
    ReportDTO toDto(Report s);

    @Named("reportDataSourceRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDataSourceDTO toDtoReportDataSourceRid(ReportDataSource reportDataSource);

    @Named("reportMetadataRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportMetadataDTO toDtoReportMetadataRid(ReportMetadata reportMetadata);
}
