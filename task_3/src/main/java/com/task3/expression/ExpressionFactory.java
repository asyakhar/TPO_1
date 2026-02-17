package com.task3.expression;


import com.task3.action.Action;
import com.task3.action.ActionType;
import com.task3.domain.Human;
import com.task3.domain.Marvin;
import com.task3.emotion.EmotionType;
import com.task3.emotion.EmotionalState;

public class ExpressionFactory {

    public static Expression createSarcasticContemptExpression() {
        Marvin marvin = new Marvin();
        Human humanity = new Human("Человечество");

        Action pause = new Action.Builder()
                .type(ActionType.PAUSE)
                .build();

        Action intonationMod = new Action.Builder()
                .type(ActionType.INTONATION_MODULATION)
                .build();

        Action timbreMod = new Action.Builder()
                .type(ActionType.TIMBRE_MODULATION)
                .build();

        EmotionalState emotion = new EmotionalState.Builder()
                .addEmotions(EmotionType.CONTEMPT, EmotionType.HORROR)
                .target(humanity)
                .build();

        return new Expression.Builder()
                .performer(marvin)
                .addActions(pause, intonationMod, timbreMod)
                .conveyedEmotion(emotion)
                .build();
    }
}