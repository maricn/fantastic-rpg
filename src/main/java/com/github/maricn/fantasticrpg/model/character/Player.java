package com.github.maricn.fantasticrpg.model.character;

import com.github.maricn.fantasticrpg.controller.command.player.Direction;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Anemic model used to represent player character.
 *
 * @author nikola
 */
public class Player extends GameCharacter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int currX, currY;
    private Set<Ability> abilities = new HashSet<>(2);
    private Direction facing = null;

    /**
     * Constructor used for creating new player.
     *
     * @param startY
     * @param startX
     */
    public Player(int startY, int startX, String name) {
        super(100, 20, 10);
        currY = startY;
        currX = startX;
        this.name = name;
    }

    /**
     * Constructor used when loading game state.
     *
     * @param healthPoints
     * @param damage
     * @param experience
     * @param name
     * @param currX
     * @param currY
     * @param abilities
     */
    public Player(int healthPoints, int damage, int experience, String name, int currX, int currY, Set<Ability> abilities) {
        super(healthPoints, damage, experience);
        this.name = name;
        this.currX = currX;
        this.currY = currY;
        this.abilities = abilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrX() {
        return currX;
    }

    public void setCurrX(int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return currY;
    }

    public void setCurrY(int currY) {
        this.currY = currY;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(Set<Ability> abilities) {
        this.abilities = abilities;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Player player = (Player) o;

        if (currX != player.currX) return false;
        if (currY != player.currY) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (abilities != null ? !abilities.equals(player.abilities) : player.abilities != null) return false;
        return facing == player.facing;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + currX;
        result = 31 * result + currY;
        result = 31 * result + (abilities != null ? abilities.hashCode() : 0);
        result = 31 * result + (facing != null ? facing.hashCode() : 0);
        return result;
    }
}
