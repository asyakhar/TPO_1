package com.task3.emotion;

public enum Intensity {
    COMPLETE("полное");

    private final String value;

    Intensity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}