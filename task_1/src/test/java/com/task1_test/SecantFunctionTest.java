package com.task1_test;

import com.task1.SecantFunction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class SecantFunctionTest {

    private static final double EPSILON = 1e-10;
    private static final double BOUNDARY = Math.PI / 2;

    // Эталон для сравнения (используем Math.cos только в тестах!)
    private static double referenceSec(double x) {
        return 1.0 / Math.cos(x);
    }

    // Значения внутри радиуса сходимости

    @Test
    public void testSecAtZero() {
        double x = 0.0;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, EPSILON, "sec(0) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtNegativeZero() {
        double x = -0.0;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, EPSILON, "sec(-0) должен совпасть с эталоном");

        assertEquals(SecantFunction.sec(0.0), SecantFunction.sec(-0.0), EPSILON, "sec должен быть четным");
    }

    @Test
    public void testSecAtOne() {
        double x = 1.0;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(1) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtNegativeHalf() {
        double x = -0.5;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(-0.5) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtPositiveHalf() {
        double x = 0.5;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(0.5) должен совпасть с эталоном");
        assertEquals(SecantFunction.sec(0.5), SecantFunction.sec(-0.5), 1e-12, "sec должен быть четным");
    }

    @Test
    public void testSecAtVerySmallValue() {
        double x = 0.000001;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-13, "sec(0.000001) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtMinValue() {
        double x = Double.MIN_VALUE;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-300, "sec(Double.MIN_VALUE) должен совпасть с эталоном");
    }

    // Точки, близкие к границе сходимости

    @Test
    public void testSecNearPositiveBoundary() {
        double x = 1.5707;

        // Используем больше членов ряда для близких к границе значений
        int terms = 100; // достаточно для точности
        double actual = SecantFunction.sec(x, terms);
        double expected = referenceSec(x);

        // Проверяем, что результат конечный и положительный
        assertFalse(Double.isNaN(actual), "sec(1.5707) не должен быть NaN");
        assertTrue(actual > 0, "sec(1.5707) должен быть положительным");


        // Относительная погрешность
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-6,
                String.format("Погрешность: %e "+actual+" "+expected, relativeError));
    }

    @Test
    public void testSecNearNegativeBoundary() {
        double x = -1.56; // чуть больше -π/2
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.56) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearBoundaryFromBelow() {
        double x = 1.57; // очень близко к π/2 снизу
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(1.57) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearNegativeBoundaryFromAbove() {
        double x = -1.57; // очень близко к -π/2 сверху
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.57) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecAtExactPiOverTwo() {
        double x = Math.PI / 2;
        double result = SecantFunction.sec(x);
        assertTrue(Double.isNaN(result), "sec(π/2) должен вернуть NaN");
    }

    @Test
    public void testSecAtExactNegativePiOverTwo() {
        double x = -Math.PI / 2;
        double result = SecantFunction.sec(x);
        assertTrue(Double.isNaN(result), "sec(-π/2) должен вернуть NaN");
    }

    // Точки за пределами области определения

    @Test
    public void testSecJustBeyondPositiveBoundary() {
        double x = 1.58; // больше π/2
        double result = SecantFunction.sec(x);
        assertTrue(Double.isNaN(result), "sec(1.58) должен вернуть NaN");
    }

    @Test
    public void testSecJustBeyondNegativeBoundary() {
        double x = -1.58; // меньше -π/2
        double result = SecantFunction.sec(x);
        assertTrue(Double.isNaN(result), "sec(-1.58) должен вернуть NaN");
    }

    @Test
    public void testSecFarBeyondBoundary() {
        double[] largeValues = {10.0, 100.0, 999.0, 1000.0, -10.0, -100.0, -999.0, -1000.0, 1e6, -1e6};
        for (double x : largeValues) {
            double result = SecantFunction.sec(x);
            assertTrue(Double.isNaN(result),
                    String.format("sec(%f) должен вернуть NaN", x));
        }
    }

    // Особые и нечисловые значения

    @Test
    public void testSecWithNaN() {
        double result = SecantFunction.sec(Double.NaN);
        assertTrue(Double.isNaN(result), "sec(NaN) должен вернуть NaN");
    }

    @Test
    public void testSecWithInfinity() {
        assertTrue(Double.isNaN(SecantFunction.sec(Double.POSITIVE_INFINITY)), "sec(+∞) должен вернуть NaN");
        assertTrue(Double.isNaN(SecantFunction.sec(Double.NEGATIVE_INFINITY)), "sec(-∞) должен вернуть NaN");
    }

    // Проверка свойств функций

    @Test
    public void testSecEvenFunction() {
        double[] testPoints = {0.1, 0.5, 0.9, 1.2, 1.5};

        for (double x : testPoints) {
            if (Math.abs(x) < BOUNDARY) {
                double secPositive = SecantFunction.sec(x);
                double secNegative = SecantFunction.sec(-x);

                assertEquals(secPositive, secNegative, 1e-12,
                        String.format("sec(%f) должно равняться sec(-%f) (свойство четности)", x, x));

                assertEquals(referenceSec(x), secPositive, 1e-12,
                        String.format("sec(%f) должно совпадать с эталоном", x));
                assertEquals(referenceSec(-x), secNegative, 1e-12,
                        String.format("sec(%f) должно совпадать с эталоном", -x));
            }
        }
    }

    @Test
    public void testSecAtVariousPoints() {
        double[] testPoints = {
                -1.5, -1.3, -1.2, -1.1, -1.0, -0.9, -0.8, -0.7, -0.6,
                -0.5, -0.4, -0.3, -0.2, -0.1, 0.0, 0.1, 0.2, 0.3, 0.4,
                0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.5
        };

        for (double x : testPoints) {
            if (Math.abs(x) < BOUNDARY) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);
                assertEquals(expected, actual, 1e-12,
                        String.format("sec(%.2f) должен совпасть с эталоном", x));
            } else {
                double result = SecantFunction.sec(x);
                assertTrue(Double.isNaN(result),
                        String.format("sec(%.2f) должен вернуть NaN", x));
            }
        }
    }


    @Test
    public void testSecNearUnity() {
        double[] testPoints = {-1.0000001, 1.0000001, -0.9999999, 0.9999999};

        for (double x : testPoints) {
            if (Math.abs(x) < BOUNDARY) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);
                assertEquals(expected, actual, 1e-12,
                        String.format("sec(%f) должен совпадать с эталоном", x));
            } else {
                double result = SecantFunction.sec(x);
                assertTrue(Double.isNaN(result),
                        String.format("sec(%f) должен вернуть NaN", x));
            }
        }
    }

    @Test
    public void testExtensiveRandomValues() {
        Random random = new Random(42);
        int iterations = 10000;
        double min = -1.5;
        double max = 1.5;

        for (int i = 0; i < iterations; i++) {
            double x = min + (max - min) * random.nextDouble();

            // Пропускаем точки слишком близкие к границе
            if (Math.abs(Math.abs(x) - BOUNDARY) < 1e-8) {
                continue;
            }

            double expected = referenceSec(x);
            double actual = SecantFunction.sec(x);

            assertEquals(expected, actual, 1e-7,
                    String.format("Случайный тест: sec(%.15f) должен совпадать с эталоном", x));
        }
    }

    @Test
    public void testSecWithDifferentIterations() {
        double x = 0.8;

        double resultWithFewIter = SecantFunction.sec(x, 5);
        double resultWithManyIter = SecantFunction.sec(x, 20);
        double reference = referenceSec(x);

        assertNotEquals(resultWithFewIter, resultWithManyIter, 1e-10,
                "Результаты с разным числом итераций должны отличаться");

        assertTrue(Math.abs(resultWithManyIter - reference) < Math.abs(resultWithFewIter - reference),
                "Больше итераций должно давать более точный результат");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.3, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5})
    public void testSecParameterized(double x) {
        if (Math.abs(x) < BOUNDARY) {
            double expected = referenceSec(x);
            double actual = SecantFunction.sec(x);
            assertEquals(expected, actual, 1e-8,
                    String.format("sec(%f) должен совпадать с эталоном", x));
        } else {
            double result = SecantFunction.sec(x);
            assertTrue(Double.isNaN(result),
                    String.format("sec(%f) должен вернуть NaN", x));
        }
    }

    @Test
    public void testSecBoundaryValues() {
        // Тестирование значений, близких к границе с разных сторон
        double step = 1e-7;
        double justBelow = BOUNDARY - step;
        double justAbove = BOUNDARY + step;

        assertFalse(Double.isNaN(SecantFunction.sec(justBelow)),
                "sec чуть ниже границы должен быть определён");
        assertTrue(Double.isNaN(SecantFunction.sec(justAbove)),
                "sec чуть выше границы должен быть NaN");

        // Проверка симметрии для отрицательной границы
        double negativeJustAbove = -BOUNDARY + step;
        double negativeJustBelow = -BOUNDARY - step;

        assertFalse(Double.isNaN(SecantFunction.sec(negativeJustAbove)),
                "sec чуть выше -π/2 должен быть определён");
        assertTrue(Double.isNaN(SecantFunction.sec(negativeJustBelow)),
                "sec чуть ниже -π/2 должен быть NaN");
    }
}