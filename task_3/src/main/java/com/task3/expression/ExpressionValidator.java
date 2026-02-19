package com.task3.expression;



import com.task3.action.Action;
import com.task3.action.CalculationLevel;
import com.task3.domain.Marvin;
import com.task3.emotion.EmotionType;
import com.task3.emotion.EmotionalState;
import com.task3.emotion.Intensity;

import java.util.ArrayList;
import java.util.List;

public class ExpressionValidator {

    public static ValidationResult validate(Expression expression) {
        List<String> errors = new ArrayList<>();

        if (expression.getPerformer() == null) {
            errors.add("Исполнителем выражения должен быть Марвин");
        }

        for (Action action : expression.getActions()) {
            if (action.getCalculationLevel() != CalculationLevel.THOROUGH) {
                errors.add("Действие " + action.getType() + " не имеет тщательного расчета");
            }
        }

        EmotionalState emotion = expression.getConveyedEmotion();

        if (!emotion.containsEmotion(EmotionType.CONTEMPT)) {
            errors.add("Эмоции должны включать презрение");
        }

        if (!emotion.containsEmotion(EmotionType.HORROR)) {
            errors.add("Эмоции должны включать ужас");
        }

        if (emotion.getIntensity() != Intensity.COMPLETE) {
            errors.add("Интенсивность эмоций должна быть полной");
        }

        if (!"Человечество".equals(emotion.getTarget().getName())) {
            errors.add("Эмоции должны быть направлены на Человечество");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        @Override
        public String toString() {
            if (valid) {
                return "Выражение валидно";
            }
            return "Ошибки валидации: " + String.join(", ", errors);
        }
    }
}