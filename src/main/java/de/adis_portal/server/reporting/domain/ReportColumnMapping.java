package de.adis_portal.server.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A ReportColumnMapping.
 */
@Entity
@Table(name = "report_column_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportColumnMapping implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "rid")
    private String rid;

    @Column(name = "source_column_name")
    private String sourceColumnName;

    @Column(name = "source_column_index")
    private Integer sourceColumnIndex;

    @Column(name = "column_title")
    private String columnTitle;

    @Column(name = "lang")
    private String lang;

    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "datasource", "metadata", "schedules", "distributions", "executions", "parameters", "columns" },
        allowSetters = true
    )
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getRid() {
        return this.rid;
    }

    public ReportColumnMapping rid(String rid) {
        this.setRid(rid);
        return this;
    }

    public void setRid(String rid) {
        this.rid = rid;
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

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Override
    public String getId() {
        return this.rid;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ReportColumnMapping setIsPersisted() {
        this.isPersisted = true;
        return this;
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
        return getRid() != null && getRid().equals(((ReportColumnMapping) o).getRid());
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
            "rid=" + getRid() +
            ", sourceColumnName='" + getSourceColumnName() + "'" +
            ", sourceColumnIndex=" + getSourceColumnIndex() +
            ", columnTitle='" + getColumnTitle() + "'" +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
