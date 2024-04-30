package de.adis_portal.server.reporting.service.mapper;

import static de.adis_portal.server.reporting.domain.ReportExecutionAsserts.*;
import static de.adis_portal.server.reporting.domain.ReportExecutionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportExecutionMapperTest {

    private ReportExecutionMapper reportExecutionMapper;

    @BeforeEach
    void setUp() {
        reportExecutionMapper = new ReportExecutionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportExecutionSample1();
        var actual = reportExecutionMapper.toEntity(reportExecutionMapper.toDto(expected));
        assertReportExecutionAllPropertiesEquals(expected, actual);
    }
}
