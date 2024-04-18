package de.adis_portal.server.reporting.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportColumnMapping} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportColumnMappingDTO implements Serializable {

    private Long id;

    private String sourceColumnName;

    private Integer sourceColumnIndex;

    private String columnTitle;

    private String lang;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceColumnName() {
        return sourceColumnName;
    }

    public void setSourceColumnName(String sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
    }

    public Integer getSourceColumnIndex() {
        return sourceColumnIndex;
    }

    public void setSourceColumnIndex(Integer sourceColumnIndex) {
        this.sourceColumnIndex = sourceColumnIndex;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
        if (!(o instanceof ReportColumnMappingDTO)) {
            return false;
        }

        ReportColumnMappingDTO reportColumnMappingDTO = (ReportColumnMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportColumnMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportColumnMappingDTO{" +
            "id=" + getId() +
            ", sourceColumnName='" + getSourceColumnName() + "'" +
            ", sourceColumnIndex=" + getSourceColumnIndex() +
            ", columnTitle='" + getColumnTitle() + "'" +
            ", lang='" + getLang() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
