package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.Report;
import de.adis_portal.server.reporting.domain.ReportParam;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
import de.adis_portal.server.reporting.service.dto.ReportParamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportParam} and its DTO {@link ReportParamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportParamMapper extends EntityMapper<ReportParamDTO, ReportParam> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportParamDTO toDto(ReportParam s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
