package com.github.maricn.fantasticrpg.controller.command.menu;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.CommandHandler;
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
                menuFactory.getFightMenu().interact();
                break;
            case DUMP:
                io.dumpMap(gameState.getMap());
                commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.RESUME));
                break;
            case PAUSE:
                gameState.setState(GameState.State.PAUSED);
                menuFactory.getPauseMenu().interact();
                break;
            case SAVE:
//                gameState.setState(GameState.State.PAUSED);
                gameStateRepository.save(gameState);
                menuFactory.getPauseMenu().interact();
//                play();
                break;
            case LOAD:
                List<GameStateInfo> allGameStateInfos = gameStateRepository.getAllGameStateInfos();
                io.write("Choose saved game:\n");
                for (int i = 0; i < Math.min(10, allGameStateInfos.size()); i++) {
                    io.write("(" + i + ") " + allGameStateInfos.get(i).toString());
                }

                char saveOrd = io.readChar();
                GameStateInfo gameStateInfo = allGameStateInfos.get(saveOrd - '0');
                GameState loadedGameState = gameStateRepository.load(gameStateInfo);

                gameState.setMap(loadedGameState.getMap());
                gameState.setPlayer(loadedGameState.getPlayer());
                gameState.setState(loadedGameState.getState());
//                play();
                break;
            case QUIT:
                io.write("Bye!");
                Main.setRunning(false);
                break;
            case NEW:
                // @TODO: kill old game (in pause menu, starting new game)
                if (gameState.getState() == GameState.State.PAUSED) {
                    gameState.setState(GameState.State.NEW);
                    commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.NEW));
                    break;
                }

                // @TODO: input error checking
                io.write("What is your character's name?");
                String charName = io.read();

                io.write("Input a level number (1-10 Easy, 11-20 Medium, 21-30 Hard):\n");
                int level = Integer.parseInt(io.read().trim());

                // Generate map and create a player if first level
                Map map = mapFactory.generateMap(level);
                if (gameState.getPlayer() == null) {
                    gameState.setPlayer(new Player(map.getStartY(), map.getStartX(), charName));
                }

                // Put player on map
                Player player = gameState.getPlayer();
                player.setCurrX(map.getStartX());
                player.setCurrY(map.getStartY());
                map.explore(map.getStartY(), map.getStartX());
                try {
                    map.getField(map.getStartY(), map.getStartX()).setOccupying(player);
                } catch (FantasticRpgException ignored) {
                }

                // Update game state
                gameState.setState(GameState.State.ROAMING);
                gameState.setMap(map);
                gameState.setPlayer(player);

                io.write("You are placed in level " + level + ". Explore the world to complete the level.\n");
                menuFactory.getMovementMenu().interact();
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
