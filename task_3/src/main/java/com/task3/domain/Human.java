package com.task3.domain;

import java.util.Objects;

public class Human extends Character {
    private final String species;

    public Human(String name) {
        super(name);
        this.species = "human";
    }

    public String getSpecies() {
        return species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Human human = (Human) o;
        return Objects.equals(species, human.species);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), species);
    }

    @Override
    public String toString() {
        return "Human{name='" + getName() + "', species='" + species + "'}";
    }
}