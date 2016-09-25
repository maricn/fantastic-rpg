package com.github.maricn.fantasticrpg.model.character;

import com.github.maricn.fantasticrpg.model.map.FieldType;

import java.util.Random;

/**
 * Created by nikola on 2016-09-22.
 *
 * @author nikola
 */
public class MonsterFactory {

    public Monster createMonster(Random random, MonsterDifficulty difficulty, FieldType fieldType) {
        Monster.MonsterType[] types = Monster.MonsterType.values();
        Monster.MonsterType monsterType;
        do {
            monsterType = types[random.nextInt(types.length)];
        } while (monsterType.getFieldType() != fieldType);

        return new Monster(difficulty.hp, difficulty.dmg, difficulty.xp, difficulty.xp, monsterType);
    }

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
    }
}
