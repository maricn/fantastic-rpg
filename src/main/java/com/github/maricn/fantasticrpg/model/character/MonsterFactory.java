package com.github.maricn.fantasticrpg.model.character;

import com.github.maricn.fantasticrpg.model.map.FieldType;

import java.util.Random;

/**
 * Used to generate different monsters based on provided {@link com.github.maricn.fantasticrpg.model.character.MonsterFactory.MonsterDifficulty} and {@link FieldType} they inhabit.
 * {@link Random} number generator is passed as an argument, in order to generate the same {@link Monster}s on a level.
 *
 * @author nikola
 */
public class MonsterFactory {

    public static final double RANDOMNESS_RATIO = 0.2d;

    public Monster createMonster(Random random, MonsterDifficulty difficulty, FieldType fieldType) {
        Monster.MonsterType[] types = Monster.MonsterType.values();
        Monster.MonsterType monsterType;
        do {
            monsterType = types[random.nextInt(types.length)];
        } while (monsterType.getFieldType() != fieldType);

        int randomHp = (int) (difficulty.hp * (1 - RANDOMNESS_RATIO)) +
                random.nextInt((int) (difficulty.hp * RANDOMNESS_RATIO));
        int randomDamage = (int) (difficulty.dmg * (1 - RANDOMNESS_RATIO) +
                random.nextInt((int) (difficulty.dmg * RANDOMNESS_RATIO)));
        int randomXp = (int) (difficulty.xp * (1 - RANDOMNESS_RATIO) +
                random.nextInt((int) (difficulty.xp * RANDOMNESS_RATIO)));
        return new Monster(randomHp, randomDamage, randomXp, randomXp, monsterType);
    }

    /**
     * Enum representing different point scales for game difficulties.
     */
    public enum MonsterDifficulty {
        EASY(100, 30, 10), MODERATE(150, 80, 100), HARD(300, 100, 200);

        private final int hp;
        private final int dmg;
        private final int xp;

        MonsterDifficulty(int hp, int dmg, int xp) {
            this.hp = hp;
            this.dmg = dmg;
            this.xp = xp;
        }

        public int getHp() {
            return hp;
        }

        public int getDmg() {
            return dmg;
        }

        public int getXp() {
            return xp;
        }
    }
}
