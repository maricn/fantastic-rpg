package com.github.maricn.fantasticrpg.model.map;

import com.github.maricn.fantasticrpg.model.character.GameCharacter;

import java.io.Serializable;

/**
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public class Field implements Serializable {
    private static final Long serialVersionUID = 1L;

    private boolean explored;
    private FieldType type;
    private GameCharacter occupying;

    public Field(FieldType fieldType) {
        this(false, fieldType, null);
    }

    public Field(boolean explored, FieldType type, GameCharacter occupying) {
        this.explored = explored;
        this.type = type;
        this.occupying = occupying;
    }

    public GameCharacter getOccupying() {
        return occupying;
    }

    public void setOccupying(GameCharacter occupying) {
        this.occupying = occupying;
    }

    public boolean getExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (explored != field.explored) return false;
        if (type != field.type) return false;
        return !(occupying != null ? !occupying.equals(field.occupying) : field.occupying != null);

    }

    @Override
    public int hashCode() {
        int result = (explored ? 1 : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (occupying != null ? occupying.hashCode() : 0);
        return result;
    }
}
