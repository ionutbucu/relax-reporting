package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportColumnMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportColumnMappingDTO.class);
        ReportColumnMappingDTO reportColumnMappingDTO1 = new ReportColumnMappingDTO();
        reportColumnMappingDTO1.setId(1L);
        ReportColumnMappingDTO reportColumnMappingDTO2 = new ReportColumnMappingDTO();
        assertThat(reportColumnMappingDTO1).isNotEqualTo(reportColumnMappingDTO2);
        reportColumnMappingDTO2.setId(reportColumnMappingDTO1.getId());
        assertThat(reportColumnMappingDTO1).isEqualTo(reportColumnMappingDTO2);
        reportColumnMappingDTO2.setId(2L);
        assertThat(reportColumnMappingDTO1).isNotEqualTo(reportColumnMappingDTO2);
        reportColumnMappingDTO1.setId(null);
        assertThat(reportColumnMappingDTO1).isNotEqualTo(reportColumnMappingDTO2);
    }
}
