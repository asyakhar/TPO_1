package com.task1;

public class Function {

    public static double sec(double x) {
        return sec(x, Integer.MAX_VALUE);
    }


    public static double sec(double x, int n) {

        if (Math.abs(Math.cos(x)) < 1e-10) {
            return Double.NaN;
        }


        if (Double.isInfinite(x) || Double.isNaN(x)) {
            return Double.NaN;
        }


        x = normalizeAngle(x);


        if (Math.abs(x) > Math.PI / 4) {
            return 1.0 / Math.cos(x);
        }

        double result = 1.0;
        double term = 1.0;
        double xSquared = x * x;



        long[] eulerNumbers = {1, -1, 5, -61, 1385, -50521, 2702765L, -199360981L};

        for (int i = 1; i <= n && i - 1 < eulerNumbers.length; i++) {
            double oldResult = result;


            term = term * xSquared / (2 * i * (2 * i - 1));
            double nextTerm = term * Math.abs(eulerNumbers[i - 1]);


            result += (i % 2 == 1) ? nextTerm : -nextTerm;


            if (Math.abs(result - oldResult) < 1e-15) {
                break;
            }
        }

        return result;
    }


    private static double normalizeAngle(double x) {
        double twoPi = 2 * Math.PI;
        x = x % twoPi;
        if (x > Math.PI) {
            x -= twoPi;
        } else if (x < -Math.PI) {
            x += twoPi;
        }
        return x;
    }
}