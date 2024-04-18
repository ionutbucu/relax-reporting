package de.adis_portal.server.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.adis_portal.server.reporting.domain.enumeration.QueryType;
import de.adis_portal.server.reporting.domain.enumeration.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Size(min = 10, max = 10000)
    @Column(name = "query", length = 10000, nullable = false)
    private String query;

    @Enumerated(EnumType.STRING)
    @Column(name = "query_type")
    private QueryType queryType;

    @NotNull
    @Size(min = 3)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ReportDataSource datasource;

    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ReportMetadata metadata;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportSchedule> schedules = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportDistribution> distributions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportExecution> executions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportParam> parameters = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportColumnMapping> columns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Report name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Report description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return this.query;
    }

    public Report query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryType getQueryType() {
        return this.queryType;
    }

    public Report queryType(QueryType queryType) {
        this.setQueryType(queryType);
        return this;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Report fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public Report reportType(ReportType reportType) {
        this.setReportType(reportType);
        return this;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportDataSource getDatasource() {
        return this.datasource;
    }

    public void setDatasource(ReportDataSource reportDataSource) {
        this.datasource = reportDataSource;
    }

    public Report datasource(ReportDataSource reportDataSource) {
        this.setDatasource(reportDataSource);
        return this;
    }

    public ReportMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ReportMetadata reportMetadata) {
        this.metadata = reportMetadata;
    }

    public Report metadata(ReportMetadata reportMetadata) {
        this.setMetadata(reportMetadata);
        return this;
    }

    public Set<ReportSchedule> getSchedules() {
        return this.schedules;
    }

    public void setSchedules(Set<ReportSchedule> reportSchedules) {
        if (this.schedules != null) {
            this.schedules.forEach(i -> i.setReport(null));
        }
        if (reportSchedules != null) {
            reportSchedules.forEach(i -> i.setReport(this));
        }
        this.schedules = reportSchedules;
    }

    public Report schedules(Set<ReportSchedule> reportSchedules) {
        this.setSchedules(reportSchedules);
        return this;
    }

    public Report addSchedules(ReportSchedule reportSchedule) {
        this.schedules.add(reportSchedule);
        reportSchedule.setReport(this);
        return this;
    }

    public Report removeSchedules(ReportSchedule reportSchedule) {
        this.schedules.remove(reportSchedule);
        reportSchedule.setReport(null);
        return this;
    }

    public Set<ReportDistribution> getDistributions() {
        return this.distributions;
    }

    public void setDistributions(Set<ReportDistribution> reportDistributions) {
        if (this.distributions != null) {
            this.distributions.forEach(i -> i.setReport(null));
        }
        if (reportDistributions != null) {
            reportDistributions.forEach(i -> i.setReport(this));
        }
        this.distributions = reportDistributions;
    }

    public Report distributions(Set<ReportDistribution> reportDistributions) {
        this.setDistributions(reportDistributions);
        return this;
    }

    public Report addDistributions(ReportDistribution reportDistribution) {
        this.distributions.add(reportDistribution);
        reportDistribution.setReport(this);
        return this;
    }

    public Report removeDistributions(ReportDistribution reportDistribution) {
        this.distributions.remove(reportDistribution);
        reportDistribution.setReport(null);
        return this;
    }

    public Set<ReportExecution> getExecutions() {
        return this.executions;
    }

    public void setExecutions(Set<ReportExecution> reportExecutions) {
        if (this.executions != null) {
            this.executions.forEach(i -> i.setReport(null));
        }
        if (reportExecutions != null) {
            reportExecutions.forEach(i -> i.setReport(this));
        }
        this.executions = reportExecutions;
    }

    public Report executions(Set<ReportExecution> reportExecutions) {
        this.setExecutions(reportExecutions);
        return this;
    }

    public Report addExecutions(ReportExecution reportExecution) {
        this.executions.add(reportExecution);
        reportExecution.setReport(this);
        return this;
    }

    public Report removeExecutions(ReportExecution reportExecution) {
        this.executions.remove(reportExecution);
        reportExecution.setReport(null);
        return this;
    }

    public Set<ReportParam> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<ReportParam> reportParams) {
        if (this.parameters != null) {
            this.parameters.forEach(i -> i.setReport(null));
        }
        if (reportParams != null) {
            reportParams.forEach(i -> i.setReport(this));
        }
        this.parameters = reportParams;
    }

    public Report parameters(Set<ReportParam> reportParams) {
        this.setParameters(reportParams);
        return this;
    }

    public Report addParameters(ReportParam reportParam) {
        this.parameters.add(reportParam);
        reportParam.setReport(this);
        return this;
    }

    public Report removeParameters(ReportParam reportParam) {
        this.parameters.remove(reportParam);
        reportParam.setReport(null);
        return this;
    }

    public Set<ReportColumnMapping> getColumns() {
        return this.columns;
    }

    public void setColumns(Set<ReportColumnMapping> reportColumnMappings) {
        if (this.columns != null) {
            this.columns.forEach(i -> i.setReport(null));
        }
        if (reportColumnMappings != null) {
            reportColumnMappings.forEach(i -> i.setReport(this));
        }
        this.columns = reportColumnMappings;
    }

    public Report columns(Set<ReportColumnMapping> reportColumnMappings) {
        this.setColumns(reportColumnMappings);
        return this;
    }

    public Report addColumns(ReportColumnMapping reportColumnMapping) {
        this.columns.add(reportColumnMapping);
        reportColumnMapping.setReport(this);
        return this;
    }

    public Report removeColumns(ReportColumnMapping reportColumnMapping) {
        this.columns.remove(reportColumnMapping);
        reportColumnMapping.setReport(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return getId() != null && getId().equals(((Report) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", query='" + getQuery() + "'" +
            ", queryType='" + getQueryType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", reportType='" + getReportType() + "'" +
            "}";
    }
}
