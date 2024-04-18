package de.adis_portal.server.reporting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportDataSource.
 */
@Entity
@Table(name = "report_data_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "url")
    private String url;

    @Column(name = "user")
    private String user;

    @Column(name = "password")
    private String password;

    @JsonIgnoreProperties(
        value = { "datasource", "metadata", "schedules", "distributions", "executions", "parameters", "columns" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "datasource")
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportDataSource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public ReportDataSource type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public ReportDataSource url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.user;
    }

    public ReportDataSource user(String user) {
        this.setUser(user);
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public ReportDataSource password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        if (this.report != null) {
            this.report.setDatasource(null);
        }
        if (report != null) {
            report.setDatasource(this);
        }
        this.report = report;
    }

    public ReportDataSource report(Report report) {
        this.setReport(report);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDataSource)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportDataSource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDataSource{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
