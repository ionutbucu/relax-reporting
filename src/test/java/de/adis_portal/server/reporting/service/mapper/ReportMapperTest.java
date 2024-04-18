package de.adis_portal.server.reporting.service.mapper;

import static de.adis_portal.server.reporting.domain.ReportAsserts.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportMapperTest {

    private ReportMapper reportMapper;

    @BeforeEach
    void setUp() {
        reportMapper = new ReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportSample1();
        var actual = reportMapper.toEntity(reportMapper.toDto(expected));
        assertReportAllPropertiesEquals(expected, actual);
    }
}
