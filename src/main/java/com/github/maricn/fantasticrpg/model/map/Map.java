package com.github.maricn.fantasticrpg.model.map;

import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.exception.OutOfBoundsFantasticRpgException;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents game map consisting of fields which can be explored, and can contain {@link com.github.maricn.fantasticrpg.model.character.GameCharacter}s.
 * Each level is represented by a map.
 *
 * @author nikola
 */
public class Map implements Serializable {
    private static final Long serialVersionUID = 1L;

    private static final int[] DIRECTIONS = new int[]{-2, -1, 0, 1, 2};

    private final Field[][] fields;
    private final int startY;
    private final int startX;
    private int numOfMonsters;

    public Map(Field[][] fields, int startY, int startX, int numOfMonsters) {
        this.fields = fields;
        this.startY = startY;
        this.startX = startX;
        this.numOfMonsters = numOfMonsters;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartX() {
        return startX;
    }

    public int getHeight() {
        return fields.length;
    }

    public int getWidth() {
        return fields[0].length;
    }

    public int getNumOfMonsters() {
        return numOfMonsters;
    }

    public void setNumOfMonsters(int numOfMonsters) {
        this.numOfMonsters = numOfMonsters;
    }

    /**
     * Returns a field with given coordinates.
     *
     * @param y Y coordinate
     * @param x X coordinate
     * @return field
     * @throws FantasticRpgException
     */
    public Field getField(int y, int x) throws OutOfBoundsFantasticRpgException {
        if (y < 0 || x < 0 || y >= getHeight() || x >= getWidth()) {
            throw new OutOfBoundsFantasticRpgException("Field coordinates (" + y + ", " + x + ") out of map.");
        }

        return fields[y][x];
    }

    /**
     * Explores surrounding fields.
     *
     * @param y Y coordinate of center field
     * @param x X coordinate of center field
     */
    public void explore(int y, int x) {
        for (int i : DIRECTIONS) {
            for (int j : DIRECTIONS) {
                try {
                    getField(y + i, x + j).setExplored(true);
                } catch (OutOfBoundsFantasticRpgException ignored) {
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        if (startY != map.startY) return false;
        if (startX != map.startX) return false;
        if (numOfMonsters != map.numOfMonsters) return false;
        return Arrays.deepEquals(fields, map.fields);
    }

    @Override
    public int hashCode() {
        int result = fields != null ? Arrays.deepHashCode(fields) : 0;
        result = 31 * result + startY;
        result = 31 * result + startX;
        result = 31 * result + numOfMonsters;
        return result;
    }
}
