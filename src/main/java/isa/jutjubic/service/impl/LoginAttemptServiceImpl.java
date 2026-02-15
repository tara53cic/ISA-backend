package isa.jutjubic.service.impl;

import isa.jutjubic.service.LoginAttemptService;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_MS = 60_000;

    private final Map<String, Deque<Long>> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String ip) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = attempts.get(ip);

        if (timestamps == null) {
            return false;
        }

        // remove old attempts
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > WINDOW_MS) {
            timestamps.pollFirst();
        }

        return timestamps.size() >= MAX_ATTEMPTS;
    }

    public void recordFailedAttempt(String ip) {
        attempts.computeIfAbsent(ip, k -> new ArrayDeque<>())
                .addLast(System.currentTimeMillis());
    }

    public void resetAttempts(String ip) {
        attempts.remove(ip);
    }
}

