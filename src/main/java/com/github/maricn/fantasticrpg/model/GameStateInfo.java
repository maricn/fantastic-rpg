package com.github.maricn.fantasticrpg.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Metadata for {@link GameState}.
 *
 * @author nikola
 */
public class GameStateInfo {
    private Instant saveTime;
    private String saveName;
    private String playerName;

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
                "playerName = '" + playerName +
                "\', saveTime = " + DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .withLocale(Locale.getDefault())
                .format(saveTime) +
                '}';
    }
}
