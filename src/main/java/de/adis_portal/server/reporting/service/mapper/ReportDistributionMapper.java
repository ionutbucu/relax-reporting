package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.Report;
import de.adis_portal.server.reporting.domain.ReportDistribution;
import de.adis_portal.server.reporting.service.dto.ReportDTO;
import de.adis_portal.server.reporting.service.dto.ReportDistributionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDistribution} and its DTO {@link ReportDistributionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportDistributionMapper extends EntityMapper<ReportDistributionDTO, ReportDistribution> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportRid")
    ReportDistributionDTO toDto(ReportDistribution s);

    @Named("reportRid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rid", source = "rid")
    ReportDTO toDtoReportRid(Report report);
}
