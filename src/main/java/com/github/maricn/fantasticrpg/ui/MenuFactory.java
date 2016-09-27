package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.command.CommandDispatcher;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.command.player.model.Direction;
import com.github.maricn.fantasticrpg.command.player.model.FightCommand;
import com.github.maricn.fantasticrpg.command.player.model.MoveCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;

import java.util.Arrays;
import java.util.List;

/**
 * Factory class for producing standard menus used in game.
 *
 * @author nikola
 */
public class MenuFactory {

    private InputOutput io;
    private CommandDispatcher commandDispatcher;

    private Menu mainMenu, movementMenu, pauseMenu, fightMenu;

    public MenuFactory(InputOutput io, CommandDispatcher commandDispatcher) {
        this.io = io;
        this.commandDispatcher = commandDispatcher;
    }

    /**
     * Produces and reuses main menu.
     *
     * @return main menu
     */
    public Menu getMainMenu() {
        if (mainMenu == null) {
            this.mainMenu = create(Arrays.asList(
                    new MenuCommand(MenuCommand.Menu.NEW),
                    new MenuCommand(MenuCommand.Menu.SAVE),
                    new MenuCommand(MenuCommand.Menu.LOAD),
                    new MenuCommand(MenuCommand.Menu.QUIT)
            ));
        }

        return this.mainMenu;
    }

    /**
     * Produces and reuses movement menu.
     *
     * @return movement menu
     */
    public Menu getMovementMenu() {
        if (movementMenu == null) {
            this.movementMenu = create(Arrays.asList(
                    new MoveCommand(Direction.NORTH),
                    new MoveCommand(Direction.EAST),
                    new MoveCommand(Direction.SOUTH),
                    new MoveCommand(Direction.WEST),
                    new MenuCommand(MenuCommand.Menu.DUMP),
                    new MenuCommand(MenuCommand.Menu.PAUSE)
            ));
        }

        return this.movementMenu;
    }

    /**
     * Produces and reuses pause menu.
     *
     * @return pause menu
     */
    public Menu getPauseMenu() {
        if (pauseMenu == null) {
            this.pauseMenu = create(Arrays.asList(
                    new MenuCommand(MenuCommand.Menu.RESUME),
                    new MenuCommand(MenuCommand.Menu.DUMP),
                    new MenuCommand(MenuCommand.Menu.NEW),
                    new MenuCommand(MenuCommand.Menu.SAVE),
                    new MenuCommand(MenuCommand.Menu.LOAD),
                    new MenuCommand(MenuCommand.Menu.QUIT)
            ));
        }

        return this.pauseMenu;
    }

    /**
     * Produces and reuses fight menu.
     *
     * @return fight menu
     */
    public Menu getFightMenu() {
        if (this.fightMenu == null) {
            this.fightMenu = create(Arrays.asList(
                    new FightCommand(FightCommand.Action.ATTACK),
                    new FightCommand(FightCommand.Action.RETREAT)
            ));
        }

        return this.fightMenu;
    }

    /**
     * Produces load game menu displaying saved games filenames.
     *
     * @param commands List of <code>LoadMenuCommand</code>s
     * @return load game menu
     */
    public Menu getLoadMenu(List<Command> commands) {
        return create(commands);
    }

    private Menu create(List<Command> commands) {
        return new Menu(io, commandDispatcher, commands);
    }
}
