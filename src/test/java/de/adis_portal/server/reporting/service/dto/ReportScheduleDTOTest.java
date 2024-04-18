package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportScheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportScheduleDTO.class);
        ReportScheduleDTO reportScheduleDTO1 = new ReportScheduleDTO();
        reportScheduleDTO1.setId(1L);
        ReportScheduleDTO reportScheduleDTO2 = new ReportScheduleDTO();
        assertThat(reportScheduleDTO1).isNotEqualTo(reportScheduleDTO2);
        reportScheduleDTO2.setId(reportScheduleDTO1.getId());
        assertThat(reportScheduleDTO1).isEqualTo(reportScheduleDTO2);
        reportScheduleDTO2.setId(2L);
        assertThat(reportScheduleDTO1).isNotEqualTo(reportScheduleDTO2);
        reportScheduleDTO1.setId(null);
        assertThat(reportScheduleDTO1).isNotEqualTo(reportScheduleDTO2);
    }
}
