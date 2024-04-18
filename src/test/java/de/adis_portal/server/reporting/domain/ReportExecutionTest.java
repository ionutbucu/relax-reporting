package de.adis_portal.server.reporting.domain;

import static de.adis_portal.server.reporting.domain.ReportExecutionTestSamples.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportExecutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportExecution.class);
        ReportExecution reportExecution1 = getReportExecutionSample1();
        ReportExecution reportExecution2 = new ReportExecution();
        assertThat(reportExecution1).isNotEqualTo(reportExecution2);

        reportExecution2.setId(reportExecution1.getId());
        assertThat(reportExecution1).isEqualTo(reportExecution2);

        reportExecution2 = getReportExecutionSample2();
        assertThat(reportExecution1).isNotEqualTo(reportExecution2);
    }

    @Test
    void reportTest() throws Exception {
        ReportExecution reportExecution = getReportExecutionRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportExecution.setReport(reportBack);
        assertThat(reportExecution.getReport()).isEqualTo(reportBack);

        reportExecution.report(null);
        assertThat(reportExecution.getReport()).isNull();
    }
}
