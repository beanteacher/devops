package com.example.actuator.metric.gauge;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueGauge {
    private final MeterRegistry registry;
    private final AtomicInteger smsSize = new AtomicInteger(0);
    private final AtomicInteger mmsSize = new AtomicInteger(0);
    private final AtomicInteger rcsSize = new AtomicInteger(0);

    @PostConstruct
    void init() {
        Gauge.builder("relay_queue_size", smsSize, AtomicInteger::get)
                .tag("type","sms").register(registry);
        Gauge.builder("relay_queue_size", mmsSize, AtomicInteger::get)
                .tag("type","mms").register(registry);
        Gauge.builder("relay_queue_size", rcsSize, AtomicInteger::get)
                .tag("type","rcs").register(registry);
        Gauge.builder("relay_queue_size", () ->
                        smsSize.get() + mmsSize.get() + rcsSize.get())
                .description("전송 대기 큐 길이(합계)")
                .baseUnit("messages")
                .tag("type", "total")
                .register(registry);
    }

    @Scheduled(fixedDelayString = "15000")
    void refreshFromDb() {
        smsSize.set(10);
        mmsSize.set(100);
        rcsSize.set(1000);
    }
}
