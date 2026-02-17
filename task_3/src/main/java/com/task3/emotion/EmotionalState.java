package com.task3.emotion;

import com.task3.domain.Character;
import com.task3.domain.Human;
import com.task3.domain.Marvin;

import java.util.*;

public class EmotionalState {
    private final Set<EmotionType> types;
    private final Intensity intensity;
    private final Character target;

    private EmotionalState(Builder builder) {
        this.types = Collections.unmodifiableSet(new HashSet<>(builder.types));
        this.intensity = builder.intensity;
        this.target = builder.target;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(types, "Типы эмоций не могут быть null");
        Objects.requireNonNull(intensity, "Интенсивность не может быть null");
        Objects.requireNonNull(target, "Цель не может быть null");

        if (types.isEmpty()) {
            throw new IllegalStateException("Должна быть хотя бы одна эмоция");
        }

        for (EmotionType type : types) {
            if (type != EmotionType.CONTEMPT && type != EmotionType.HORROR) {
                throw new IllegalStateException("Допустимы только эмоции: презрение и ужас");
            }
        }

        if (intensity != Intensity.COMPLETE) {
            throw new IllegalStateException("Интенсивность эмоций должна быть полной");
        }


        if (target instanceof Marvin) {
            throw new IllegalStateException("Эмоции не могут быть направлены на Марвина");
        }
    }

    public static class Builder {
        private final Set<EmotionType> types = new HashSet<>();
        private Intensity intensity = Intensity.COMPLETE;
        private Character target;

        public Builder addEmotion(EmotionType type) {
            this.types.add(type);
            return this;
        }

        public Builder addEmotions(EmotionType... types) {
            this.types.addAll(Arrays.asList(types));
            return this;
        }

        public Builder intensity(Intensity intensity) {
            this.intensity = intensity;
            return this;
        }

        public Builder target(Character target) {
            this.target = target;
            return this;
        }

        public EmotionalState build() {
            return new EmotionalState(this);
        }
    }

    public Set<EmotionType> getTypes() {
        return types;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public Character getTarget() {
        return target;
    }

    public boolean containsEmotion(EmotionType type) {
        return types.contains(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmotionalState that = (EmotionalState) o;
        return Objects.equals(types, that.types) &&
                intensity == that.intensity &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(types, intensity, target);
    }

    @Override
    public String toString() {
        return "EmotionalState{" +
                "types=" + types +
                ", intensity=" + intensity +
                ", target=" + target +
                '}';
    }
}