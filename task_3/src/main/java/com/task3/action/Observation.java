package com.task3.action;

import com.task3.domain.Character;
import com.task3.domain.Marvin;
import com.task3.domain.Human;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Observation {
    private final UUID id;
    private final Marvin observer;
    private final Character observed;
    private final String observedBehavior;
    private final LocalDateTime timestamp;
    private final ObservationType type;

    public Observation(Marvin observer, Character observed, String observedBehavior) {
        this.id = UUID.randomUUID();
        this.observer = Objects.requireNonNull(observer, "Наблюдатель (Марвин) не может быть null");
        this.observed = Objects.requireNonNull(observed, "Наблюдаемый объект не может быть null");
        this.observedBehavior = Objects.requireNonNull(observedBehavior, "Наблюдаемое поведение не может быть null");
        this.timestamp = LocalDateTime.now();
        this.type = determineObservationType();
        validate();
    }

    private ObservationType determineObservationType() {
        if (observed instanceof Human) {
            if (observedBehavior.toLowerCase().contains("глуп") || 
                observedBehavior.toLowerCase().contains("бессмыслен")) {
                return ObservationType.SARCASTIC;
            }
        }
        return ObservationType.NEUTRAL;
    }

    private void validate() {
        if (observer != null && observed instanceof Marvin) {
            throw new IllegalStateException("Марвин не наблюдает за самим собой");
        }

        if (observedBehavior.trim().isEmpty()) {
            throw new IllegalStateException("Поведение для наблюдения не может быть пустым");
        }
    }

    public boolean isSarcasticTrigger() {
        return type == ObservationType.SARCASTIC;
    }

    public UUID getId() {
        return id;
    }

    public Marvin getObserver() {
        return observer;
    }

    public Character getObserved() {
        return observed;
    }

    public String getObservedBehavior() {
        return observedBehavior;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ObservationType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Observation that = (Observation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Observation{observer=%s, observed=%s, behavior='%s', type=%s, time=%s}",
            observer.getName(), observed.getName(), observedBehavior, type, timestamp);
    }
}