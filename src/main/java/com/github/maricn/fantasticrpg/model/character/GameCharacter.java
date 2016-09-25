package com.github.maricn.fantasticrpg.model.character;

import java.io.Serializable;

/**
 *
 * @author nikola
 */
public class GameCharacter implements Serializable {
    private static final Long serialVersionUID = 1L;

    protected int healthPoints, damage, experience;

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public GameCharacter() {
    }

    public GameCharacter(int healthPoints, int damage, int experience) {
        this.healthPoints = healthPoints;
        this.damage = damage;
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCharacter that = (GameCharacter) o;

        if (healthPoints != that.healthPoints) return false;
        if (damage != that.damage) return false;
        return experience == that.experience;

    }

    @Override
    public int hashCode() {
        int result = healthPoints;
        result = 31 * result + damage;
        result = 31 * result + experience;
        return result;
    }

    @Override
    public String toString() {
        return "GameCharacter {" +
                "healthPoints = " + healthPoints +
                ", damage = " + damage +
                ", experience = " + experience +
                '}';
    }
}
