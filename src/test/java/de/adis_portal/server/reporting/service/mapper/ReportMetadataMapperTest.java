package de.adis_portal.server.reporting.service.mapper;

import static de.adis_portal.server.reporting.domain.ReportMetadataAsserts.*;
import static de.adis_portal.server.reporting.domain.ReportMetadataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportMetadataMapperTest {

    private ReportMetadataMapper reportMetadataMapper;

    @BeforeEach
    void setUp() {
        reportMetadataMapper = new ReportMetadataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportMetadataSample1();
        var actual = reportMetadataMapper.toEntity(reportMetadataMapper.toDto(expected));
        assertReportMetadataAllPropertiesEquals(expected, actual);
    }
}
