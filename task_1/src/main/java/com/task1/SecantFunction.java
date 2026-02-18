package com.task1;

import java.util.*;

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
                    "Argument must be in range (-π/2, π/2) for Taylor series expansion");
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
                    "Argument must be in range (-π/2, π/2) for Taylor series expansion");
        }

        double result = 0.0;
        double xPow = 1.0; 

        for (int n = 0; n < terms; n++) {
            double eulerNumber = getEulerNumber(2 * n);
            
            double coefficient = Math.abs(eulerNumber) / factorial(2 * n);
            result += coefficient * xPow;
            xPow *= x * x; 
        }

        return result;
    }

    
    private static double getEulerNumber(int n) {
        if (n % 2 != 0) return 0; 

        double[] euler = new double[n/2 + 1];
        euler[0] = 1.0; 

        for (int i = 1; i <= n/2; i++) {
            euler[i] = 0;
            
            for (int k = 0; k < i; k++) {
                euler[i] += combination(2 * i, 2 * k) * euler[k];
            }
            euler[i] = -euler[i];
        }

        return euler[n/2];
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