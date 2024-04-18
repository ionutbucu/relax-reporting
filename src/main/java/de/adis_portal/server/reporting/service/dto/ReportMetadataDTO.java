package de.adis_portal.server.reporting.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportMetadata} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportMetadataDTO implements Serializable {

    private Long id;

    @Lob
    private String metadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportMetadataDTO)) {
            return false;
        }

        ReportMetadataDTO reportMetadataDTO = (ReportMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportMetadataDTO{" +
            "id=" + getId() +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
