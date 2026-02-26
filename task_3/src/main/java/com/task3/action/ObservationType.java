package com.task3.action;

public enum ObservationType {
    NEUTRAL("нейтральное наблюдение"),
    SARCASTIC("наблюдение, вызывающее сарказм"),
    CONTEMPTUOUS("наблюдение, вызывающее презрение"),
    HORRIFYING("наблюдение, вызывающее ужас");

    private final String description;

    ObservationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}