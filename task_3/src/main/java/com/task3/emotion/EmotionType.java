package com.task3.emotion;

public enum EmotionType {
    CONTEMPT("презрение"),
    HORROR("ужас");

    private final String value;

    EmotionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}