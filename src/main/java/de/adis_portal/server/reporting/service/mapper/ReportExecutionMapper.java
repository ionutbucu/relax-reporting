package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.Report;
import de.adis_portal.server.reporting.domain.ReportExecution;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
import de.adis_portal.server.reporting.service.dto.ReportExecutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportExecution} and its DTO {@link ReportExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportExecutionMapper extends EntityMapper<ReportExecutionDTO, ReportExecution> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    ReportExecutionDTO toDto(ReportExecution s);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
