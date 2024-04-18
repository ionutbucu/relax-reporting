package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportScheduleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportSchedule getReportScheduleSample1() {
        return new ReportSchedule().id(1L).cron("cron1");
    }

    public static ReportSchedule getReportScheduleSample2() {
        return new ReportSchedule().id(2L).cron("cron2");
    }

    public static ReportSchedule getReportScheduleRandomSampleGenerator() {
        return new ReportSchedule().id(longCount.incrementAndGet()).cron(UUID.randomUUID().toString());
    }
}
