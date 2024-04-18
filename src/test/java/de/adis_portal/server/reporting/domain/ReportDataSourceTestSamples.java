package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportDataSourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportDataSource getReportDataSourceSample1() {
        return new ReportDataSource().id(1L).type("type1").url("url1").user("user1").password("password1");
    }

    public static ReportDataSource getReportDataSourceSample2() {
        return new ReportDataSource().id(2L).type("type2").url("url2").user("user2").password("password2");
    }

    public static ReportDataSource getReportDataSourceRandomSampleGenerator() {
        return new ReportDataSource()
            .id(longCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .user(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
