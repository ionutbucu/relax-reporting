package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportExecutionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportExecution getReportExecutionSample1() {
        return new ReportExecution().id(1L).error("error1").url("url1").user("user1");
    }

    public static ReportExecution getReportExecutionSample2() {
        return new ReportExecution().id(2L).error("error2").url("url2").user("user2");
    }

    public static ReportExecution getReportExecutionRandomSampleGenerator() {
        return new ReportExecution()
            .id(longCount.incrementAndGet())
            .error(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .user(UUID.randomUUID().toString());
    }
}
