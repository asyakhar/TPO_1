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

    /**
     * Марвин наблюдает за кем-то
     */
    public Observation observe(Character target, String behavior) {
        Objects.requireNonNull(target, "Цель наблюдения не может быть null");
        Objects.requireNonNull(behavior, "Поведение не может быть null");

        Observation observation = new Observation(this, target, behavior);
        observations.add(observation);
        updateEmotionalState(observation);
        return observation;
    }

    /**
     * Обновляет эмоциональное состояние на основе наблюдения
     */
    private void updateEmotionalState(Observation observation) {
        // Логируем для отладки
        System.out.println("Updating emotional state based on observation: " + observation);
        System.out.println("isSarcasticTrigger: " + observation.isSarcasticTrigger());
        System.out.println("Observed instance of Human: " + (observation.getObserved() instanceof Human));

        // Проверяем, вызывает ли наблюдение сарказм (глупое поведение человечества)
        if (observation.isSarcasticTrigger() &&
                observation.getObserved() instanceof Human) {

            // Создаем новое эмоциональное состояние с презрением и ужасом
            this.currentEmotionalState = new EmotionalState.Builder()
                    .addEmotions(EmotionType.CONTEMPT, EmotionType.HORROR)
                    .target(observation.getObserved())
                    .build();

            System.out.println("Emotional state UPDATED: " + this.currentEmotionalState);
        } else {
            // Если условие не выполнено, сбрасываем состояние и логируем причину
            this.currentEmotionalState = null;
            System.out.println("Condition not met. Emotional state reset to null.");

            // Дополнительная диагностика
            if (!observation.isSarcasticTrigger()) {
                System.out.println("Reason: observation.isSarcasticTrigger() is false");
            }
            if (!(observation.getObserved() instanceof Human)) {
                System.out.println("Reason: observed is not Human, it's " +
                        (observation.getObserved() == null ? "null" : observation.getObserved().getClass().getSimpleName()));
            }
        }
    }

    /**
     * Марвин выражает свое текущее эмоциональное состояние
     */
    public Expression express() {
        if (currentEmotionalState == null) {
            throw new IllegalStateException("Марвин ничего не чувствует в данный момент");
        }

        this.lastExpression = ExpressionFactory.createExpressionWithEmotion(this, currentEmotionalState);
        return lastExpression;
    }

    /**
     * Проверяет, испытывает ли Марвин презрение к кому-либо
     */
    public boolean feelsContemptTowards(Character target) {
        return currentEmotionalState != null &&
                currentEmotionalState.containsEmotion(EmotionType.CONTEMPT) &&
                currentEmotionalState.getTarget().equals(target);
    }

    /**
     * Проверяет, испытывает ли Марвин ужас к кому-либо
     */
    public boolean feelsHorrorTowards(Character target) {
        return currentEmotionalState != null &&
                currentEmotionalState.containsEmotion(EmotionType.HORROR) &&
                currentEmotionalState.getTarget().equals(target);
    }

    /**
     * Возвращает историю наблюдений (копию для защиты от изменений)
     */
    public List<Observation> getObservationHistory() {
        return new ArrayList<>(observations);
    }

    /**
     * Возвращает текущее эмоциональное состояние
     */
    public EmotionalState getCurrentEmotionalState() {
        return currentEmotionalState;
    }

    /**
     * Возвращает последнее выражение
     */
    public Expression getLastExpression() {
        return lastExpression;
    }

    /**
     * Сбрасывает эмоциональное состояние Марвина
     */
    public void resetEmotionalState() {
        this.currentEmotionalState = null;
        this.lastExpression = null;
    }

    /**
     * Проверяет, наблюдал ли Марвин за указанным персонажем
     */
    public boolean hasObserved(Character target) {
        return observations.stream()
                .anyMatch(obs -> obs.getObserved().equals(target));
    }

    /**
     * Возвращает количество наблюдений за указанным персонажем
     */
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