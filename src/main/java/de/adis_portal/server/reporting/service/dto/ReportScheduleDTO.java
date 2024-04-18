package de.adis_portal.server.reporting.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportSchedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String cron;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
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
        if (!(o instanceof ReportScheduleDTO)) {
            return false;
        }

        ReportScheduleDTO reportScheduleDTO = (ReportScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportScheduleDTO{" +
            "id=" + getId() +
            ", cron='" + getCron() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
