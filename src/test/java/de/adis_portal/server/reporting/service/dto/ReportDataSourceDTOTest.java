package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDataSourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDataSourceDTO.class);
        ReportDataSourceDTO reportDataSourceDTO1 = new ReportDataSourceDTO();
        reportDataSourceDTO1.setId(1L);
        ReportDataSourceDTO reportDataSourceDTO2 = new ReportDataSourceDTO();
        assertThat(reportDataSourceDTO1).isNotEqualTo(reportDataSourceDTO2);
        reportDataSourceDTO2.setId(reportDataSourceDTO1.getId());
        assertThat(reportDataSourceDTO1).isEqualTo(reportDataSourceDTO2);
        reportDataSourceDTO2.setId(2L);
        assertThat(reportDataSourceDTO1).isNotEqualTo(reportDataSourceDTO2);
        reportDataSourceDTO1.setId(null);
        assertThat(reportDataSourceDTO1).isNotEqualTo(reportDataSourceDTO2);
    }
}
