package com.task3.action;

public enum ActionType {
    PAUSE("пауза", false),
    INTONATION_MODULATION("модуляция интонации", false),
    TIMBRE_MODULATION("модуляция тембра", false),
    MOCKING_LAUGHTER("издевательский смех", true);

    private final String description;
    private final boolean isOffensive;

    ActionType(String description, boolean isOffensive) {
        this.description = description;
        this.isOffensive=isOffensive;
    }
    public boolean isOffensive(){
        return isOffensive;
    }
    public String getDescription() {
        return description;
    }
}