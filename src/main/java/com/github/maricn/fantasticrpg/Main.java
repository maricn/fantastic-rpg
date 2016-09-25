package com.github.maricn.fantasticrpg;

import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommandHandler;
import com.github.maricn.fantasticrpg.controller.command.player.ActionCommandHandler;
import com.github.maricn.fantasticrpg.io.Console;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.MonsterFactory;
import com.github.maricn.fantasticrpg.model.map.MapFactory;
import com.github.maricn.fantasticrpg.repository.GameStateRepository;
import com.github.maricn.fantasticrpg.repository.GameStateRepositoryFileImpl;
import com.github.maricn.fantasticrpg.ui.MenuFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main class, used for sharing running state of application and as a composition root.
 *
 * @author nikola
 */
public class Main {

    private static AtomicBoolean running = new AtomicBoolean(true);

    public static boolean getRunning() {
        return Main.running.get();
    }

    public static void setRunning(boolean running) {
        Main.running.set(running);
    }

    public static void main(String[] args) {
        MonsterFactory monsterFactory = new MonsterFactory();
        MapFactory mapFactory = new MapFactory(monsterFactory);

        GameState gameState = new GameState();
        gameState.setState(GameState.State.NEW);

        GameStateRepository gameStateRepository = new GameStateRepositoryFileImpl();

        InputOutput io = new Console();

        CommandDispatcher commandDispatcher = new CommandDispatcher();

        MenuFactory menuFactory = new MenuFactory(io, commandDispatcher);
        ActionCommandHandler actionCommandHandler = new ActionCommandHandler(gameState, io, menuFactory, commandDispatcher);
        MenuCommandHandler menuCommandHandler = new MenuCommandHandler(gameState, io, commandDispatcher, menuFactory, mapFactory, gameStateRepository);

        commandDispatcher.setActionCommandHandler(actionCommandHandler);
        commandDispatcher.setMenuCommandHandler(menuCommandHandler);

        commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.MAIN));
        commandDispatcher.run();
    }
}
