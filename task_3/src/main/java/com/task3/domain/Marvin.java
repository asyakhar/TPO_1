package com.task3.domain;

import com.task3.domain.Character;

public class Marvin extends Character {
    private static final String MARVIN_NAME = "Марвин";

    public Marvin() {
        super(MARVIN_NAME);
    }

    @Override
    public String toString() {
        return "Marvin{name='" + getName() + "'}";
    }
}