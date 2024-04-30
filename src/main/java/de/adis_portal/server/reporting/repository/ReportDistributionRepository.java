package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportDistribution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportDistribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportDistributionRepository extends JpaRepository<ReportDistribution, String> {}
