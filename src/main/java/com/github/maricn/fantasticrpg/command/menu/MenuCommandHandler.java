package com.github.maricn.fantasticrpg.command.menu;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.command.CommandDispatcher;
import com.github.maricn.fantasticrpg.command.CommandHandler;
import com.github.maricn.fantasticrpg.command.menu.model.LoadMenuCommand;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.model.map.MapFactory;
import com.github.maricn.fantasticrpg.repository.GameStateRepository;
import com.github.maricn.fantasticrpg.ui.MenuFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.maricn.fantasticrpg.command.menu.model.MenuCommand.Menu.*;
import static com.github.maricn.fantasticrpg.model.GameState.State.PAUSED;

/**
 * Handles displaying menus and changing game state commands (ie., non gaming commands).
 *
 * @author nikola
 */
public class MenuCommandHandler implements CommandHandler<MenuCommand> {
    private GameState gameState;
    private InputOutput io;
    private final CommandDispatcher commandDispatcher;
    private final MenuFactory menuFactory;
    private final MapFactory mapFactory;
    private final GameStateRepository gameStateRepository;

    public MenuCommandHandler(GameState gameState, InputOutput io, CommandDispatcher commandDispatcher, MenuFactory menuFactory, MapFactory mapFactory, GameStateRepository gameStateRepository) {
        this.gameState = gameState;
        this.io = io;
        this.commandDispatcher = commandDispatcher;
        this.menuFactory = menuFactory;
        this.mapFactory = mapFactory;
        this.gameStateRepository = gameStateRepository;
    }

    @Override
    public void executeCommand(MenuCommand command) {
        switch (command.getMenu()) {
            case MAIN:
                menuFactory.getMainMenu().interact();
                break;
            case FIGHT:
                gameState.setState(GameState.State.FIGHTING);
                menuFactory.getFightMenu().interact();
                break;
            case DUMP:
                io.dumpMap(gameState.getMap());
                io.dumpPlayer(gameState.getPlayer());
                commandDispatcher.offer(new MenuCommand(RESUME));
                break;
            case PAUSE:
                gameState.setState(PAUSED);
                menuFactory.getPauseMenu().interact();
                break;
            case SAVE:
                gameStateRepository.save(gameState);
                io.write("Game successfully saved!\n");

                // We can save when we finish level or in game
                if (gameState.getState() == PAUSED) {
                    menuFactory.getPauseMenu().interact();
                } else {
                    menuFactory.getMainMenu().interact();
                }
                break;
            case LOAD:
                List<GameStateInfo> allGameStateInfos = gameStateRepository.getAllGameStateInfos();
                List<Command> loadMenuCommandList = IntStream.range(0, allGameStateInfos.size())
                        .limit(10)
                        .mapToObj(i -> new LoadMenuCommand((char) (i + '0'), allGameStateInfos.get(i).toString()))
                        .collect(Collectors.toList());
                loadMenuCommandList.add(new MenuCommand(RESUME));
                if (!loadMenuCommandList.isEmpty()) {
                    menuFactory.getLoadMenu(loadMenuCommandList).interact();
                } else {
                    io.error("No saved games present!\n");
                    commandDispatcher.offer(new MenuCommand(gameState.getState() == PAUSED ? RESUME : MAIN));
                }
                break;
            case LOADGAME:
                LoadMenuCommand lmc = (LoadMenuCommand) command;
                GameStateInfo gameStateInfo = gameStateRepository.getAllGameStateInfos().get(lmc.getChoice() - '0');
                try {
                    GameState loadedGameState = gameStateRepository.load(gameStateInfo);

                    gameState.setMap(loadedGameState.getMap());
                    gameState.setPlayer(loadedGameState.getPlayer());
                    gameState.setState(loadedGameState.getState());
                } catch (FantasticRpgException e) {
                    io.error(e.getMessage());
                    commandDispatcher.offer(new MenuCommand(gameState.getState() == PAUSED ? RESUME : MAIN));
                    break;
                }

                commandDispatcher.offer(new MenuCommand(RESUME));
                break;
            case QUIT:
                io.write("Bye!");
                Main.setRunning(false);
                break;
            case NEW:
                if (gameState.getState() == PAUSED) {
                    gameState.setState(GameState.State.NEW);
                    commandDispatcher.offer(new MenuCommand(NEW));
                    break;
                }

                // Input player's name or reuse it if it's not the first level
                String playerName;
                if (gameState.getPlayer() == null) {
                    io.write("What is your character's name?\n");
                    playerName = io.read();
                } else {
                    playerName = gameState.getPlayer().getName();
                }

                int level = -1;
                do {
                    io.write("Input a level number (1-10 Easy, 11-20 Medium, 21-30 Hard):\n");
                    try {
                        level = Integer.parseInt(io.read().trim());
                    } catch (Exception ignored) {
                    }
                } while (level < 1 || level > 30);

                // Generate map and create a player if first level
                Map map = mapFactory.generateMap(level);
                if (gameState.getPlayer() == null) {
                    gameState.setPlayer(new Player(map.getStartY(), map.getStartX(), playerName));
                }

                // Put player on map
                Player player = gameState.getPlayer();
                player.setCurrX(map.getStartX());
                player.setCurrY(map.getStartY());
                map.explore(map.getStartY(), map.getStartX());
                map.getField(map.getStartY(), map.getStartX()).setOccupying(player);

                // Update game state
                gameState.setState(GameState.State.ROAMING);
                gameState.setMap(map);
                gameState.setPlayer(player);

                io.write("You are placed in level " + level + ". Explore the world to complete the level.\n");
                commandDispatcher.offer(new MenuCommand(RESUME));
                break;
            case RESUME:
                gameState.setState(GameState.State.ROAMING);
                menuFactory.getMovementMenu().interact();
                break;
            default:
                break;
        }
    }
}
