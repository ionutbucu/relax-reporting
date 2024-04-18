package de.adis_portal.server.reporting.service.dto;

import de.adis_portal.server.reporting.domain.enumeration.QueryType;
import de.adis_portal.server.reporting.domain.enumeration.ReportType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.Report} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    @Size(max = 512)
    private String description;

    @NotNull
    @Size(min = 10, max = 10000)
    private String query;

    private QueryType queryType;

    @NotNull
    @Size(min = 3)
    private String fileName;

    private ReportType reportType;

    private ReportDataSourceDTO datasource;

    private ReportMetadataDTO metadata;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportDataSourceDTO getDatasource() {
        return datasource;
    }

    public void setDatasource(ReportDataSourceDTO datasource) {
        this.datasource = datasource;
    }

    public ReportMetadataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportMetadataDTO metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDTO)) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", query='" + getQuery() + "'" +
            ", queryType='" + getQueryType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", reportType='" + getReportType() + "'" +
            ", datasource=" + getDatasource() +
            ", metadata=" + getMetadata() +
            "}";
    }
}
