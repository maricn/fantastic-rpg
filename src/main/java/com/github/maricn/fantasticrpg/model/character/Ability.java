package com.github.maricn.fantasticrpg.model.character;

/**
 * Enum representing different abilities player may possess.
 *
 * @author nikola
 */
public enum Ability {
    // @TODO: increase values
    SWIMMING(500), GODMODE(1000);

    private int experience;

    Ability(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }
}
