package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.Report;
import de.adis_portal.server.reporting.domain.ReportSchedule;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
import de.adis_portal.server.reporting.service.dto.ReportScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportSchedule} and its DTO {@link ReportScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportScheduleMapper extends EntityMapper<ReportScheduleDTO, ReportSchedule> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    ReportScheduleDTO toDto(ReportSchedule s);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
