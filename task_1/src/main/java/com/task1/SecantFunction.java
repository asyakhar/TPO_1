package com.task1;

public class SecantFunction {

    private static final double PI_HALF = Math.PI / 2.0;
    private static final double EPS_SINGULAR = 1e-12;

    public static double sec(double x) {
        return sec(x, 100);
    }

    public static double sec(double x, int n) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }
        
        double absX = Math.abs(x);

        if (absX >= PI_HALF - EPS_SINGULAR) {
            return Double.NaN;
        }
        
        if (absX < 1e-10) {
            return 1.0;
        }

        if (absX >= 1.4) {
            return computeSecNearBoundary(x, n);
        }

        return computeSecSeries(absX, n);
    }

    private static double computeSecNearBoundary(double x, int n) {

        double delta = PI_HALF - Math.abs(x);
        
        if (delta < 1e-8) {
            return 1.0 / delta;
        }
        
        double sinDelta = computeSinSeries(delta, n);
        
        return 1.0 / sinDelta;
    }

    private static double computeSinSeries(double x, int maxTerms) {
        
        double sum = x;
        double term = x;
        double x2 = x * x;

        for (int i = 1; i <= maxTerms; i++) {
            term *= -x2 / ((2 * i) * (2 * i + 1));
            sum += term;

            if (Math.abs(term) < 1e-16 * Math.abs(sum)) {
                break;
            }
        }

        return sum;
    }

    private static double computeSecSeries(double x, int maxTerms) {
        double sum = 1.0;
        double x2 = x * x;
        double powerX = 1.0;

        double[] euler = new double[maxTerms + 1];
        euler[0] = 1.0;

        for (int i = 1; i <= maxTerms; i++) {
            int twoN = 2 * i;

            double sumE = 0.0;
            for (int k = 0; k < i; k++) {
                double comb = combination(twoN, 2 * k);
                sumE += comb * euler[k];

                if (Double.isInfinite(sumE)) {
                    break;
                }
            }

            euler[i] = -sumE;

            if (Double.isInfinite(euler[i])) {
                break;
            }

            powerX *= x2;

            
            double logTerm = Math.log(Math.abs(euler[i])) + Math.log(powerX) - logFactorial(twoN);

            if (logTerm < -700) { 
                break;
            }

            double term = Math.exp(logTerm);
            sum += term;

            
            if (term < 1e-16 * sum) {
                break;
            }
        }

        return sum;
    }

    private static double combination(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1.0;

        k = Math.min(k, n - k);
        
        if (n > 50) {
            double logComb = 0.0;
            for (int i = 1; i <= k; i++) {
                logComb += Math.log(n - k + i) - Math.log(i);
            }
            return Math.exp(logComb);
        }

        
        double res = 1.0;
        for (int i = 1; i <= k; i++) {
            res = res * (n - k + i) / i;
        }
        return res;
    }

    private static double logFactorial(int n) {
        if (n <= 1) return 0.0;

        double logFact = 0.0;
        for (int i = 2; i <= n; i++) {
            logFact += Math.log(i);
        }
        return logFact;
    }
    

}