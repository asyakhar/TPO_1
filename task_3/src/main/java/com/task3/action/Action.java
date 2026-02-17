package com.task3.action;

import java.util.Objects;

public class Action {
    private final ActionType type;
    private final Duration duration;
    private final CalculationLevel calculationLevel;

    private Action(Builder builder) {
        this.type = builder.type;
        this.duration = builder.duration;
        this.calculationLevel = builder.calculationLevel;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(type, "Тип действия не может быть null");
        Objects.requireNonNull(duration, "Длительность не может быть null");
        Objects.requireNonNull(calculationLevel, "Уровень расчета не может быть null");

        if (duration != Duration.MICROSECOND) {
            throw new IllegalStateException("Действие должно быть микросекундным");
        }

        if (calculationLevel != CalculationLevel.THOROUGH) {
            throw new IllegalStateException("Действие должно быть тщательно рассчитано");
        }
    }

    public static class Builder {
        private ActionType type;
        private Duration duration = Duration.MICROSECOND;
        private CalculationLevel calculationLevel = CalculationLevel.THOROUGH;

        public Builder type(ActionType type) {
            this.type = type;
            return this;
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder calculationLevel(CalculationLevel level) {
            this.calculationLevel = level;
            return this;
        }

        public Action build() {
            return new Action(this);
        }
    }

    public ActionType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public CalculationLevel getCalculationLevel() {
        return calculationLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return type == action.type &&
                duration == action.duration &&
                calculationLevel == action.calculationLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, duration, calculationLevel);
    }

    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                ", duration=" + duration +
                ", calculationLevel=" + calculationLevel +
                '}';
    }
}