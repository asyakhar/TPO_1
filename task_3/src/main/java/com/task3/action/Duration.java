package com.task3.action;

public enum Duration {
    MICROSECOND("микросекундная");

    private final String value;

    Duration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}