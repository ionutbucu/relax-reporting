package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.Report;
import de.adis_portal.server.reporting.domain.ReportColumnMapping;
import de.adis_portal.server.reporting.service.dto.ReportColumnMappingDTO;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportColumnMapping} and its DTO {@link ReportColumnMappingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportColumnMappingMapper extends EntityMapper<ReportColumnMappingDTO, ReportColumnMapping> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    ReportColumnMappingDTO toDto(ReportColumnMapping s);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
