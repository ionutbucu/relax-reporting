package de.adis_portal.server.reporting.domain;

import static de.adis_portal.server.reporting.domain.ReportDataSourceTestSamples.*;
import static de.adis_portal.server.reporting.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDataSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDataSource.class);
        ReportDataSource reportDataSource1 = getReportDataSourceSample1();
        ReportDataSource reportDataSource2 = new ReportDataSource();
        assertThat(reportDataSource1).isNotEqualTo(reportDataSource2);

        reportDataSource2.setRid(reportDataSource1.getRid());
        assertThat(reportDataSource1).isEqualTo(reportDataSource2);

        reportDataSource2 = getReportDataSourceSample2();
        assertThat(reportDataSource1).isNotEqualTo(reportDataSource2);
    }

    @Test
    void reportTest() throws Exception {
        ReportDataSource reportDataSource = getReportDataSourceRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportDataSource.setReport(reportBack);
        assertThat(reportDataSource.getReport()).isEqualTo(reportBack);
        assertThat(reportBack.getDatasource()).isEqualTo(reportDataSource);

        reportDataSource.report(null);
        assertThat(reportDataSource.getReport()).isNull();
        assertThat(reportBack.getDatasource()).isNull();
    }
}
