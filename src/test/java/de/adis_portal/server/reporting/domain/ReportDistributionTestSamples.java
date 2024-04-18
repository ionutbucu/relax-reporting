package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportDistributionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportDistribution getReportDistributionSample1() {
        return new ReportDistribution().id(1L).email("email1").description("description1");
    }

    public static ReportDistribution getReportDistributionSample2() {
        return new ReportDistribution().id(2L).email("email2").description("description2");
    }

    public static ReportDistribution getReportDistributionRandomSampleGenerator() {
        return new ReportDistribution()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
