package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Report getReportSample1() {
        return new Report().id(1L).name("name1").description("description1").query("query1").fileName("fileName1");
    }

    public static Report getReportSample2() {
        return new Report().id(2L).name("name2").description("description2").query("query2").fileName("fileName2");
    }

    public static Report getReportRandomSampleGenerator() {
        return new Report()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .query(UUID.randomUUID().toString())
            .fileName(UUID.randomUUID().toString());
    }
}
