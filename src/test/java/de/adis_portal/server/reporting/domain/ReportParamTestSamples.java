package de.adis_portal.server.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportParamTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportParam getReportParamSample1() {
        return new ReportParam().id(1L).name("name1").type("type1").value("value1").conversionRule("conversionRule1");
    }

    public static ReportParam getReportParamSample2() {
        return new ReportParam().id(2L).name("name2").type("type2").value("value2").conversionRule("conversionRule2");
    }

    public static ReportParam getReportParamRandomSampleGenerator() {
        return new ReportParam()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString())
            .conversionRule(UUID.randomUUID().toString());
    }
}
