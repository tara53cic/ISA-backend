package isa.jutjubic.crdt;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GCounter implements Serializable {

    private final Map<String, Long> counts = new ConcurrentHashMap<>();

    public void increment(String replicaId) {
        counts.merge(replicaId, 1L, Long::sum);
    }

    public void merge(GCounter other) {
        other.counts.forEach(
                (replicaId, value) ->
                        counts.merge(replicaId, value, Math::max)
        );
    }

    public long value() {
        return counts.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    public Map<String, Long> getState() {
        return counts;
    }
}
