package com.task3.action;

public enum CalculationLevel {
    THOROUGH("тщательный");

    private final String value;

    CalculationLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}