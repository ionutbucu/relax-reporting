package de.adis_portal.server.reporting.service.mapper;

import static de.adis_portal.server.reporting.domain.ReportScheduleAsserts.*;
import static de.adis_portal.server.reporting.domain.ReportScheduleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportScheduleMapperTest {

    private ReportScheduleMapper reportScheduleMapper;

    @BeforeEach
    void setUp() {
        reportScheduleMapper = new ReportScheduleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportScheduleSample1();
        var actual = reportScheduleMapper.toEntity(reportScheduleMapper.toDto(expected));
        assertReportScheduleAllPropertiesEquals(expected, actual);
    }
}
