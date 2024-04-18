package de.adis_portal.server.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportMetadata.
 */
@Entity
@Table(name = "report_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @JsonIgnoreProperties(
        value = { "datasource", "metadata", "schedules", "distributions", "executions", "parameters", "columns" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "metadata")
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public ReportMetadata metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        if (this.report != null) {
            this.report.setMetadata(null);
        }
        if (report != null) {
            report.setMetadata(this);
        }
        this.report = report;
    }

    public ReportMetadata report(Report report) {
        this.setReport(report);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportMetadata)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportMetadata) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportMetadata{" +
            "id=" + getId() +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
