package com.task3.expression;



import com.task3.action.Action;
import com.task3.action.CalculationLevel;
import com.task3.domain.Marvin;
import com.task3.emotion.EmotionalState;

import java.util.*;

public class Expression {
    private final UUID id;
    private final Marvin performer;
    private final List<Action> actions;
    private final EmotionalState conveyedEmotion;

    private Expression(Builder builder) {
        this.id = UUID.randomUUID();
        this.performer = builder.performer;
        this.actions = Collections.unmodifiableList(new ArrayList<>(builder.actions));
        this.conveyedEmotion = builder.conveyedEmotion;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(performer, "Исполнитель (Марвин) обязателен");
        Objects.requireNonNull(conveyedEmotion, "Эмоциональное состояние обязательно");

        if (actions == null || actions.isEmpty()) {
            throw new IllegalStateException("Выражение должно содержать хотя бы одно действие");
        }

        for (Action action : actions) {
            if (action.getCalculationLevel() != CalculationLevel.THOROUGH) {
                throw new IllegalStateException("Все действия должны быть тщательно рассчитаны");
            }
        }

        if (conveyedEmotion.getTarget() instanceof Marvin) {
            throw new IllegalStateException("Эмоции не могут быть направлены на Марвина");
        }
    }

    public static class Builder {
        private Marvin performer;
        private final List<Action> actions = new ArrayList<>();
        private EmotionalState conveyedEmotion;

        public Builder performer(Marvin performer) {
            this.performer = performer;
            return this;
        }

        public Builder addAction(Action action) {
            this.actions.add(action);
            return this;
        }

        public Builder addActions(Action... actions) {
            this.actions.addAll(Arrays.asList(actions));
            return this;
        }

        public Builder conveyedEmotion(EmotionalState emotion) {
            this.conveyedEmotion = emotion;
            return this;
        }

        public Expression build() {
            return new Expression(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public Marvin getPerformer() {
        return performer;
    }

    public List<Action> getActions() {
        return actions;
    }

    public EmotionalState getConveyedEmotion() {
        return conveyedEmotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Expression{" +
                "id=" + id +
                ", performer=" + performer +
                ", actions=" + actions +
                ", conveyedEmotion=" + conveyedEmotion +
                '}';
    }
}