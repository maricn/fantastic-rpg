package com.github.maricn.fantasticrpg.repository;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;

import java.util.List;

/**
 * Repository interface for providing game save/load functionality.
 * <p>
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public interface GameStateRepository {
    void save(GameState gameState);

    GameState load(GameStateInfo name);

    List<GameStateInfo> getAllGameStateInfos();
}
