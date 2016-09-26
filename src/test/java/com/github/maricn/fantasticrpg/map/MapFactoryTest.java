package com.github.maricn.fantasticrpg.map;

import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.MonsterFactory;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.map.Field;
import com.github.maricn.fantasticrpg.model.map.FieldType;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.model.map.MapFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by nikola on 2016-09-26.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class MapFactoryTest {

    private MapFactory mapFactory;

    @Mock
    private MonsterFactory monsterFactory;

    @Before
    public void setUp() {
        monsterFactory = Mockito.mock(MonsterFactory.class);
        mapFactory = new MapFactory(monsterFactory);
    }

    @After
    public void tearDown() {
        Mockito.reset(monsterFactory);
    }

    @Test
    public void test() throws FantasticRpgException {
        when(monsterFactory.createMonster(any(Random.class), any(MonsterFactory.MonsterDifficulty.class), eq(FieldType.EMPTY)))
                .thenReturn(new Monster(1, 1, 1, 1, Monster.MonsterType.BAT))
                .thenReturn(new Monster(2, 2, 2, 2, Monster.MonsterType.ROBOT))
                .thenReturn(new Monster(3, 3, 3, 3, Monster.MonsterType.WIZARD))
                .thenReturn(new Monster(4, 4, 4, 4, Monster.MonsterType.ELF));
        when(monsterFactory.createMonster(any(Random.class), any(MonsterFactory.MonsterDifficulty.class), eq(FieldType.WATER)))
                .thenReturn(new Monster(3, 3, 3, 3, Monster.MonsterType.SHARK));
        when(monsterFactory.createMonster(any(Random.class), any(MonsterFactory.MonsterDifficulty.class), eq(FieldType.WALL)))
                .thenReturn(new Monster(5, 5, 5, 5, Monster.MonsterType.DEICIDE));

        int mapSize = 999;
        Map map = mapFactory.generateMap(mapSize, mapSize, 30);

        assertNotNull(map);
        java.util.Map<Monster.MonsterType, Integer> monsters = new HashMap<>();
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                Field field = map.getField(i, j);
                if (field.getOccupying() != null && field.getOccupying() instanceof Monster) {
                    Monster monster = (Monster) field.getOccupying();
                    monsters.merge(monster.getType(), 1, (v, vv) -> ++v);
                }
            }
        }

        // If there are all types of monsters, there are all types of fields
        // Highly implementation dependent, since we're using random and since monster types may change
        for (Monster.MonsterType monsterType : Monster.MonsterType.values()) {
            assertNotNull("!" + monsterType, monsters.get((monsterType)));
            assertTrue(monsters.get(monsterType) > 0);
        }
    }

    @Test
    public void testReusabilityOfSeed() {
        when(monsterFactory.createMonster(any(Random.class), any(MonsterFactory.MonsterDifficulty.class), eq(FieldType.EMPTY)))
                .thenReturn(new Monster(1, 1, 1, 1, Monster.MonsterType.BAT));
        when(monsterFactory.createMonster(any(Random.class), any(MonsterFactory.MonsterDifficulty.class), eq(FieldType.WATER)))
                .thenReturn(new Monster(3, 3, 3, 3, Monster.MonsterType.SHARK));
        when(monsterFactory.createMonster(any(Random.class), any(MonsterFactory.MonsterDifficulty.class), eq(FieldType.WALL)))
                .thenReturn(new Monster(5, 5, 5, 5, Monster.MonsterType.DEICIDE));

        Map map1 = mapFactory.generateMap(30);
        Map map2 = mapFactory.generateMap(80, 24, 30);

        assertNotNull(map1);
        assertNotNull(map2);
        assertEquals(map1, map2);
    }
}
