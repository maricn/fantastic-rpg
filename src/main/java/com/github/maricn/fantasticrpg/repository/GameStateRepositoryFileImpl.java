package com.github.maricn.fantasticrpg.repository;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;

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
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public class GameStateRepositoryFileImpl implements GameStateRepository {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .withLocale(Locale.getDefault());

    public GameStateRepositoryFileImpl() {
        try {
            Files.createDirectories(Paths.get("savegames"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameState load(GameStateInfo gameStateInfo) {
        File file = Paths.get("savegames", gameStateInfo.getSaveName()).toFile();
        try {
            ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            return (GameState) input.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return null;
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
//                            FileTime lastModifiedTime = Files.getLastModifiedTime(path);
//                            gsi.setSaveTime(lastModifiedTime.toInstant());

                        return gsi;
                    })
                    .sorted((o1, o2) -> o2.getSaveTime().compareTo(o1.getSaveTime()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(0);
    }
}
