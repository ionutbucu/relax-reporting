package de.adis_portal.server.reporting.domain;

import static de.adis_portal.server.reporting.domain.ReportScheduleTestSamples.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportSchedule.class);
        ReportSchedule reportSchedule1 = getReportScheduleSample1();
        ReportSchedule reportSchedule2 = new ReportSchedule();
        assertThat(reportSchedule1).isNotEqualTo(reportSchedule2);

        reportSchedule2.setId(reportSchedule1.getId());
        assertThat(reportSchedule1).isEqualTo(reportSchedule2);

        reportSchedule2 = getReportScheduleSample2();
        assertThat(reportSchedule1).isNotEqualTo(reportSchedule2);
    }

    @Test
    void reportTest() throws Exception {
        ReportSchedule reportSchedule = getReportScheduleRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportSchedule.setReport(reportBack);
        assertThat(reportSchedule.getReport()).isEqualTo(reportBack);

        reportSchedule.report(null);
        assertThat(reportSchedule.getReport()).isNull();
    }
}
