package ru.sirius.hl.service;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class ObservabilityService {
    private final Map<String, ConcurrentLinkedQueue<Timing>> timings = new ConcurrentHashMap<>();

    private final long minuteWindow = 60_000;
    private final long[] statsWindows = {20_000, 30_000, 60_000};


    private record Timing(long timestamp, long duration) {}

    @PostConstruct
    public void init() {
        timings.put("controller", new ConcurrentLinkedQueue<>());
        timings.put("database", new ConcurrentLinkedQueue<>());
        timings.put("additionalStats", new ConcurrentLinkedQueue<>());
    }

    public long startTiming() {
        return System.nanoTime();
    }

    public void stopTiming(long startTime, String operation) {
        long duration = (System.nanoTime() - startTime) / 1_000_000; // в миллисекундах
        timings.computeIfAbsent(operation, k -> new ConcurrentLinkedQueue<>())
                .add(new Timing(Instant.now().toEpochMilli(), duration));
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanOldTimings() {
        long now = Instant.now().toEpochMilli();
        timings.forEach((operation, queue) -> {
            while (!queue.isEmpty() && now - queue.peek().timestamp > minuteWindow) {
                queue.poll();
            }
        });
    }


    @Scheduled(fixedRate = 20_000)
    public void buildStatistics() {
        long now = Instant.now().toEpochMilli();

        timings.forEach((operation, queue) -> {
            for (long window : statsWindows) {
                AtomicLong count = new AtomicLong(0);
                AtomicLong totalDuration = new AtomicLong(0);

                queue.forEach(timing -> {
                    if (now - timing.timestamp <= window) {
                        count.incrementAndGet();
                        totalDuration.addAndGet(timing.duration);
                    }
                });

                long avgDuration = count.get() > 0 ? totalDuration.get() / count.get() : 0;

                System.out.printf("Operation: %s, Window: %dms, Count: %d, Avg Duration: %dms%n",
                        operation, window, count.get(), avgDuration);
            }
        });
    }
}