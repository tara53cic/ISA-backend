package isa.jutjubic.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import isa.jutjubic.service.MonitoringService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    private final MeterRegistry registry;

    private final Set<String> activeUsernames = ConcurrentHashMap.newKeySet();

    public MonitoringServiceImpl(MeterRegistry registry) {
        this.registry = registry;

        Gauge.builder("app_active_users_count", activeUsernames, Set::size)
                .description("Number of unique users active in the current window")
                .register(registry);
    }

    public void logUserActivity(String username) {
        activeUsernames.add(username);
    }

    @Scheduled(fixedRate = 300000)
    public void resetActiveUsers() {
        activeUsernames.clear();
    }
}
