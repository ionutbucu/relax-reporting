package de.adis_portal.server.reporting.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportParam} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportParamDTO implements Serializable {

    private Long id;

    private String name;

    private String type;

    private String value;

    private String conversionRule;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getConversionRule() {
        return conversionRule;
    }

    public void setConversionRule(String conversionRule) {
        this.conversionRule = conversionRule;
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
        if (!(o instanceof ReportParamDTO)) {
            return false;
        }

        ReportParamDTO reportParamDTO = (ReportParamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportParamDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportParamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            ", conversionRule='" + getConversionRule() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
