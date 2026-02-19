import com.task3.action.Action;
import com.task3.action.ActionType;
import com.task3.action.CalculationLevel;
import com.task3.domain.Human;
import com.task3.domain.Marvin;
import com.task3.emotion.EmotionType;
import com.task3.emotion.EmotionalState;
import com.task3.emotion.Intensity;
import com.task3.expression.Expression;
import com.task3.expression.ExpressionFactory;
import com.task3.expression.ExpressionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarvinTest {

    private Marvin marvin;
    private Human humanity;

    @BeforeEach
    void setUp() {
        marvin = new Marvin();
        humanity = new Human("Человечество");
    }

    @Test
    void shouldNotCreateExpressionWithoutPerformer() {
        Action pause = new Action.Builder()
                .type(ActionType.PAUSE)
                .build();

        EmotionalState emotion = new EmotionalState.Builder()
                .addEmotions(EmotionType.CONTEMPT, EmotionType.HORROR)
                .target(humanity)
                .build();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Expression.Builder()
                    .addAction(pause)
                    .conveyedEmotion(emotion)
                    .build();
        });

        assertTrue(exception.getMessage().contains("Исполнитель (Марвин) обязателен"));
    }

    @Test
    void shouldNotTargetMarvinWithContempt() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new EmotionalState.Builder()
                    .addEmotion(EmotionType.CONTEMPT)
                    .target(marvin)
                    .build();
        });

        assertTrue(exception.getMessage().contains("не могут быть направлены на Марвина"));
    }

    @Test
    void allActionsShouldHaveThoroughCalculation() {
        Action action = new Action.Builder()
                .type(ActionType.PAUSE)
                .build();
        
        assertEquals(CalculationLevel.THOROUGH, action.getCalculationLevel());
    }

    @Test
    void shouldCreateSarcasticContemptExpression() {

        Expression expression = ExpressionFactory.createSarcasticContemptExpression();

        assertEquals(marvin, expression.getPerformer());

        List<Action> actions = expression.getActions();
        assertEquals(3, actions.size());

        assertTrue(actions.stream().anyMatch(a -> a.getType() == ActionType.PAUSE));
        assertTrue(actions.stream().anyMatch(a -> a.getType() == ActionType.INTONATION_MODULATION));
        assertTrue(actions.stream().anyMatch(a -> a.getType() == ActionType.TIMBRE_MODULATION));
        

        actions.forEach(action -> 
            assertEquals(CalculationLevel.THOROUGH, action.getCalculationLevel())
        );


        EmotionalState emotion = expression.getConveyedEmotion();
        assertTrue(emotion.containsEmotion(EmotionType.CONTEMPT));
        assertTrue(emotion.containsEmotion(EmotionType.HORROR));
        assertEquals(Intensity.COMPLETE, emotion.getIntensity());
        assertEquals("Человечество", emotion.getTarget().getName());


        ExpressionValidator.ValidationResult result =
                ExpressionValidator.validate(expression);
        
        assertTrue(result.isValid());
    }

    @Test
    void shouldNotCreateExpressionWithoutActions() {
        EmotionalState emotion = new EmotionalState.Builder()
                .addEmotions(EmotionType.CONTEMPT, EmotionType.HORROR)
                .target(humanity)
                .build();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new Expression.Builder()
                    .performer(marvin)
                    .conveyedEmotion(emotion)
                    .build();
        });

        assertTrue(exception.getMessage().contains("хотя бы одно действие"));
    }

    @Test
    void shouldNotCreateEmotionalStateWithoutEmotions() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new EmotionalState.Builder()
                    .target(humanity)
                    .build();
        });

        assertTrue(exception.getMessage().contains("хотя бы одна эмоция"));
    }

    @Test
    void shouldNotCreateActionWithInvalidDuration() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Action.Builder()
                    .type(ActionType.PAUSE)
                    .duration(null)
                    .build();
        });

        assertTrue(exception.getMessage().contains("Длительность не может быть null"));
    }
    @Test
    void expressionShouldNotContainOffensiveActions() {

        Expression expression = ExpressionFactory.createSarcasticContemptExpression();
        List<Action> actions = expression.getActions();

        assertTrue(actions.stream().allMatch(a ->
                a.getType() == ActionType.PAUSE ||
                        a.getType() == ActionType.INTONATION_MODULATION ||
                        a.getType() == ActionType.TIMBRE_MODULATION
        ));
    }
}