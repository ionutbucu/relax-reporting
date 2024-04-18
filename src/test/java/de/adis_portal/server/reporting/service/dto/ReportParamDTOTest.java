package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportParamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportParamDTO.class);
        ReportParamDTO reportParamDTO1 = new ReportParamDTO();
        reportParamDTO1.setId(1L);
        ReportParamDTO reportParamDTO2 = new ReportParamDTO();
        assertThat(reportParamDTO1).isNotEqualTo(reportParamDTO2);
        reportParamDTO2.setId(reportParamDTO1.getId());
        assertThat(reportParamDTO1).isEqualTo(reportParamDTO2);
        reportParamDTO2.setId(2L);
        assertThat(reportParamDTO1).isNotEqualTo(reportParamDTO2);
        reportParamDTO1.setId(null);
        assertThat(reportParamDTO1).isNotEqualTo(reportParamDTO2);
    }
}
