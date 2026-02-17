package com.task1_test;

import com.task1.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

     
    @ParameterizedTest(name = "sec({0})")
    @DisplayName("Проверка граничных значений")
    @ValueSource(doubles = {
            -1000.0, -10.0, -Math.PI, -2.0, -1.0, -0.5, -0.1,
            -0.0, 0.0,
            0.1, 0.5, 1.0, 2.0, Math.PI, 10.0, 1000.0,
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY
    })
    void testBoundaryValues(double x) {
        double expected = 1.0 / Math.cos(x);
        double actual = Function.sec(x);

         
        if (Double.isNaN(expected) || Double.isInfinite(expected)) {
            assertTrue(Double.isNaN(actual),
                    "Для x = " + x + " ожидается NaN, получено: " + actual);
        } else {
            assertEquals(expected, actual, 1e-8,
                    "Несовпадение для x = " + x);
        }
    }

     
    @Test
    @DisplayName("Проверка точек разрыва")
    void testDiscontinuities() {
        double[] discontinuities = {
                -Math.PI/2, Math.PI/2, 3*Math.PI/2, -3*Math.PI/2,
                Math.PI/2 + 2*Math.PI, Math.PI/2 - 2*Math.PI
        };

        for (double x : discontinuities) {
            assertTrue(Double.isNaN(Function.sec(x)),
                    "В точке x = " + x + " должна быть неопределенность (NaN)");
        }
    }

     
    @Test
    @DisplayName("Проверка вблизи точек разрыва")
    void testNearDiscontinuities() {
        double[] centers = {-Math.PI/2, Math.PI/2, 3*Math.PI/2};
        double epsilon = 1e-10;

        for (double center : centers) {
            double x1 = center + epsilon;
            double x2 = center - epsilon;

            double actual1 = Function.sec(x1);
            double actual2 = Function.sec(x2);
            double expected1 = 1.0 / Math.cos(x1);
            double expected2 = 1.0 / Math.cos(x2);

             
            assertTrue(Math.abs(actual1) > 1e9 || Double.isNaN(actual1),
                    "Ожидается большое значение или NaN для x = " + x1);

             
            if (!Double.isNaN(actual1)) {
                assertEquals(expected1, actual1, 1e-5,
                        "Несовпадение для x = " + x1);
            }
        }
    }

     
    @Test
    @DisplayName("Проверка четности")
    void testEvenFunction() {
        double[] testValues = {0.1, 0.5, 0.8, 1.0, 1.2, 1.5, 2.0};

        for (double x : testValues) {
            double secX = Function.sec(x);
            double secMinusX = Function.sec(-x);

             
            if (!Double.isNaN(secX) && !Double.isNaN(secMinusX)) {
                assertEquals(secX, secMinusX, 1e-10,
                        "Функция должна быть четной для x = " + x);
            }
        }
    }

     
    @Test
    @DisplayName("Проверка периодичности")
    void testPeriodicity() {
        double[] testValues = {0.2, 0.7, 1.3, 1.8};
        double period = 2 * Math.PI;

        for (double x : testValues) {
            double sec1 = Function.sec(x);
            double sec2 = Function.sec(x + period);
            double sec3 = Function.sec(x + 2 * period);
            double sec4 = Function.sec(x - period);

             
            if (!Double.isNaN(sec1)) {
                assertAll(
                        () -> assertEquals(sec1, sec2, 1e-8, "Период 2π для x = " + x),
                        () -> assertEquals(sec1, sec3, 1e-8, "Период 4π для x = " + x),
                        () -> assertEquals(sec1, sec4, 1e-8, "Отрицательный период для x = " + x)
                );
            }
        }
    }

     
    @Test
    @DisplayName("Проверка основных значений")
    void testKeyValues() {
         
        double[][] testPairs = {
                {0.0, 1.0 / Math.cos(0.0)},
                {0.2, 1.0 / Math.cos(0.2)},
                {0.4, 1.0 / Math.cos(0.4)},
                {0.6, 1.0 / Math.cos(0.6)},
                {0.8, 1.0 / Math.cos(0.8)},
                {1.0, 1.0 / Math.cos(1.0)},
                {1.2, 1.0 / Math.cos(1.2)},
                {1.4, 1.0 / Math.cos(1.4)},
                {-0.2, 1.0 / Math.cos(-0.2)},
                {-0.4, 1.0 / Math.cos(-0.4)},
                {-0.6, 1.0 / Math.cos(-0.6)},
                {-0.8, 1.0 / Math.cos(-0.8)},
                {-1.0, 1.0 / Math.cos(-1.0)},
                {-1.2, 1.0 / Math.cos(-1.2)},
                {-1.4, 1.0 / Math.cos(-1.4)}
        };

        for (double[] pair : testPairs) {
            double x = pair[0];
            double expected = pair[1];
            double actual = Function.sec(x);

            assertEquals(expected, actual, 1e-8,
                    "Несовпадение для x = " + x);
        }
    }

     
    @Test
    @DisplayName("Проверка первых членов ряда")
    void testSeriesTerms() {
        double x = 0.3;
        double x2 = x * x;
        double x4 = x2 * x2;
        double x6 = x4 * x2;

         
        double term0 = 1.0;
        double term1 = x2 / 2.0;
        double term2 = 5.0 * x4 / 24.0;
        double term3 = 61.0 * x6 / 720.0;

        double expected1 = term0;
        double expected2 = term0 + term1;
        double expected3 = term0 + term1 + term2;
        double expected4 = term0 + term1 + term2 + term3;

         
        assertEquals(expected1, Function.sec(x, 1), 1e-8, "1 член");
        assertEquals(expected2, Function.sec(x, 2), 1e-6, "2 члена");
        assertEquals(expected3, Function.sec(x, 3), 1e-6, "3 члена");
        assertEquals(expected4, Function.sec(x, 4), 1e-8, "4 члена");
    }

     
    @Test
    @DisplayName("Проверка сходимости")
    void testConvergence() {
        double x = 0.2;  
        double exact = 1.0 / Math.cos(x);

        double prevError = Double.MAX_VALUE;

        for (int terms = 1; terms <= 8; terms++) {
            double approx = Function.sec(x, terms);
            double error = Math.abs(exact - approx);

            if (terms > 1) {
                assertTrue(error < prevError * 1.5,  
                        "Ошибка должна уменьшаться: terms=" + terms +
                                ", error=" + error + ", prevError=" + prevError);
            }

            prevError = error;
        }
    }

     
    @Test
    @DisplayName("Fuzzing тестирование")
    void testRandomValues() {
        int testCount = 10000;
        int failures = 0;
        double maxDiff = 0;
        double worstX = 0;

        for (int i = 0; i < testCount; i++) {
             
            double x;
            do {
                x = ThreadLocalRandom.current().nextDouble(-5, 5);
            } while (Math.abs(Math.cos(x)) < 1e-6);

            double expected = 1.0 / Math.cos(x);
            double actual = Function.sec(x);
            double diff = Math.abs(expected - actual);

            if (diff > 1e-6) {
                failures++;
                if (diff > maxDiff) {
                    maxDiff = diff;
                    worstX = x;
                }
            }
        }

        double failureRate = (failures * 100.0) / testCount;
        System.out.printf("Fuzzing тест: проверено %d, ошибок %d (%.2f%%), макс. отклонение %.2e при x = %.4f%n",
                testCount, failures, failureRate, maxDiff, worstX);

        assertTrue(failureRate < 1.0,
                "Слишком много ошибок: " + failureRate + "%");
    }

     
    @Test
    @DisplayName("Проверка специальных значений")
    void testSpecialValues() {
        double[][] specials = {
                {Math.PI/3, 2.0},                     
                {Math.PI/4, Math.sqrt(2)},             
                {Math.PI/6, 2.0/Math.sqrt(3)},         
                {0.0, 1.0}                             
        };

        for (double[] pair : specials) {
            double x = pair[0];
            double expected = pair[1];
            double actual = Function.sec(x);

            assertEquals(expected, actual, 1e-8,
                    "Специальное значение для x = " + x);
        }
    }
}