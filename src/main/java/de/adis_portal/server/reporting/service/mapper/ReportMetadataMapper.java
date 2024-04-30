package de.adis_portal.server.reporting.service.mapper;

import de.adis_portal.server.reporting.domain.ReportMetadata;
import de.adis_portal.server.reporting.service.dto.ReportMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportMetadata} and its DTO {@link ReportMetadataDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMetadataMapper extends EntityMapper<ReportMetadataDTO, ReportMetadata> {}
