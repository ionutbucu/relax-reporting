package de.adis_portal.server.reporting.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportExecution} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportExecutionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @Size(max = 256)
    private String error;

    private String url;

    private String user;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
        if (!(o instanceof ReportExecutionDTO)) {
            return false;
        }

        ReportExecutionDTO reportExecutionDTO = (ReportExecutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportExecutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportExecutionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", error='" + getError() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
