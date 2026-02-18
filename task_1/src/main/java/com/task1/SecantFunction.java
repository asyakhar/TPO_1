package com.task1;


public class SecantFunction {

    public static double sec(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (Double.isInfinite(x)) {
            return Double.NaN;
        }

        if (Math.abs(x) >= Math.PI / 2) {
            throw new IllegalArgumentException(
                    "Аргумент должен быть в интервале (-π/2, π/2) для разложения в ряд Тейлора");
        }

        return 1.0 / Math.cos(x);
    }

    public static double secTaylor(double x, int terms) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (Double.isInfinite(x)) {
            return Double.NaN;
        }

        if (Math.abs(x) >= Math.PI / 2) {
            throw new IllegalArgumentException(
                    "Аргумент должен находиться в интервале (-π/2, π/2) для разложения в ряд Тейлора");
        }

        double result = 0.0;
        double xPow = 1.0;

        double[] eulers = getAllEulerNumbers(terms);


        System.out.println("Числа Эйлера (E2n):");
        for (int n = 0; n < terms; n++) {
            System.out.printf("E%d = %.0f%n", 2*n, eulers[n]);
        }

        for (int n = 0; n < terms; n++) {

            double coefficient = Math.abs(eulers[n]) / factorial(2 * n);
            result += coefficient * xPow;
            xPow *= x * x;

            System.out.printf("n=%d, E=%f, coeff=%f, term=%f, result=%f%n",
                    n, eulers[n], coefficient, coefficient * Math.pow(x, 2*n), result);
        }

        return result;
    }

    private static double[] getAllEulerNumbers(int terms) {
        double[] E = new double[terms];
        E[0] = 1.0; // E0 соответствует E(0)

        if (terms > 1) {
            E[1] = -1.0; // E2 = -1
        }

        for (int n = 2; n < terms; n++) {
            double sum = 0;
            // E(2n) = - sum_{k=0}^{n-1} C(2n, 2k) * E(2k)
            for (int k = 0; k < n; k++) {
                double comb = combination(2 * n, 2 * k);
                sum += comb * E[k];
            }
            E[n] = -sum;
        }

        return E;
    }

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

    private static double factorial(int n) {
        if (n == 0) return 1.0;
        double result = 1.0;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

}