package com.github.maricn.fantasticrpg.command.player.model;

/**
 * Enum representing four world directions.
 *
 * @author nikola
 */
public enum Direction {
    WEST('W', "West", -1, 0),
    NORTH('N', "North", 0, -1),
    EAST('E', "East", 1, 0),
    SOUTH('S', "South", 0, 1);

    private final char abbreviation;
    private final String name;
    private final int deltaX;
    private final int deltaY;

    Direction(char abbr, String option, int deltaX, int deltaY) {
        this.abbreviation = abbr;
        this.name = option;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public char getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }
}
