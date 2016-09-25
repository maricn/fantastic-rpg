package com.github.maricn.fantasticrpg;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.repository.GameStateRepository;
import com.github.maricn.fantasticrpg.repository.GameStateRepositoryFileImpl;
import com.github.maricn.fantasticrpg.util.TestWorldBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by nikola on 2016-09-25.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class GameStateRepositoryFunctionalTest {
    private GameStateRepository gameStateRepository;

    @Before
    public void setUp() {
        gameStateRepository = new GameStateRepositoryFileImpl();
    }

    @Test
    public void testAll() {
        Instant startTime = Instant.now();
        Player player = TestWorldBuilder.buildPlayer();
        Map map = TestWorldBuilder.buildMap(player);
        GameState gameState = new GameState();
        gameState.setState(GameState.State.ROAMING);
        gameState.setMap(map);
        gameState.setPlayer(player);

        gameStateRepository.save(gameState);
        List<GameStateInfo> list = gameStateRepository.getAllGameStateInfos();

        assertNotNull(list);
        assertFalse(list.isEmpty());
        GameStateInfo gameStateInfo = list.get(0);
        assertNotNull(gameStateInfo);
        assertEquals(player.getName(), gameStateInfo.getPlayerName());
        assertTrue(gameStateInfo.getSaveTime().getEpochSecond() > startTime.getEpochSecond() - 1L);
        assertTrue(gameStateInfo.getSaveTime().getEpochSecond() < Instant.now().getEpochSecond() + 1L);

        GameState loadedGameState = gameStateRepository.load(gameStateInfo);

        assertNotNull(loadedGameState);
        assertEquals(gameState, loadedGameState);
    }
}
