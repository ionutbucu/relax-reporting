package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportParam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportParamRepository extends JpaRepository<ReportParam, Long> {}
