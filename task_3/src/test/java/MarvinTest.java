import com.task3.action.*;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для доменной модели Марвина")
public class MarvinTest {

    private Marvin marvin;
    private Human humanity;

    @BeforeEach
    void setUp() {
        marvin = new Marvin();
        humanity = new Human("Человечество");
    }

    @Test
    @DisplayName("Нельзя создать выражение без исполнителя (Марвина)")
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
    @DisplayName("Нельзя направить презрение на самого Марвина")
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
    @DisplayName("Все действия Марвина имеют тщательный расчет")
    void allActionsShouldHaveThoroughCalculation() {
        Action action = new Action.Builder()
                .type(ActionType.PAUSE)
                .build();

        assertEquals(CalculationLevel.THOROUGH, action.getCalculationLevel());
    }

    @Test
    @DisplayName("Создание саркастического выражения с презрением через фабрику")
    void shouldCreateSarcasticContemptExpression() {

        Expression expression = ExpressionFactory.createSarcasticExpression();

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
    @DisplayName("Нельзя создать выражение без действий")
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
    @DisplayName("Нельзя создать эмоциональное состояние без эмоций")
    void shouldNotCreateEmotionalStateWithoutEmotions() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new EmotionalState.Builder()
                    .target(humanity)
                    .build();
        });

        assertTrue(exception.getMessage().contains("хотя бы одна эмоция"));
    }

    @Test
    @DisplayName("Нельзя создать действие с недопустимой длительностью")
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
    @DisplayName("Выражение Марвина не содержит обидных действий")
    void expressionShouldNotContainOffensiveActions() {
        Expression expression = ExpressionFactory.createSarcasticExpression();
        List<Action> actions = expression.getActions();

        assertTrue(actions.stream().allMatch(a ->
                a.getType() == ActionType.PAUSE ||
                        a.getType() == ActionType.INTONATION_MODULATION ||
                        a.getType() == ActionType.TIMBRE_MODULATION
        ));
    }

    // Первоначальные новые тесты
    @Test
    @DisplayName("Марвин не выражает презрение через обидные действия")
    void marvinShouldNotExpressContemptThroughOffensiveActions() {
        Action offensiveAction = new Action.Builder()
                .type(ActionType.MOCKING_LAUGHTER) // обидное действие
                .build();

        EmotionalState contempt = new EmotionalState.Builder()
                .addEmotion(EmotionType.CONTEMPT)
                .target(humanity)
                .build();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new Expression.Builder()
                    .performer(marvin)
                    .addAction(offensiveAction)
                    .conveyedEmotion(contempt)
                    .build();
        });

        assertTrue(exception.getMessage().contains("не содержит обидных действий"));
    }

    @Test
    @DisplayName("Сарказм Марвина разворачивается в определенном порядке: сначала пауза")
    void marvinsSarcasmUnfoldsInSpecificOrder() {

        Expression expression = ExpressionFactory.createSarcasticExpression();
        List<Action> actions = expression.getActions();

        assertEquals(ActionType.PAUSE, actions.get(0).getType());

        List<ActionType> subsequentTypes = actions.subList(1, actions.size())
                .stream()
                .map(Action::getType)
                .toList();

        assertTrue(subsequentTypes.contains(ActionType.INTONATION_MODULATION));
        assertTrue(subsequentTypes.contains(ActionType.TIMBRE_MODULATION));
    }

    @Test
    @DisplayName("Марвин выражает презрение после наблюдения за человечеством")
    void marvinExpressesContemptAfterObservingHumanity() {

        Observation observation = new Observation(marvin, humanity, "человечество снова ведет себя бессмысленно");

        assertTrue(observation.isSarcasticTrigger(),
                "Наблюдение за глупым поведением людей должно вызывать сарказм");

        Expression reaction = ExpressionFactory.createSarcasticExpression();

        assertTrue(reaction.getConveyedEmotion().containsEmotion(EmotionType.CONTEMPT),
                "Марвин должен выражать презрение");
        assertTrue(reaction.getConveyedEmotion().containsEmotion(EmotionType.HORROR),
                "Марвин должен выражать ужас");
        assertEquals(humanity, reaction.getConveyedEmotion().getTarget(),
                "Презрение направлено на человечество");
    }

// еще новые тесты
    @Test
    @DisplayName("Марвин не обижается - после наблюдения не создает обидных действий")
    void marvinIsNotOffensiveAfterObservation() {
        marvin.observe(humanity, "люди ведут себя глупо и бессмысленно");

        Expression expression = marvin.express();

        List<Action> actions = expression.getActions();
        boolean hasOffensiveActions = actions.stream()
                .anyMatch(action -> action.getType().isOffensive());

        assertFalse(hasOffensiveActions, "Марвин не должен использовать обидные действия");


        assertTrue(actions.stream().allMatch(a ->
                a.getType() == ActionType.PAUSE ||
                        a.getType() == ActionType.INTONATION_MODULATION ||
                        a.getType() == ActionType.TIMBRE_MODULATION
        ), "Марвин использует только микро-паузы и модуляции");


        assertEquals(1, marvin.getObservationHistory().size());
        assertTrue(marvin.hasObserved(humanity));
    }



    @Test
    @DisplayName("Марвин выражает презрение только после наблюдения")
    void marvinExpressesContemptOnlyAfterObservation() {

        assertNull(marvin.getCurrentEmotionalState(),
                "Изначально у Марвина нет эмоций");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            marvin.express();
        });

        assertTrue(exception.getMessage().contains("ничего не чувствует"),
                "Марвин не может выражать эмоции без наблюдения");

        marvin.observe(humanity, "люди снова глупят");
        assertNotNull(marvin.getCurrentEmotionalState(),
                "После наблюдения у Марвина появляются эмоции");

        Expression expression = marvin.express();
        assertNotNull(expression, "После наблюдения Марвин может выразить эмоции");
    }

    @Test
    @DisplayName("Марвин различает наблюдения за разными целями")
    void marvinDistinguishesBetweenDifferentTargets() {
        Human arthur = new Human("Артур Дент");
        Human ford = new Human("Форд Префект");

        marvin.observe(arthur, "обычное поведение");
        marvin.observe(ford, "обычное поведение");
        marvin.observe(humanity, "глупое поведение человечества");

        assertEquals(3, marvin.getObservationHistory().size());
        assertEquals(1, marvin.getObservationCountFor(arthur));
        assertEquals(1, marvin.getObservationCountFor(ford));
        assertEquals(1, marvin.getObservationCountFor(humanity));

        EmotionalState state = marvin.getCurrentEmotionalState();
        assertEquals(humanity, state.getTarget(),
                "Презрение должно быть направлено на человечество");

        assertTrue(marvin.feelsContemptTowards(humanity),
                "Презрение к человечеству");
        assertFalse(marvin.feelsContemptTowards(arthur),
                "Нет презрения к Артуру");
        assertFalse(marvin.feelsContemptTowards(ford),
                "Нет презрения к Форду");
    }



    @Test
    @DisplayName("Полная цепочка взаимодействия: наблюдение -> эмоция -> выражение -> валидация")
    void completeInteractionChain() {
        String stupidBehavior = "человечество демонстрирует глупое и бессмысленное поведение";
        Observation observation = marvin.observe(humanity, stupidBehavior);

        assertTrue(observation.isSarcasticTrigger(),
                "Наблюдение должно быть саркастическим триггером (содержит 'глуп' и 'бессмыслен')");
        assertEquals(ObservationType.SARCASTIC, observation.getType(),
                "Тип наблюдения должен быть SARCASTIC");

        EmotionalState emotion = marvin.getCurrentEmotionalState();
        assertNotNull(emotion, "После наблюдения должно появиться эмоциональное состояние");
        assertTrue(emotion.containsEmotion(EmotionType.CONTEMPT), "Должно быть презрение");
        assertTrue(emotion.containsEmotion(EmotionType.HORROR), "Должен быть ужас");
        assertEquals(humanity, emotion.getTarget(), "Эмоции направлены на человечество");
        assertEquals(Intensity.COMPLETE, emotion.getIntensity(), "Интенсивность должна быть полной");

        Expression expression = marvin.express();

        assertNotNull(expression, "Выражение должно быть создано");
        assertEquals(marvin, expression.getPerformer(), "Исполнитель - Марвин");
        assertEquals(emotion, expression.getConveyedEmotion(), "Эмоции совпадают");

        List<Action> actions = expression.getActions();
        assertFalse(actions.isEmpty(), "Выражение должно содержать действия");
        assertEquals(3, actions.size(), "Должно быть ровно 3 действия");

        assertTrue(actions.stream().anyMatch(a -> a.getType() == ActionType.PAUSE),
                "Должна быть пауза");
        assertTrue(actions.stream().anyMatch(a -> a.getType() == ActionType.INTONATION_MODULATION),
                "Должна быть модуляция интонации");
        assertTrue(actions.stream().anyMatch(a -> a.getType() == ActionType.TIMBRE_MODULATION),
                "Должна быть модуляция тембра");

        assertTrue(actions.stream().allMatch(a -> a.getCalculationLevel() == CalculationLevel.THOROUGH),
                "Все действия должны быть тщательно рассчитаны");

        assertTrue(actions.stream().allMatch(a -> a.getDuration() == Duration.MICROSECOND),
                "Все действия должны быть микросекундными");

        ExpressionValidator.ValidationResult result =
                ExpressionValidator.validate(expression);

        assertTrue(result.isValid(), "Вся цепочка должна быть валидна");
        assertTrue(result.getErrors().isEmpty(), "Ошибок быть не должно: " + result.getErrors());

        assertTrue(marvin.feelsContemptTowards(humanity), "Презрение к человечеству");
        assertTrue(marvin.feelsHorrorTowards(humanity), "Ужас к человечеству");
        assertTrue(marvin.hasObserved(humanity), "Марвин наблюдал за человечеством");
        assertEquals(1, marvin.getObservationCountFor(humanity), "Должно быть ровно одно наблюдение");

        List<Observation> history = marvin.getObservationHistory();
        assertEquals(1, history.size(), "История должна содержать одно наблюдение");
        assertEquals(stupidBehavior, history.get(0).getObservedBehavior(),
                "Поведение в истории должно совпадать");
    }

    @Test
    @DisplayName("Марвин не реагирует на обычное поведение")
    void marvinDoesNotReactToNormalBehavior() {
        String normalBehavior = "человечество занимается обычными делами";

        Observation observation = marvin.observe(humanity, normalBehavior);

        assertFalse(observation.isSarcasticTrigger(),
                "Обычное поведение не должно быть саркастическим триггером");
        assertEquals(ObservationType.NEUTRAL, observation.getType(),
                "Тип наблюдения должен быть NEUTRAL");

        assertNull(marvin.getCurrentEmotionalState(),
                "При обычном поведении эмоции должны отсутствовать");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            marvin.express();
        });

        assertTrue(exception.getMessage().contains("ничего не чувствует"),
                "Должно быть исключение об отсутствии эмоций");
    }
}