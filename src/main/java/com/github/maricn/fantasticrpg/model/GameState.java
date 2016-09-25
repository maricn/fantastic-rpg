package com.github.maricn.fantasticrpg.model;

import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.map.Map;

import java.io.Serializable;

/**
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public class GameState implements Serializable {
    private static final Long serialVersionUID = 1L;

    private Map map;
    private Player player;
    private State state;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameState gameState = (GameState) o;

        if (map != null ? !map.equals(gameState.map) : gameState.map != null) return false;
        if (player != null ? !player.equals(gameState.player) : gameState.player != null) return false;
        return state == gameState.state;

    }

    @Override
    public int hashCode() {
        int result = map != null ? map.hashCode() : 0;
        result = 31 * result + (player != null ? player.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    public enum State {
        ROAMING, PAUSED, FIGHTING, NEW
    }
}
