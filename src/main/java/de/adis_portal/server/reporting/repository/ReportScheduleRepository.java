package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportSchedule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportScheduleRepository extends JpaRepository<ReportSchedule, String> {}
