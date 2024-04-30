package de.adis_portal.server.reporting.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportScheduleAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportScheduleAllPropertiesEquals(ReportSchedule expected, ReportSchedule actual) {
        assertReportScheduleAutoGeneratedPropertiesEquals(expected, actual);
        assertReportScheduleAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportScheduleAllUpdatablePropertiesEquals(ReportSchedule expected, ReportSchedule actual) {
        assertReportScheduleUpdatableFieldsEquals(expected, actual);
        assertReportScheduleUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportScheduleAutoGeneratedPropertiesEquals(ReportSchedule expected, ReportSchedule actual) {}

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportScheduleUpdatableFieldsEquals(ReportSchedule expected, ReportSchedule actual) {
        assertThat(expected)
            .as("Verify ReportSchedule relevant properties")
            .satisfies(e -> assertThat(e.getRid()).as("check rid").isEqualTo(actual.getRid()))
            .satisfies(e -> assertThat(e.getCron()).as("check cron").isEqualTo(actual.getCron()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportScheduleUpdatableRelationshipsEquals(ReportSchedule expected, ReportSchedule actual) {
        assertThat(expected)
            .as("Verify ReportSchedule relationships")
            .satisfies(e -> assertThat(e.getReport()).as("check report").isEqualTo(actual.getReport()));
    }
}
