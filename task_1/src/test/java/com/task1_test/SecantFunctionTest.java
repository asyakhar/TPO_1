package com.task1_test;

import com.task1.SecantFunction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;


public class SecantFunctionTest {

    private static final double EPSILON = 1e-10;
    private static final double BOUNDARY = Math.PI / 2;

    private static final double[][] REFERENCE_VALUES = {
            // x, expected sec(x)
            {0.0, 1.0},
            {-0.0, 1.0},
            {1.0, 1.8508157176809255},
            {-1.0, 1.8508157176809255},
            {0.5, 1.13949392732455},
            {-0.5, 1.13949392732455},
            {0.000001, 1.0000000000005},
            {Double.MIN_VALUE, 1.0},
            {1.5707, 10381.3274657332},
            {-1.56, 92.6258945325371},
            {1.57, 1255.76598966421},
            {-1.57, 1255.76598966421},
            {0.1, 1.0050209184004555},
            {-0.1, 1.0050209184004555},
            {0.2, 1.02033884494119},
            {-0.2, 1.02033884494119},
            {0.3, 1.04675160153809},
            {-0.3, 1.04675160153809},
            {0.4, 1.08570442838324},
            {-0.4, 1.08570442838324},
            {0.6, 1.21162831451232},
            {-0.6, 1.21162831451232},
            {0.7, 1.30745925973359},
            {-0.7, 1.30745925973359},
            {0.8, 1.43532419967224},
            {-0.8, 1.43532419967224},
            {0.9,1.60872581046605},
            {-0.9, 1.60872581046605},
            {1.1, 2.20460438871736},
            {-1.1, 2.20460438871736},
            {1.2, 2.75970360133241},
            {-1.2, 2.75970360133241},
            {1.3, 3.73833412707544},
            {-1.3, 3.73833412707544},
            {1.4, 5.88349008482734},
            {-1.4, 5.88349008482734},
            {1.5, 14.1368329029699},
            {-1.5, 14.1368329029699},
            {-1.0000001, 1.85081600592845},
            {1.0000001, 1.85081600592845},
            {-0.9999999, 1.85081542943351},
            {0.9999999, 1.85081542943351}

    };


    private static double getReferenceValue(double x) {
        for (double[] ref : REFERENCE_VALUES) {

            if (Math.abs(ref[0] - x) < 1e-15) {
                return ref[1];
            }

            if (Double.doubleToLongBits(ref[0]) == Double.doubleToLongBits(x)) {
                return ref[1];
            }
        }
        throw new IllegalArgumentException("Нет эталонного значения для x = " + x);
    }



    // Значения внутри радиуса сходимости

    @Test
    public void testSecAtZero() {
        double x = 0.0;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, EPSILON, "sec(0) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtNegativeZero() {
        double x = -0.0;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, EPSILON, "sec(-0) должен совпасть с эталоном");

        assertEquals(SecantFunction.sec(0.0), SecantFunction.sec(-0.0), EPSILON, "sec должен быть четным");
    }

    @Test
    public void testSecAtOne() {
        double x = 1.0;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(1) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtNegativeHalf() {
        double x = -0.5;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(-0.5) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtPositiveHalf() {
        double x = 0.5;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-12, "sec(0.5) должен совпасть с эталоном");
        assertEquals(SecantFunction.sec(0.5), SecantFunction.sec(-0.5), 1e-12, "sec должен быть четным");
    }

    @Test
    public void testSecAtVerySmallValue() {
        double x = 0.000001;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-13, "sec(0.000001) должен совпасть с эталоном");
    }

    @Test
    public void testSecAtMinValue() {
        double x = Double.MIN_VALUE;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);
        assertEquals(expected, actual, 1e-300, "sec(Double.MIN_VALUE) должен совпасть с эталоном");
    }

    // Точки, близкие к границе сходимости

    @Test
    public void testSecNearPositiveBoundary() {
        double x = 1.5707;
        int terms = 100;
        double actual = SecantFunction.sec(x, terms);
        double expected = getReferenceValue(x);

        assertFalse(Double.isNaN(actual), "sec(1.5707) не должен быть NaN");
        assertTrue(actual > 0, "sec(1.5707) должен быть положительным");

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-6,
                String.format("Погрешность: %e, actual: %f, expected: %f", relativeError, actual, expected));
    }

    @Test
    public void testSecNearNegativeBoundary() {
        double x = -1.56;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(-1.56) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearBoundaryFromBelow() {
        double x = 1.57;
        double expected = getReferenceValue(x);
        double actual = SecantFunction.sec(x);

        double relativeError = Math.abs((actual - expected) / expected);
        assertTrue(relativeError < 1e-8,
                String.format("sec(1.57) должен совпасть с эталоном. Относительная погрешность: %e", relativeError));
    }

    @Test
    public void testSecNearNegativeBoundaryFromAbove() {
        double x = -1.57;
        double expected = getReferenceValue(x);
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
        double x = 1.58;
        double result = SecantFunction.sec(x);
        assertTrue(Double.isNaN(result), "sec(1.58) должен вернуть NaN");
    }

    @Test
    public void testSecJustBeyondNegativeBoundary() {
        double x = -1.58;
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

                assertEquals(getReferenceValue(x), secPositive, 1e-12,
                        String.format("sec(%f) должно совпадать с эталоном", x));
                assertEquals(getReferenceValue(-x), secNegative, 1e-12,
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
            if (Math.abs(x) < BOUNDARY) {
                double expected = getReferenceValue(x);
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
                double expected = getReferenceValue(x);
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

    public void testSecWithDifferentIterations() {
        double x = 0.8;

        double resultWithFewIter = SecantFunction.sec(x, 5);
        double resultWithManyIter = SecantFunction.sec(x, 20);
        double reference = getReferenceValue(x);

        assertNotEquals(resultWithFewIter, resultWithManyIter, 1e-10,
                "Результаты с разным числом итераций должны отличаться");

        assertTrue(Math.abs(resultWithManyIter - reference) < Math.abs(resultWithFewIter - reference),
                "Больше итераций должно давать более точный результат");
    }

    @ParameterizedTest
    @CsvSource({
            "0.1, 1.0050209184004555",
            "0.3, 1.04675160153809",
            "0.5, 1.139493927320549",
            "0.7, 1.30745925973359",
            "0.9, 1.60872581046605",
            "1.1, 2.20460438871736",
            "1.3, 3.73833412707544",
            "1.5, 14.1368329029699"
    })
    public void testSecParameterized(double x, double expected) {
        if (Math.abs(x) < BOUNDARY) {
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
        double step = 1e-7;
        double justBelow = BOUNDARY - step;
        double justAbove = BOUNDARY + step;

        assertFalse(Double.isNaN(SecantFunction.sec(justBelow)),
                "sec чуть ниже границы должен быть определён");
        assertTrue(Double.isNaN(SecantFunction.sec(justAbove)),
                "sec чуть выше границы должен быть NaN");

        double negativeJustAbove = -BOUNDARY + step;
        double negativeJustBelow = -BOUNDARY - step;

        assertFalse(Double.isNaN(SecantFunction.sec(negativeJustAbove)),
                "sec чуть выше -π/2 должен быть определён");
        assertTrue(Double.isNaN(SecantFunction.sec(negativeJustBelow)),
                "sec чуть ниже -π/2 должен быть NaN");
    }


}