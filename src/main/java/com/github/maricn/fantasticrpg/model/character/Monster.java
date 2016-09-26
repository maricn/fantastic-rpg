package com.github.maricn.fantasticrpg.model.character;

import com.github.maricn.fantasticrpg.model.map.FieldType;

import java.io.Serializable;

import static com.github.maricn.fantasticrpg.model.map.FieldType.*;

/**
 * Created by nikola on 2016-09-22.
 *
 * @author nikola
 */
public class Monster extends GameCharacter implements Serializable {
    private static final Long serialVersionUID = 1L;

    private int minXp;

    private MonsterType type;

    public Monster(int healthPoints, int maxDamage, int minXp, int experience, MonsterType type) {
        this.type = type;
        this.healthPoints = (int) (type.hpFactor * healthPoints);
        this.damage = (int) (type.damageFactor * maxDamage);
        this.minXp = (int) (type.xpFactor * minXp);
        this.experience = (int) (type.xpFactor * experience);
    }

    public int getMinXp() {
        return minXp;
    }

    public MonsterType getType() {
        return type;
    }

    public enum MonsterType implements Serializable {
        BAT(EMPTY, 0.2, 0.4, 0.6),
        ROBOT(EMPTY, 0.4, 0.2, 0.6),
        WIZARD(EMPTY, 0.2, 0.6, 0.7),
        ELF(EMPTY, 0.4, 0.6, 0.8),
        SHARK(WATER, 0.8, 1.0, 0.9),
        DEICIDE(WALL, 1.0, 1.0, 1.0);

        private static final Long serialVersionUID = 1L;

        private final double hpFactor, damageFactor, xpFactor;
        private final FieldType fieldType;

        MonsterType(FieldType fieldType, double hpFactor, double damageFactor, double xpFactor) {
            this.fieldType = fieldType;
            this.hpFactor = hpFactor;
            this.damageFactor = damageFactor;
            this.xpFactor = xpFactor;
        }

        public FieldType getFieldType() {
            return fieldType;
        }

        public double getHpFactor() {
            return hpFactor;
        }

        public double getDamageFactor() {
            return damageFactor;
        }

        public double getXpFactor() {
            return xpFactor;
        }
    }

    @Override
    public String toString() {
        return "Monster {type = " + type +
                ", healthPoints = " + healthPoints +
                ", damage = " + damage +
                ", experience = " + experience +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Monster monster = (Monster) o;

        if (minXp != monster.minXp) return false;
        return type == monster.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + minXp;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
