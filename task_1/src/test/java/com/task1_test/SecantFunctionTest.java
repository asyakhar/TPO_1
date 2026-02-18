//package com.task1_test;
//
//import com.task1.SecantFunction;
//import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
//import java.util.Random;
//
//public class SecantFunctionTest {
//
//    private static final double EPSILON = 1e-10;
//
//    @Test
//    public void testSecAtZero() {
//
//        assertEquals(1.0, SecantFunction.sec(0.0), EPSILON, "sec(0) should be 1");
//    }
//
//    @Test
//    public void testSecAtOne() {
//
//        double expected = 1.8508157176809255;
//        assertEquals(expected, SecantFunction.sec(1.0), 1e-12, "sec(1) should be approximately 1.8508157176809255");
//    }
//
//    @Test
//    public void testSecAtNegativeHalf() {
//
//        double expected = 1.139493927324549;
//        assertEquals(expected, SecantFunction.sec(-0.5), 1e-12, "sec(-0.5) should be approximately 1.139493927324549");
//    }
//
//    @Test
//    public void testSecAtVerySmallValue() {
//
//        double x = 1e-6;
//        double expected = 1.0000000000005;
//        assertEquals(expected, SecantFunction.sec(x), 1e-13, "sec(0.000001) should be approximately 1.0000000000005");
//    }
//
//    @Test
//    public void testSecNearConvergenceBoundary() {
//
//        double x = 1.5707;
//        double result = SecantFunction.sec(x);
//        assertTrue(result > 1000, "sec(1.5707) should be very large positive number");
//    }
//
//    @Test
//    public void testSecNearNegativeBoundary() {
//
//        double x = -1.56;
//        double expected = 1 / Math.cos(x);
//        double result = SecantFunction.sec(x);
//
//        double delta = 0.0001;
//        assertEquals(expected, result, delta);
//    }
//
//    @Test
//    public void testSecAtPiOverTwo() {
//
//        double x = Math.PI / 2;
//        assertThrows(IllegalArgumentException.class, () -> {
//            SecantFunction.sec(x);
//        }, "sec(π/2) should throw IllegalArgumentException");
//    }
//
//    @Test
//    public void testSecBeyondConvergenceRadius() {
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            SecantFunction.sec(1.58);
//        }, "sec(1.58) should throw IllegalArgumentException");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            SecantFunction.sec(10.0);
//        }, "sec(10.0) should throw IllegalArgumentException");
//    }
//
//    @Test
//    public void testSecWithNaN() {
//
//        assertTrue(Double.isNaN(SecantFunction.sec(Double.NaN)),
//                "sec(NaN) should return NaN");
//    }
//
//    @Test
//    public void testSecWithInfinity() {
//
//        assertTrue(Double.isNaN(SecantFunction.sec(Double.POSITIVE_INFINITY)),
//                "sec(+∞) should return NaN");
//        assertTrue(Double.isNaN(SecantFunction.sec(Double.NEGATIVE_INFINITY)),
//                "sec(-∞) should return NaN");
//    }
//
//    @Test
//    public void testSecAtVariousPoints() {
//
//        double[] testPoints = {-1.0, -0.9, -0.8, -0.7, -0.6, -0.5, -0.4, -0.3, -0.2, -0.1,
//                0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
//
//        for (double x : testPoints) {
//            if (Math.abs(x) < Math.PI / 2) {
//                double expected = 1.0 / Math.cos(x);
//                double actual = SecantFunction.sec(x);
//                assertEquals(expected, actual, 1e-12,
//                        String.format("sec(%.1f) should match Math.cos calculation", x));
//            }
//        }
//    }
//
//    @Test
//    public void testSecPropertyBased() {
//
//        Random random = new Random(42);
//        int iterations = 1000000;
//        double min = -0.999;
//        double max = 0.999;
//
//        for (int i = 0; i < iterations; i++) {
//            double x = min + (max - min) * random.nextDouble();
//            double expected = 1.0 / Math.cos(x);
//            double actual = SecantFunction.sec(x);
//
//
//            assertEquals(expected, actual, 1e-12,
//                    String.format("sec(%.15f) should match Math.cos calculation", x));
//        }
//    }
//
//    @Test
//    public void testSecEvenFunction() {
//
//        double[] testPoints = {0.1, 0.5, 1.0, 1.2};
//
//        for (double x : testPoints) {
//            if (Math.abs(x) < Math.PI / 2) {
//                double secPositive = SecantFunction.sec(x);
//                double secNegative = SecantFunction.sec(-x);
//                assertEquals(secPositive, secNegative, 1e-12,
//                        String.format("sec(%f) should equal sec(-%f)", x, x));
//            }
//        }
//    }
//
//    @Test
//    public void testSecTaylorApproximation() {
//
//        double x = 0.5;
//        int terms = 5;
//
//        double taylorResult = SecantFunction.secTaylor(x, terms);
//        double exactResult = SecantFunction.sec(x);
//
//
//        assertTrue(Math.abs(taylorResult - exactResult) < 0.01,
//                "Taylor series should approximate sec(x)");
//    }
//
//    @Test
//    public void testSecAtSpecificValues() {
//
//        double[][] testCases = {
//                {0.25, 1.032085023983703},
//                {0.75, 1.366701124672226},
//                {-0.25, 1.032085023983703},
//                {-0.75, 1.366701124672226}
//        };
//
//        for (double[] testCase : testCases) {
//            double x = testCase[0];
//            double expected = testCase[1];
//            assertEquals(expected, SecantFunction.sec(x), 1e-12,
//                    String.format("sec(%.2f) should be %.15f", x, expected));
//        }
//    }
//}
package com.task1_test;

import com.task1.SecantFunction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class SecantFunctionTest {

    private static final double EPSILON = 1e-10;

    // Эталонная реализация для сравнения
    private static double referenceSec(double x) {
        return 1.0 / Math.cos(x);
    }

    @Test
    public void testSecAtZero() {
        double expected = referenceSec(0.0);
        assertEquals(expected, SecantFunction.sec(0.0), EPSILON, "sec(0) should be 1");
    }

    @Test
    public void testSecAtOne() {
        double expected = referenceSec(1.0);
        assertEquals(expected, SecantFunction.sec(1.0), 1e-12, "sec(1) should match reference implementation");
    }

    @Test
    public void testSecAtNegativeHalf() {
        double expected = referenceSec(-0.5);
        assertEquals(expected, SecantFunction.sec(-0.5), 1e-12, "sec(-0.5) should match reference implementation");
    }

    @Test
    public void testSecAtVerySmallValue() {
        double x = 1e-6;
        double expected = referenceSec(x);
        assertEquals(expected, SecantFunction.sec(x), 1e-13, "sec(0.000001) should match reference implementation");
    }

    @Test
    public void testSecNearConvergenceBoundary() {
        double x = 1.5707; // близко к π/2
        double expected = referenceSec(x);
        double result = SecantFunction.sec(x);

        // Для больших значений используем относительную погрешность
        double relativeError = Math.abs((result - expected) / expected);
        assertTrue(relativeError < 1e-10,
                String.format("sec(1.5707) should be close to reference value. Expected: %f, Actual: %f", expected, result));
    }

    @Test
    public void testSecNearNegativeBoundary() {
        double x = -1.56; // близко к -π/2
        double expected = referenceSec(x);
        double result = SecantFunction.sec(x);

        // Для больших значений используем относительную погрешность
        double relativeError = Math.abs((result - expected) / expected);
        assertTrue(relativeError < 1e-10,
                String.format("sec(-1.56) should be close to reference value. Expected: %f, Actual: %f", expected, result));
    }

    @Test
    public void testSecAtPiOverTwo() {
        double x = Math.PI / 2;
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(x);
        }, "sec(π/2) should throw IllegalArgumentException");
    }

    @Test
    public void testSecBeyondConvergenceRadius() {
        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(1.58);
        }, "sec(1.58) should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            SecantFunction.sec(10.0);
        }, "sec(10.0) should throw IllegalArgumentException");
    }

    @Test
    public void testSecWithNaN() {
        assertTrue(Double.isNaN(SecantFunction.sec(Double.NaN)),
                "sec(NaN) should return NaN");
    }

    @Test
    public void testSecWithInfinity() {
        assertTrue(Double.isNaN(SecantFunction.sec(Double.POSITIVE_INFINITY)),
                "sec(+∞) should return NaN");
        assertTrue(Double.isNaN(SecantFunction.sec(Double.NEGATIVE_INFINITY)),
                "sec(-∞) should return NaN");
    }

    @Test
    public void testSecAtVariousPoints() {
        double[] testPoints = {-1.0, -0.9, -0.8, -0.7, -0.6, -0.5, -0.4, -0.3, -0.2, -0.1,
                0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};

        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double expected = referenceSec(x);
                double actual = SecantFunction.sec(x);
                assertEquals(expected, actual, 1e-12,
                        String.format("sec(%.1f) should match reference implementation", x));
            }
        }
    }

    @Test
    public void testSecPropertyBased() {
        Random random = new Random(42);
        int iterations = 1000000;
        double min = -1.5; // Чуть меньше π/2 ≈ 1.57
        double max = 1.5;  // Чуть меньше π/2 ≈ 1.57

        for (int i = 0; i < iterations; i++) {
            double x = min + (max - min) * random.nextDouble();

            // Пропускаем значения, слишком близкие к границам
            if (Math.abs(Math.abs(x) - Math.PI/2) < 1e-10) {
                continue;
            }

            double expected = referenceSec(x);
            double actual = SecantFunction.sec(x);

            assertEquals(expected, actual, 1e-12,
                    String.format("sec(%.15f) should match reference implementation", x));
        }
    }

    @Test
    public void testSecEvenFunction() {
        double[] testPoints = {0.1, 0.5, 1.0, 1.2};

        for (double x : testPoints) {
            if (Math.abs(x) < Math.PI / 2) {
                double secPositive = SecantFunction.sec(x);
                double secNegative = SecantFunction.sec(-x);
                double refPositive = referenceSec(x);
                double refNegative = referenceSec(-x);

                // Проверяем, что оба значения соответствуют эталону
                assertEquals(refPositive, secPositive, 1e-12,
                        String.format("sec(%f) should match reference", x));
                assertEquals(refNegative, secNegative, 1e-12,
                        String.format("sec(%f) should match reference", -x));

                // Проверяем свойство четности
                assertEquals(secPositive, secNegative, 1e-12,
                        String.format("sec(%f) should equal sec(-%f)", x, x));
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

        // Проверяем, что основная функция совпадает с эталоном
        assertEquals(referenceResult, exactResult, 1e-12,
                "sec() should match reference implementation");

        // Проверяем, что ряд Тейлора дает приближение
        assertTrue(Math.abs(taylorResult - referenceResult) < 0.01,
                "Taylor series should approximate sec(x)");
    }

    @Test
    public void testSecAtSpecificValues() {
        double[][] testCases = {
                {0.25, 0}, // Второе значение будет вычислено через referenceSec
                {0.75, 0},
                {-0.25, 0},
                {-0.75, 0}
        };

        for (double[] testCase : testCases) {
            double x = testCase[0];
            double expected = referenceSec(x);
            assertEquals(expected, SecantFunction.sec(x), 1e-12,
                    String.format("sec(%.2f) should match reference implementation", x));
        }
    }
}