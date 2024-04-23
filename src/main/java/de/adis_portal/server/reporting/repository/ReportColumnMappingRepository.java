package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportColumnMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportColumnMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportColumnMappingRepository extends JpaRepository<ReportColumnMapping, String> {}
