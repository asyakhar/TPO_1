package com.task1_test;

import com.task1.SecantFunction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class SecantFunctionTest {

    private static final double EPSILON = 1e-10;

    // Эталон для сравнения
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
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);

        assertTrue(actual > 1000, "sec(1.5707) должен быть большим позитивным числом");

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(1.5707) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearNegativeBoundary() {
        double x = -1.56;

        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        System.out.println(actual);

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.56) должен совпасть с эталоном." + actual + expected +" Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearBoundaryFromBelow() {
        double x = 1.57;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);


        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(1.57) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearNegativeBoundaryFromAbove() {
        double x = -1.57;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        System.out.println(actual);
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.57) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecAtExactPiOverTwo() {
        double x = Math.PI / 2;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(π/2) должен выбросить исключение IllegalArgumentException");
    }

    @Test
    public void testSecAtExactNegativePiOverTwo() {
        double x = -Math.PI / 2;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(-π/2) должен выбросить исключение IllegalArgumentException");
    }

    // Точки за пределами радиуса сходимости

    @Test
    public void testSecJustBeyondPositiveBoundary() {
        double x = 1.58;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(1.58) должен выбросить исключение IllegalArgumentException");
    }

    @Test
    public void testSecJustBeyondNegativeBoundary() {
        double x = -1.58;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(-1.58) должен выбросить исключение IllegalArgumentException");
    }

    @Test
    public void testSecFarBeyondBoundary() {
        double[] largeValues = {10.0, 100.0, 999.0, 1000.0, -10.0, -100.0, -999.0, -1000.0, 1e6, -1e6};
        for (double x : largeValues) {
            assertThrows(IllegalArgumentException.class, () -> {
                SecantFunction.sec(x);
            }, String.format("sec(%f) должен выбросить исключение IllegalArgumentException", x));
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
            if (Math.abs(x) < Math.PI / 2) {
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
                -1.5, -1.4, -1.3, -1.2, -1.1, -1.0, -0.9, -0.8, -0.7, -0.6,
                -0.5, -0.4, -0.3, -0.2, -0.1, 0.0, 0.1, 0.2, 0.3, 0.4,
                0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5
        };

        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);
                assertEquals(expected, actual, 1e-12,
                        String.format("sec(%.2f) должен совпасть с эталоном", x));
            } else {
                final double xFinal = x;
                assertThrows(IllegalArgumentException.class, () -> {
                    SecantFunction.sec(xFinal);
                }, String.format("sec(%.2f) должен выбросить исключение IllegalArgumentException", x));
            }
        }
    }

    @Test
    public void testSecTaylorApproximation() {
        double x = 0.5;
        int terms = 5;

        double taylorResult = SecantFunction.secTaylor(x, terms);
        double exactResult = SecantFunction.sec(x);
        double referenceResult = referenceSec(x);

        assertEquals(referenceResult, exactResult, 1e-12, "sec() должен совпасть с эталоном");

        double error = Math.abs(taylorResult - exactResult);
        assertTrue(error < 0.01,
                String.format("Ряд Тейлора с %d членами должен приближать sec(0.5). Погрешность: %e", terms, error));
    }

    @Test
    public void testSecTaylorWithMoreTerms() {
        double x = 1.0;
        int terms = 10;

        double taylorResult = SecantFunction.secTaylor(x, terms);
        double exactResult = SecantFunction.sec(x);
        double referenceResult = referenceSec(x);

        assertEquals(referenceResult, exactResult, 1e-12, "sec() должен совпадать с эталоном");

        double error = Math.abs(taylorResult - exactResult);
        assertTrue(error < 0.001,
                String.format("Ряд Тейлора с %d членами должен быть точным для x=1.0. Погрешность: %e", terms, error));
    }




    @Test
    public void testSecNearUnity() {

        double[] testPoints = {-1.0000001, 1.0000001, -0.9999999, 0.9999999};

        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);
                assertEquals(expected, actual, 1e-12,
                        String.format("sec(%f) должен совпадать с эталоном", x));
            } else {
                final double xFinal = x;
                assertThrows(IllegalArgumentException.class, () -> {
                    SecantFunction.sec(xFinal);
                }, String.format("sec(%f) должно выбрасывать IllegalArgumentException", x));
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


            if (Math.abs(Math.abs(x) - Math.PI/2) < 1e-8) {
                continue;
            }

            double expected = referenceSec(x);
            double actual = SecantFunction.sec(x);

            assertEquals(expected, actual, 1e-12,
                    String.format("Случайный тест: sec(%.15f) должен совпадать с эталоном", x));
        }
    }
}