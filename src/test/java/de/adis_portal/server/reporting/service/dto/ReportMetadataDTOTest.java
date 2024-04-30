package de.adis_portal.server.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.adis_portal.server.reporting.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportMetadataDTO.class);
        ReportMetadataDTO reportMetadataDTO1 = new ReportMetadataDTO();
        reportMetadataDTO1.setRid("id1");
        ReportMetadataDTO reportMetadataDTO2 = new ReportMetadataDTO();
        assertThat(reportMetadataDTO1).isNotEqualTo(reportMetadataDTO2);
        reportMetadataDTO2.setRid(reportMetadataDTO1.getRid());
        assertThat(reportMetadataDTO1).isEqualTo(reportMetadataDTO2);
        reportMetadataDTO2.setRid("id2");
        assertThat(reportMetadataDTO1).isNotEqualTo(reportMetadataDTO2);
        reportMetadataDTO1.setRid(null);
        assertThat(reportMetadataDTO1).isNotEqualTo(reportMetadataDTO2);
    }
}
