package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportMetadata;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportMetadataRepository extends JpaRepository<ReportMetadata, Long> {}
