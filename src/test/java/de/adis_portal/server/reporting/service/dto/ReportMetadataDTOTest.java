package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportMetadataDTO.class);
        ReportMetadataDTO reportMetadataDTO1 = new ReportMetadataDTO();
        reportMetadataDTO1.setId(1L);
        ReportMetadataDTO reportMetadataDTO2 = new ReportMetadataDTO();
        assertThat(reportMetadataDTO1).isNotEqualTo(reportMetadataDTO2);
        reportMetadataDTO2.setId(reportMetadataDTO1.getId());
        assertThat(reportMetadataDTO1).isEqualTo(reportMetadataDTO2);
        reportMetadataDTO2.setId(2L);
        assertThat(reportMetadataDTO1).isNotEqualTo(reportMetadataDTO2);
        reportMetadataDTO1.setId(null);
        assertThat(reportMetadataDTO1).isNotEqualTo(reportMetadataDTO2);
    }
}
