package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDataSourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDataSourceDTO.class);
        ReportDataSourceDTO reportDataSourceDTO1 = new ReportDataSourceDTO();
        reportDataSourceDTO1.setRid("id1");
        ReportDataSourceDTO reportDataSourceDTO2 = new ReportDataSourceDTO();
        assertThat(reportDataSourceDTO1).isNotEqualTo(reportDataSourceDTO2);
        reportDataSourceDTO2.setRid(reportDataSourceDTO1.getRid());
        assertThat(reportDataSourceDTO1).isEqualTo(reportDataSourceDTO2);
        reportDataSourceDTO2.setRid("id2");
        assertThat(reportDataSourceDTO1).isNotEqualTo(reportDataSourceDTO2);
        reportDataSourceDTO1.setRid(null);
        assertThat(reportDataSourceDTO1).isNotEqualTo(reportDataSourceDTO2);
    }
}
