package de.adis_portal.server.reporting.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportDistribution} entity.
 */
@Schema(description = "not an ignored comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDistributionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    @Pattern(regexp = "^[^@]+@[^@]+$")
    private String email;

    @Size(max = 256)
    private String description;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDistributionDTO)) {
            return false;
        }

        ReportDistributionDTO reportDistributionDTO = (ReportDistributionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDistributionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDistributionDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", description='" + getDescription() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
