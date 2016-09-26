package com.github.maricn.fantasticrpg.model.map;

import java.io.Serializable;

/**
 * Enum representing different field types for map fields.
 *
 * @author nikola
 */
public enum FieldType implements Serializable {
    EMPTY,
    WATER,
    WALL;

    private static final Long serialVersionUID = 1L;

}
