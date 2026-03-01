package com.task3.domain;

import com.task3.action.Observation;
import com.task3.emotion.EmotionalState;
import com.task3.emotion.EmotionType;
import com.task3.expression.Expression;
import com.task3.expression.ExpressionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Marvin extends Character {
    private static final String MARVIN_NAME = "Марвин";
    private final List<Observation> observations;
    private EmotionalState currentEmotionalState;
    private Expression lastExpression;

    public Marvin() {
        super(MARVIN_NAME);
        this.observations = new ArrayList<>();
        this.currentEmotionalState = null;
        this.lastExpression = null;
    }

    public Observation observe(Character target, String behavior) {
        Objects.requireNonNull(target, "Цель наблюдения не может быть null");
        Objects.requireNonNull(behavior, "Поведение не может быть null");

        Observation observation = new Observation(this, target, behavior);
        observations.add(observation);
        updateEmotionalState(observation);
        return observation;
    }


    private void updateEmotionalState(Observation observation) {

        System.out.println("Обновляем эмоциональное состояние на основе наблюдений: " + observation);
        System.out.println("Наличие триггера сарказма: " + observation.isSarcasticTrigger());
        System.out.println("Наблюдаемый объект является экземпляром Human: " + (observation.getObserved() instanceof Human));

        if (observation.isSarcasticTrigger() &&
                observation.getObserved() instanceof Human) {


            this.currentEmotionalState = new EmotionalState.Builder()
                    .addEmotions(EmotionType.CONTEMPT, EmotionType.HORROR)
                    .target(observation.getObserved())
                    .build();

            System.out.println("Эмоциональное состояние обновлено: " + this.currentEmotionalState);
        } else {
            this.currentEmotionalState = null;
            System.out.println("Условие не выполнено. Эмоциональное состояние сброшено в null");


            if (!observation.isSarcasticTrigger()) {
                System.out.println("Причина: observation.isSarcasticTrigger() is false");
            }
            if (!(observation.getObserved() instanceof Human)) {
                System.out.println("Причина: наблюдаемый объект не является Human, это " +
                        (observation.getObserved() == null ? "null" : observation.getObserved().getClass().getSimpleName()));
            }
        }
    }


    public Expression express() {
        if (currentEmotionalState == null) {
            throw new IllegalStateException("Марвин ничего не чувствует в данный момент");
        }

        this.lastExpression = ExpressionFactory.createExpressionWithEmotion(this, currentEmotionalState);
        return lastExpression;
    }


    public boolean feelsContemptTowards(Character target) {
        return currentEmotionalState != null &&
                currentEmotionalState.containsEmotion(EmotionType.CONTEMPT) &&
                currentEmotionalState.getTarget().equals(target);
    }


    public boolean feelsHorrorTowards(Character target) {
        return currentEmotionalState != null &&
                currentEmotionalState.containsEmotion(EmotionType.HORROR) &&
                currentEmotionalState.getTarget().equals(target);
    }


    public List<Observation> getObservationHistory() {
        return new ArrayList<>(observations);
    }


    public EmotionalState getCurrentEmotionalState() {
        return currentEmotionalState;
    }

    // Проверяет, наблюдал ли Марвин за указанным персонажем

    public boolean hasObserved(Character target) {
        return observations.stream()
                .anyMatch(obs -> obs.getObserved().equals(target));
    }

    public long getObservationCountFor(Character target) {
        return observations.stream()
                .filter(obs -> obs.getObserved().equals(target))
                .count();
    }

    @Override
    public String toString() {
        return String.format("Marvin{name='%s', observations=%d, hasEmotions=%s}",
                getName(),
                observations.size(),
                currentEmotionalState != null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Marvin marvin = (Marvin) o;
        return Objects.equals(observations, marvin.observations) &&
                Objects.equals(currentEmotionalState, marvin.currentEmotionalState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), observations, currentEmotionalState);
    }
}