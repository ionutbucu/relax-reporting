package de.adis_portal.server.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportColumnMapping.
 */
@Entity
@Table(name = "report_column_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportColumnMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "source_column_name")
    private String sourceColumnName;

    @Column(name = "source_column_index")
    private Integer sourceColumnIndex;

    @Column(name = "column_title")
    private String columnTitle;

    @Column(name = "lang")
    private String lang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "datasource", "metadata", "schedules", "distributions", "executions", "parameters", "columns" },
        allowSetters = true
    )
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportColumnMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceColumnName() {
        return this.sourceColumnName;
    }

    public ReportColumnMapping sourceColumnName(String sourceColumnName) {
        this.setSourceColumnName(sourceColumnName);
        return this;
    }

    public void setSourceColumnName(String sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
    }

    public Integer getSourceColumnIndex() {
        return this.sourceColumnIndex;
    }

    public ReportColumnMapping sourceColumnIndex(Integer sourceColumnIndex) {
        this.setSourceColumnIndex(sourceColumnIndex);
        return this;
    }

    public void setSourceColumnIndex(Integer sourceColumnIndex) {
        this.sourceColumnIndex = sourceColumnIndex;
    }

    public String getColumnTitle() {
        return this.columnTitle;
    }

    public ReportColumnMapping columnTitle(String columnTitle) {
        this.setColumnTitle(columnTitle);
        return this;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getLang() {
        return this.lang;
    }

    public ReportColumnMapping lang(String lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ReportColumnMapping report(Report report) {
        this.setReport(report);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportColumnMapping)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportColumnMapping) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportColumnMapping{" +
            "id=" + getId() +
            ", sourceColumnName='" + getSourceColumnName() + "'" +
            ", sourceColumnIndex=" + getSourceColumnIndex() +
            ", columnTitle='" + getColumnTitle() + "'" +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
