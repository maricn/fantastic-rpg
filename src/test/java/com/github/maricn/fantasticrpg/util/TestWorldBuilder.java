package com.github.maricn.fantasticrpg.util;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.Ability;
import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.map.Field;
import com.github.maricn.fantasticrpg.model.map.FieldType;
import com.github.maricn.fantasticrpg.model.map.Map;

import java.util.Collections;

/**
 * Created by nikola on 2016-09-25.
 *
 * @author nikola
 */
public class TestWorldBuilder {

    public static Player buildPlayer() {
        return new Player(100, 20, 10, "player", 0, 0, Collections.<Ability>emptySet());
    }

    public static Monster buildEasyMonster(Monster.MonsterType type) {
        return new Monster(1, 1, 1, 10, type);
    }

    public static Monster buildImpossibleMonster(Monster.MonsterType type) {
        return new Monster(99_999, 99_999, 1, 99_999, type);
    }

    public static Map buildExploredMap() {
        return buildMap(buildPlayer(), true);
    }

    public static Map buildMap(Player player) {
        return buildMap(player, false);
    }

    public static Map buildMap(Player player, boolean explored) {
        Field[][] fields = new Field[][]{
                {new Field(explored, FieldType.EMPTY, player), new Field(explored, FieldType.WALL, buildImpossibleMonster(Monster.MonsterType.DEICIDE))},
                {new Field(explored, FieldType.EMPTY, null), new Field(explored, FieldType.WATER, null)},
                {new Field(explored, FieldType.EMPTY, buildImpossibleMonster(Monster.MonsterType.ROBOT)), new Field(explored, FieldType.WATER, buildEasyMonster(Monster.MonsterType.SHARK))}
        };
        return new Map(fields, 0, 0, 2);
    }

    public static GameState buildGameState() {
        Player player = buildPlayer();
        Map map = buildMap(player);

        GameState gameState = new GameState();
        gameState.setState(GameState.State.ROAMING);
        gameState.setMap(map);
        gameState.setPlayer(player);

        return gameState;

    }
}
