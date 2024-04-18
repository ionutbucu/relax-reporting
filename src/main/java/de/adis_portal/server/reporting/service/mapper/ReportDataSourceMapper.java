package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.ReportDataSource;
import de.adis_portal.server.reporting.service.dto.ReportDataSourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDataSource} and its DTO {@link ReportDataSourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportDataSourceMapper extends EntityMapper<ReportDataSourceDTO, ReportDataSource> {}
