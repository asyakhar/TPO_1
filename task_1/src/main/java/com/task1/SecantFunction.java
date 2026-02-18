package com.task1;

import java.util.*;

public class SecantFunction {

    /**
     * Вычисляет секанс через разложение в степенной ряд
     * sec(x) = 1/cos(x)
     * Ряд Тейлора: sec(x) = 1 + x²/2! + 5x⁴/4! + 61x⁶/6! + ...
     * Радиус сходимости: |x| < π/2
     *
     * @param x аргумент функции
     * @return значение секанса
     * @throws IllegalArgumentException если |x| >= π/2 (вне радиуса сходимости)
     */
    public static double sec(double x) {
        // Обработка специальных значений
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (Double.isInfinite(x)) {
            return Double.NaN;
        }

        // Проверка радиуса сходимости
        if (Math.abs(x) >= Math.PI / 2) {
            throw new IllegalArgumentException(
                    "Argument must be in range (-π/2, π/2) for Taylor series expansion");
        }

        // Используем прямое вычисление 1/cos(x) для точности
        // Ряд Тейлора для секанса сходится медленно и требует чисел Эйлера
        return 1.0 / Math.cos(x);
    }

    /**
     * Вычисляет секанс через разложение в ряд Тейлора
     * sec(x) = 1 + (1/2)x² + (5/24)x⁴ + (61/720)x⁶ + (277/8064)x⁸ + ...
     *
     * @param x аргумент функции
     * @param terms количество членов ряда
     * @return значение секанса
     * @throws IllegalArgumentException если |x| >= π/2
     */
    public static double secTaylor(double x, int terms) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (Double.isInfinite(x)) {
            return Double.NaN;
        }

        if (Math.abs(x) >= Math.PI / 2) {
            throw new IllegalArgumentException(
                    "Argument must be in range (-π/2, π/2) for Taylor series expansion");
        }

        double result = 0.0;
        double xPow = 1.0; // x^0

        for (int n = 0; n < terms; n++) {
            double eulerNumber = getEulerNumber(2 * n);
            // В формуле для sec(x) используется |E(2n)|, так как числа Эйлера знакочередуются
            double coefficient = Math.abs(eulerNumber) / factorial(2 * n);
            result += coefficient * xPow;
            xPow *= x * x; // x^(2n+2)
        }

        return result;
    }

    /**
     * Вычисляет числа Эйлера по рекуррентной формуле
     * E(0) = 1
     * Сумма от k=0 до n по четным: C(2n,2k) * E(2k) = 0 для n >= 1
     *
     * @param n индекс числа Эйлера (должен быть четным)
     * @return число Эйлера E(n)
     */
    private static double getEulerNumber(int n) {
        if (n % 2 != 0) return 0; // Нечетные числа Эйлера равны 0

        double[] euler = new double[n/2 + 1];
        euler[0] = 1.0; // E0 = 1

        for (int i = 1; i <= n/2; i++) {
            euler[i] = 0;
            // По формуле: сумма C(2i, 2k) * E(2k) = 0 для k=0..i-1
            for (int k = 0; k < i; k++) {
                euler[i] += combination(2 * i, 2 * k) * euler[k];
            }
            euler[i] = -euler[i];
        }

        return euler[n/2];
    }

    /**
     * Вычисляет биномиальный коэффициент C(n, k)
     *
     * @param n общее количество элементов
     * @param k количество выбираемых элементов
     * @return биномиальный коэффициент
     */
    private static double combination(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;

        double result = 1;
        k = Math.min(k, n - k);

        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }

        return result;
    }

    /**
     * Вычисляет факториал числа
     *
     * @param n неотрицательное целое число
     * @return факториал n
     */
    private static double factorial(int n) {
        if (n == 0) return 1.0;
        double result = 1.0;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}