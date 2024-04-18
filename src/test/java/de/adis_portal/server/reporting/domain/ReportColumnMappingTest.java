package de.adis_portal.server.reporting.domain;

import static de.adis_portal.server.reporting.domain.ReportColumnMappingTestSamples.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportColumnMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportColumnMapping.class);
        ReportColumnMapping reportColumnMapping1 = getReportColumnMappingSample1();
        ReportColumnMapping reportColumnMapping2 = new ReportColumnMapping();
        assertThat(reportColumnMapping1).isNotEqualTo(reportColumnMapping2);

        reportColumnMapping2.setId(reportColumnMapping1.getId());
        assertThat(reportColumnMapping1).isEqualTo(reportColumnMapping2);

        reportColumnMapping2 = getReportColumnMappingSample2();
        assertThat(reportColumnMapping1).isNotEqualTo(reportColumnMapping2);
    }

    @Test
    void reportTest() throws Exception {
        ReportColumnMapping reportColumnMapping = getReportColumnMappingRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportColumnMapping.setReport(reportBack);
        assertThat(reportColumnMapping.getReport()).isEqualTo(reportBack);

        reportColumnMapping.report(null);
        assertThat(reportColumnMapping.getReport()).isNull();
    }
}
