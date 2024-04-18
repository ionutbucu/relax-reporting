package de.adis_portal.server.reporting.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.adis_portal.server.reporting.domain.ReportDataSource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDataSourceDTO implements Serializable {

    private Long id;

    private String type;

    private String url;

    private String user;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDataSourceDTO)) {
            return false;
        }

        ReportDataSourceDTO reportDataSourceDTO = (ReportDataSourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDataSourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDataSourceDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
