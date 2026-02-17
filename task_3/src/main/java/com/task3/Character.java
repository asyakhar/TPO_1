package com.task3;

import java.util.Objects;

public abstract class Character {
    private final String name;

    protected Character(String name) {
        this.name = Objects.requireNonNull(name, "Имя не может быть null");
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(name, character.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Character{name='" + name + "'}";
    }
}