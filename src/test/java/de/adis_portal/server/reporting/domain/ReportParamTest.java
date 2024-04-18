package de.adis_portal.server.reporting.domain;

import static de.adis_portal.server.reporting.domain.ReportParamTestSamples.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportParamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportParam.class);
        ReportParam reportParam1 = getReportParamSample1();
        ReportParam reportParam2 = new ReportParam();
        assertThat(reportParam1).isNotEqualTo(reportParam2);

        reportParam2.setId(reportParam1.getId());
        assertThat(reportParam1).isEqualTo(reportParam2);

        reportParam2 = getReportParamSample2();
        assertThat(reportParam1).isNotEqualTo(reportParam2);
    }

    @Test
    void reportTest() throws Exception {
        ReportParam reportParam = getReportParamRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportParam.setReport(reportBack);
        assertThat(reportParam.getReport()).isEqualTo(reportBack);

        reportParam.report(null);
        assertThat(reportParam.getReport()).isNull();
    }
}
