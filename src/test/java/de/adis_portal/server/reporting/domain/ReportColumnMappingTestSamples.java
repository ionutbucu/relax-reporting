package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReportColumnMappingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReportColumnMapping getReportColumnMappingSample1() {
        return new ReportColumnMapping()
            .id(1L)
            .sourceColumnName("sourceColumnName1")
            .sourceColumnIndex(1)
            .columnTitle("columnTitle1")
            .lang("lang1");
    }

    public static ReportColumnMapping getReportColumnMappingSample2() {
        return new ReportColumnMapping()
            .id(2L)
            .sourceColumnName("sourceColumnName2")
            .sourceColumnIndex(2)
            .columnTitle("columnTitle2")
            .lang("lang2");
    }

    public static ReportColumnMapping getReportColumnMappingRandomSampleGenerator() {
        return new ReportColumnMapping()
            .id(longCount.incrementAndGet())
            .sourceColumnName(UUID.randomUUID().toString())
            .sourceColumnIndex(intCount.incrementAndGet())
            .columnTitle(UUID.randomUUID().toString())
            .lang(UUID.randomUUID().toString());
    }
}
