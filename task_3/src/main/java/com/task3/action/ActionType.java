package com.task3.action;

public enum ActionType {
    PAUSE("пауза"),
    INTONATION_MODULATION("модуляция интонации"),
    TIMBRE_MODULATION("модуляция тембра");

    private final String description;

    ActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}