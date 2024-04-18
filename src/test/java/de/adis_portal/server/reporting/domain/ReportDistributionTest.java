package de.adis_portal.server.reporting.domain;

import static de.adis_portal.server.reporting.domain.ReportDistributionTestSamples.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDistributionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDistribution.class);
        ReportDistribution reportDistribution1 = getReportDistributionSample1();
        ReportDistribution reportDistribution2 = new ReportDistribution();
        assertThat(reportDistribution1).isNotEqualTo(reportDistribution2);

        reportDistribution2.setId(reportDistribution1.getId());
        assertThat(reportDistribution1).isEqualTo(reportDistribution2);

        reportDistribution2 = getReportDistributionSample2();
        assertThat(reportDistribution1).isNotEqualTo(reportDistribution2);
    }

    @Test
    void reportTest() throws Exception {
        ReportDistribution reportDistribution = getReportDistributionRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportDistribution.setReport(reportBack);
        assertThat(reportDistribution.getReport()).isEqualTo(reportBack);

        reportDistribution.report(null);
        assertThat(reportDistribution.getReport()).isNull();
    }
}
