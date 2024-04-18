package de.adis_portal.server.reporting.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportColumnMappingAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportColumnMappingAllPropertiesEquals(ReportColumnMapping expected, ReportColumnMapping actual) {
        assertReportColumnMappingAutoGeneratedPropertiesEquals(expected, actual);
        assertReportColumnMappingAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportColumnMappingAllUpdatablePropertiesEquals(ReportColumnMapping expected, ReportColumnMapping actual) {
        assertReportColumnMappingUpdatableFieldsEquals(expected, actual);
        assertReportColumnMappingUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportColumnMappingAutoGeneratedPropertiesEquals(ReportColumnMapping expected, ReportColumnMapping actual) {
        assertThat(expected)
            .as("Verify ReportColumnMapping auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportColumnMappingUpdatableFieldsEquals(ReportColumnMapping expected, ReportColumnMapping actual) {
        assertThat(expected)
            .as("Verify ReportColumnMapping relevant properties")
            .satisfies(e -> assertThat(e.getSourceColumnName()).as("check sourceColumnName").isEqualTo(actual.getSourceColumnName()))
            .satisfies(e -> assertThat(e.getSourceColumnIndex()).as("check sourceColumnIndex").isEqualTo(actual.getSourceColumnIndex()))
            .satisfies(e -> assertThat(e.getColumnTitle()).as("check columnTitle").isEqualTo(actual.getColumnTitle()))
            .satisfies(e -> assertThat(e.getLang()).as("check lang").isEqualTo(actual.getLang()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportColumnMappingUpdatableRelationshipsEquals(ReportColumnMapping expected, ReportColumnMapping actual) {
        assertThat(expected)
            .as("Verify ReportColumnMapping relationships")
            .satisfies(e -> assertThat(e.getReport()).as("check report").isEqualTo(actual.getReport()));
    }
}
