package com.github.maricn.fantasticrpg.model.character;

/**
 * Enum representing different abilities player may possess.
 *
 * @author nikola
 */
public enum Ability {
    SWIMMING(400), GODMODE(800);

    private int experience;

    Ability(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }
}
