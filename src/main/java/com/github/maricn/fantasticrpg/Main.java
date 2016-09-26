package com.github.maricn.fantasticrpg;

import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommandHandler;
import com.github.maricn.fantasticrpg.controller.command.player.ActionCommandHandler;
import com.github.maricn.fantasticrpg.io.Console;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.MonsterFactory;
import com.github.maricn.fantasticrpg.model.map.MapFactory;
import com.github.maricn.fantasticrpg.repository.GameStateRepository;
import com.github.maricn.fantasticrpg.repository.GameStateRepositoryFileImpl;
import com.github.maricn.fantasticrpg.ui.MenuFactory;

import java.util.Arrays;
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
        InputOutput io = new Console();
        io.write("Initializing game... ");

        MonsterFactory monsterFactory = new MonsterFactory();
        MapFactory mapFactory = new MapFactory(monsterFactory);

        GameState gameState = new GameState();
        gameState.setState(GameState.State.NEW);

        GameStateRepository gameStateRepository = new GameStateRepositoryFileImpl();

        CommandDispatcher commandDispatcher = new CommandDispatcher();

        MenuFactory menuFactory = new MenuFactory(io, commandDispatcher);
        ActionCommandHandler actionCommandHandler = new ActionCommandHandler(gameState, io, menuFactory, commandDispatcher);
        MenuCommandHandler menuCommandHandler = new MenuCommandHandler(gameState, io, commandDispatcher, menuFactory, mapFactory, gameStateRepository);

        commandDispatcher.setActionCommandHandler(actionCommandHandler);
        commandDispatcher.setMenuCommandHandler(menuCommandHandler);

        commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.MAIN));

        io.write("DONE!\n\n" +
                "Your goal is to explore as many levels as you wish with your character.\n" +
                "When you start new game, you have 10 experience points.\n" +
                "By exploring level, you will encounter different evil creatures.\n" +
                "By fighting them you gain different amounts of experience.\n" +
                "More difficult it is to defeat a certain creature, more experience you gain.\n" +
                "Creatures are arranged (ASC) by their health points, damage and experience as following:\n" +
                Arrays.toString(Monster.MonsterType.values()) +
                ".\n" +
                "Start with lower levels until you improve your character, \n" +
                "so your chances of defeating stronger monsters increase.\n\n" +
                "Good luck!\n\n");
        commandDispatcher.run();
    }
}
