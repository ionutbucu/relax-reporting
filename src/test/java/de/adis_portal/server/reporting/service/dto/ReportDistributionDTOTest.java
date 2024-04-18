package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDistributionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDistributionDTO.class);
        ReportDistributionDTO reportDistributionDTO1 = new ReportDistributionDTO();
        reportDistributionDTO1.setId(1L);
        ReportDistributionDTO reportDistributionDTO2 = new ReportDistributionDTO();
        assertThat(reportDistributionDTO1).isNotEqualTo(reportDistributionDTO2);
        reportDistributionDTO2.setId(reportDistributionDTO1.getId());
        assertThat(reportDistributionDTO1).isEqualTo(reportDistributionDTO2);
        reportDistributionDTO2.setId(2L);
        assertThat(reportDistributionDTO1).isNotEqualTo(reportDistributionDTO2);
        reportDistributionDTO1.setId(null);
        assertThat(reportDistributionDTO1).isNotEqualTo(reportDistributionDTO2);
    }
}
