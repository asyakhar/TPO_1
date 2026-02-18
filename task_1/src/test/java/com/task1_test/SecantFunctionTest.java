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
        assertEquals(expected, actual, 1e-12, "sec(1) should match reference");
    }

    @Test
    public void testSecAtNegativeHalf() {
        double x = -0.5;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(-0.5) should match reference");
    }

    @Test
    public void testSecAtPositiveHalf() {
        double x = 0.5;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(0.5) should match reference");
        // Проверка четности
        assertEquals(SecantFunction.sec(0.5), SecantFunction.sec(-0.5), 1e-12, "sec should be even");
    }

    @Test
    public void testSecAtVerySmallValue() {
        double x = 0.000001;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-13, "sec(0.000001) should match reference");
    }

    @Test
    public void testSecAtMinValue() {
        double x = Double.MIN_VALUE;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-300, "sec(Double.MIN_VALUE) should match reference");
    }

    // Категория 2: Точки, близкие к границе сходимости (|x| < π/2, но близко)

    @Test
    public void testSecNearPositiveBoundary() {
        double x = 1.5707; // чуть меньше π/2
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);

        // Проверка что значение большое
        assertTrue(actual > 1000, "sec(1.5707) should be very large positive number");

        // Для больших значений используем относительную погрешность
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(1.5707) should match reference. Relative error: %e", relativeError));
    }

    @Test
    public void testSecNearNegativeBoundary() {
        double x = -1.56; // близко к -π/2
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        System.out.println(actual);

        // Для больших значений используем относительную погрешность
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.56) should match reference. Relative error: %e", relativeError));
    }

    @Test
    public void testSecNearBoundaryFromBelow() {
        double x = 1.57; // близко к π/2, но меньше
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);

        assertTrue(actual > 1000, "sec(1.57) should be very large");

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(1.57) should match reference. Relative error: %e", relativeError));
    }

    @Test
    public void testSecNearNegativeBoundaryFromAbove() {
        double x = -1.57;
        double expected = referenceSec(x);
        double actual = SecantFunction.sec(x);
        System.out.println(actual);
        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.57) should match reference. Relative error: %e", relativeError));
    }

    @Test
    public void testSecAtExactPiOverTwo() {
        double x = Math.PI / 2;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(π/2) should throw IllegalArgumentException");
    }

    @Test
    public void testSecAtExactNegativePiOverTwo() {
        double x = -Math.PI / 2;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(-π/2) should throw IllegalArgumentException");
    }

    // Категория 3: Точки за пределами радиуса сходимости (|x| > π/2)

    @Test
    public void testSecJustBeyondPositiveBoundary() {
        double x = 1.58; // чуть больше π/2
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(1.58) should throw IllegalArgumentException");
    }

    @Test
    public void testSecJustBeyondNegativeBoundary() {
        double x = -1.58; // чуть меньше -π/2
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(-1.58) should throw IllegalArgumentException");
    }

    @Test
    public void testSecFarBeyondBoundary() {
        double[] largeValues = {10.0, 100.0, 999.0, 1000.0, -10.0, -100.0, -999.0, -1000.0, 1e6, -1e6};
        for (double x : largeValues) {
            assertThrows(IllegalArgumentException.class, () -> {
                SecantFunction.sec(x);
            }, String.format("sec(%f) should throw IllegalArgumentException", x));
        }
    }

    // Категория 4: Особые и нечисловые значения

    @Test
    public void testSecWithNaN() {
        double result = SecantFunction.sec(Double.NaN);
        assertTrue(Double.isNaN(result), "sec(NaN) should return NaN");
    }

    @Test
    public void testSecWithInfinity() {
        assertTrue(Double.isNaN(SecantFunction.sec(Double.POSITIVE_INFINITY)), "sec(+∞) should return NaN");
        assertTrue(Double.isNaN(SecantFunction.sec(Double.NEGATIVE_INFINITY)), "sec(-∞) should return NaN");
    }

    // Дополнительные тесты для проверки свойств функции

    @Test
    public void testSecEvenFunction() {
        double[] testPoints = {0.1, 0.5, 0.9, 1.2, 1.5};
        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double secPositive = SecantFunction.sec(x);
                double secNegative = SecantFunction.sec(-x);
                assertEquals(secPositive, secNegative, 1e-12,
                        String.format("sec(%f) should equal sec(-%f) (even function property)", x, x));

                // Также проверяем с эталоном
                assertEquals(referenceSec(x), secPositive, 1e-12,
                        String.format("sec(%f) should match reference", x));
                assertEquals(referenceSec(-x), secNegative, 1e-12,
                        String.format("sec(%f) should match reference", -x));
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
                        String.format("sec(%.2f) should match reference", x));
            } else {
                final double xFinal = x;
                assertThrows(IllegalArgumentException.class, () -> {
                    SecantFunction.sec(xFinal);
                }, String.format("sec(%.2f) should throw IllegalArgumentException", x));
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

        // Проверяем что основная функция совпадает с эталоном
        assertEquals(referenceResult, exactResult, 1e-12, "sec() should match reference");

        // Проверяем что ряд Тейлора приближается к точному значению
        double error = Math.abs(taylorResult - exactResult);
        assertTrue(error < 0.01,
                String.format("Taylor series with %d terms should approximate sec(0.5). Error: %e", terms, error));
    }

    @Test
    public void testSecTaylorWithMoreTerms() {
        double x = 1.0;
        int terms = 10;

        double taylorResult = SecantFunction.secTaylor(x, terms);
        double exactResult = SecantFunction.sec(x);
        double referenceResult = referenceSec(x);

        // Проверяем что основная функция совпадает с эталоном
        assertEquals(referenceResult, exactResult, 1e-12, "sec() should match reference");

        // Проверяем что ряд Тейлора с 8 членами достаточно точен
        double error = Math.abs(taylorResult - exactResult);
        assertTrue(error < 0.001,
                String.format("Taylor series with %d terms should be accurate for x=1.0. Error: %e", terms, error));
    }

    @Test
    public void testSecAtSpecificValuesFromSpec() {
        // Тестирование конкретных значений из спецификации с сравнением с эталоном
        double[] testPoints = {
                0.0, 1.0, -0.5, 0.5, 0.000001, -0.000001,
                1.5707, -1.56, 1.57, -1.57
        };

        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);

                // Для обычных значений используем абсолютную погрешность
                if (Math.abs(expected) < 1000) {
                    assertEquals(expected, actual, 1e-12,
                            String.format("sec(%f) should match reference", x));
                } else {
                    // Для больших значений используем относительную погрешность
                    double relativeError = Math.abs((actual - expected) / expected);
                    assertTrue(relativeError < 1e-8,
                            String.format("sec(%f) should match reference. Relative error: %e", x, relativeError));
                }
            }
        }
    }

    @Test
    public void testSecBoundaryValues() {

        double[] boundaryPoints = {-1.0, 1.0, -0.99, 0.99, -0.5, 0.5, -0.000001, 0.000001};

        for (double x : boundaryPoints) {
            double expected = referenceSec(x);
            double actual = SecantFunction.sec(x);
            assertEquals(expected, actual, 1e-12,
                    String.format("sec(%f) should match reference", x));
        }
    }

    @Test
    public void testSecNearUnity() {
        double[] testPoints = {-1.0000001, 1.0000001, -0.9999999, 0.9999999};

        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);
                assertEquals(expected, actual, 1e-12,
                        String.format("sec(%f) should match reference", x));
            } else {
                final double xFinal = x;
                assertThrows(IllegalArgumentException.class, () -> {
                    SecantFunction.sec(xFinal);
                }, String.format("sec(%f) should throw IllegalArgumentException", x));
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

            // Пропускаем значения, слишком близкие к границам
            if (Math.abs(Math.abs(x) - Math.PI/2) < 1e-8) {
                continue;
            }

            double expected = referenceSec(x);
            double actual = SecantFunction.sec(x);

            assertEquals(expected, actual, 1e-12,
                    String.format("Random test: sec(%.15f) should match reference", x));
        }
    }
}