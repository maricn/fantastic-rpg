package com.github.maricn.fantasticrpg.model.map;

import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public class Map implements Serializable {
    private static final Long serialVersionUID = 1L;

    private static final int[] DIRECTIONS = new int[]{-1, 0, 1};

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

    public Field getField(int y, int x) throws FantasticRpgException {
        if (y < 0 || x < 0 || y >= getHeight() || x >= getWidth()) {
            throw new FantasticRpgException("Field coordinates (" + y + ", " + x + ") out of map.");
        }

        return fields[y][x];
    }

    public void explore(int y, int x) {
        for (int i : DIRECTIONS) {
            for (int j : DIRECTIONS) {
                try {
                    getField(y + i, x + j).setExplored(true);
                } catch (FantasticRpgException ignored) {
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
