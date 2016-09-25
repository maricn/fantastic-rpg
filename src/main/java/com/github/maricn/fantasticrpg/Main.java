package com.github.maricn.fantasticrpg;

import com.github.maricn.fantasticrpg.controller.CommandExecutor;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommandHandler;
import com.github.maricn.fantasticrpg.controller.command.player.ActionCommandHandler;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.MonsterFactory;
import com.github.maricn.fantasticrpg.io.Console;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.map.MapFactory;
import com.github.maricn.fantasticrpg.repository.GameStateRepository;
import com.github.maricn.fantasticrpg.repository.GameStateRepositoryFileImpl;
import com.github.maricn.fantasticrpg.ui.MenuFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public class Main {

    // @TODO: nikola - mention Ctrl+Z on UNIX and bg/fg for pausing game

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
        CommandExecutor commandExecutor = new CommandExecutor();

        MenuFactory menuFactory = new MenuFactory(io, commandExecutor);

        ActionCommandHandler actionCommandHandler = new ActionCommandHandler(gameState, io, menuFactory, commandExecutor);
        menuFactory.setActionCommandHandler(actionCommandHandler);

        MenuCommandHandler menuCommandHandler = new MenuCommandHandler(gameState, io, commandExecutor, menuFactory, mapFactory, gameStateRepository);
        menuFactory.setMenuCommandHandler(menuCommandHandler);

        commandExecutor.exec(new MenuCommand(menuCommandHandler, MenuCommand.Menu.MAIN));
        commandExecutor.run();
    }
}
