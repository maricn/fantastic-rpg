package com.github.maricn.fantasticrpg.repository;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

import java.util.List;

/**
 * Repository interface for providing game save/load functionality.
 *
 * @author nikola
 */
public interface GameStateRepository {

    /**
     * Persist provided {@link GameState}.
     *
     * @param gameState game state to save
     */
    void save(GameState gameState);

    /**
     * List all persisted {@link GameState}s.
     *
     * @return list of {@link GameStateInfo}s representing persisted {@link GameState}s metadata
     */
    List<GameStateInfo> getAllGameStateInfos();

    /**
     * Load persisted {@link GameState} represented by its {@link GameStateInfo} {@code gameStateInfo} parameter.
     *
     * @param gameStateInfo {@link GameState} metadata used for identifying it
     * @return loaded {@link GameState}
     */
    GameState load(GameStateInfo gameStateInfo) throws FantasticRpgException;
}
