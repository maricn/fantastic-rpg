package com.github.maricn.fantasticrpg.model.character;

import com.github.maricn.fantasticrpg.model.map.FieldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-26.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class MonsterFactoryTest {

    private MonsterFactory monsterFactory;

    private final Random myRandom = new Random(Instant.now().getEpochSecond());

    @Mock
    private Random random;

    @Before
    public void setUp() {
        random = Mockito.mock(Random.class);
        when(random.nextInt(anyInt()))
                .thenAnswer(invocationOnMock -> {
                    Integer arg = (Integer) invocationOnMock.getArguments()[0];
                    return myRandom.nextInt(arg > 0 ? arg : -arg);
                });

        monsterFactory = new MonsterFactory();
    }

    @After
    public void tearDown() {
        Mockito.reset(random);
    }

    @Test
    public void testEasyEmpty() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.EASY, FieldType.EMPTY);
    }


    @Test
    public void testEasyWater() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.EASY, FieldType.WATER);
    }

    @Test
    public void testEasyWall() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.EASY, FieldType.WALL);
    }

    @Test
    public void testModerateEmpty() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.MODERATE, FieldType.EMPTY);
    }

    @Test
    public void testModerateWater() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.MODERATE, FieldType.WATER);
    }

    @Test
    public void testModerateWall() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.MODERATE, FieldType.WALL);
    }

    @Test
    public void testHardEmpty() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.HARD, FieldType.EMPTY);
    }

    @Test
    public void testHardWater() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.HARD, FieldType.WATER);
    }

    @Test
    public void testHardWall() {
        testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty.HARD, FieldType.WALL);
    }

    private void testByDifficultyAndFieldType(MonsterFactory.MonsterDifficulty difficulty, FieldType fieldType) {
        Monster monster = monsterFactory.createMonster(random, difficulty, fieldType);
        assertNotNull(monster);
        assertEquals(fieldType, monster.getType().getFieldType());
        assertMonsterInDifficultyLevel(monster, difficulty);

        verify(random, atLeast(3)).nextInt(anyInt());
    }

    private void assertMonsterInDifficultyLevel(Monster monster, MonsterFactory.MonsterDifficulty difficulty) {
        assertTrue(difficulty.getHp() >= monster.getHealthPoints());
        assertTrue(
                monster.getHealthPoints() >=
                        (int) monster.getType().getHpFactor() * difficulty.getHp() * (1 - MonsterFactory.RANDOMNESS_RATIO)
        );
        assertTrue(difficulty.getDmg() >= monster.getDamage());
        assertTrue(
                monster.getDamage() >=
                        (int) monster.getType().getDamageFactor() * difficulty.getDmg() * (1 - MonsterFactory.RANDOMNESS_RATIO)
        );
        assertTrue(difficulty.getXp() >= monster.getExperience());
        assertTrue(
//                "Monster: " + monster.getExperience() + " | expected min: " + monster.getType().getXpFactor() * difficulty.getXp() * (1 - MonsterFactory.RANDOMNESS_RATIO),
                monster.getExperience() >=
                        (int) monster.getType().getXpFactor() * difficulty.getXp() * (1 - MonsterFactory.RANDOMNESS_RATIO)
        );
        assertTrue(difficulty.getXp() >= monster.getMinXp());
        assertTrue(
//                "Monster: " + monster.getMinXp() + " | expected min: " + monster.getType().getXpFactor() * difficulty.getXp() * (1 - MonsterFactory.RANDOMNESS_RATIO),
                monster.getMinXp() >=
                        (int) monster.getType().getXpFactor() * difficulty.getXp() * (1 - MonsterFactory.RANDOMNESS_RATIO)
        );
    }
}
