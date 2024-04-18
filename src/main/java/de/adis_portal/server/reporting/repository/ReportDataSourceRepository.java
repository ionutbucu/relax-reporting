package de.adis_portal.server.reporting.repository;

import de.adis_portal.server.reporting.domain.ReportDataSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportDataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportDataSourceRepository extends JpaRepository<ReportDataSource, Long> {}
