package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReportMetadataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportMetadata getReportMetadataSample1() {
        return new ReportMetadata().id(1L);
    }

    public static ReportMetadata getReportMetadataSample2() {
        return new ReportMetadata().id(2L);
    }

    public static ReportMetadata getReportMetadataRandomSampleGenerator() {
        return new ReportMetadata().id(longCount.incrementAndGet());
    }
}
