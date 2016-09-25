package com.github.maricn.fantasticrpg.model;

import java.time.Instant;

/**
 * Created by nikola on 2016-09-22.
 *
 * @author nikola
 */
public class GameStateInfo {
    private Instant saveTime;
    private String saveName;
    private String playerName;
//    private GameState gameState;

    public Instant getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Instant saveTime) {
        this.saveTime = saveTime;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return "GameStateInfo{" +
                "saveTime=" + saveTime +
                ", saveName='" + saveName + '\'' +
                ", playerName='" + playerName + '\'' +
                '}';
    }
}
