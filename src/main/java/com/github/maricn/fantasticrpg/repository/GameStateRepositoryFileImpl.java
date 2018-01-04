package com.github.maricn.fantasticrpg.repository;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Implementation of {@link GameStateRepository} which saves and loads {@link GameState}s
 * to and from files in folder {@code savegames}.
 *
 * @author nikola
 */
public class GameStateRepositoryFileImpl implements GameStateRepository {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .withLocale(Locale.getDefault());

    public GameStateRepositoryFileImpl() throws IOException {
        Files.createDirectories(Paths.get("savegames"));
    }

    @Override
    public void save(GameState gameState) {
        String fileName = dateTimeFormatter
                .format(Instant.now()) +
                "." + gameState.getPlayer().getName() +
                ".frpg";
        Path savegame = Paths.get("savegames", fileName);
        try {
            File file = savegame.toFile();
            boolean newFile = file.createNewFile();
            if (newFile) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(gameState);
            }
        } catch (IOException ignored) {
            // @TODO: implement logging at least
        }
    }

    @Override
    public GameState load(GameStateInfo gameStateInfo) throws FantasticRpgException {
        File file = Paths.get("savegames", gameStateInfo.getSaveName()).toFile();
        try {
            ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            return (GameState) input.readObject();
        } catch (ClassNotFoundException | IOException ignored) {
            throw new FantasticRpgException("Saved game is corrupt or is saved with different game version.\n");
        }
    }

    @Override
    public List<GameStateInfo> getAllGameStateInfos() {
        try {
            return Files
                    .list(Paths.get("savegames"))
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        GameStateInfo gsi = new GameStateInfo();
                        String fileName = path.toString().replaceFirst("savegames/", "");
                        String[] fileNameParts = fileName.split("\\.");
                        gsi.setSaveName(fileName);
                        gsi.setPlayerName(fileNameParts[1]);

                        Instant parsedDate = Instant.from(dateTimeFormatter.parse(fileNameParts[0]));
                        gsi.setSaveTime(parsedDate);
                        return gsi;
                    })
                    .sorted((o1, o2) -> o2.getSaveTime().compareTo(o1.getSaveTime()))
                    .collect(Collectors.toList());
        } catch (IOException ignored) {
            // @TODO: implement logging at least
        }

        return new ArrayList<>(0);
    }
}
