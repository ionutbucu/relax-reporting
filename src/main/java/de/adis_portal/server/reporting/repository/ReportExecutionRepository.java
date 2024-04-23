package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportExecutionRepository extends JpaRepository<ReportExecution, String> {}
