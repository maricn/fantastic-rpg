package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.controller.CommandExecutor;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.controller.command.CommandHandler;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.controller.command.player.Direction;
import com.github.maricn.fantasticrpg.controller.command.player.FightCommand;
import com.github.maricn.fantasticrpg.controller.command.player.MoveCommand;

import java.util.Arrays;
import java.util.List;

/**
 * @author nikola
 */
@SuppressWarnings("unchecked")
public class MenuFactory {

    private CommandHandler actionCommandHandler, menuCommandHandler;

    public void setActionCommandHandler(CommandHandler actionCommandHandler) {
        this.actionCommandHandler = actionCommandHandler;
    }

    public void setMenuCommandHandler(CommandHandler menuCommandHandler) {
        this.menuCommandHandler = menuCommandHandler;
    }

    private InputOutput io;
    private CommandExecutor commandExecutor;

    private Menu mainMenu, movementMenu, pauseMenu;

    public MenuFactory(InputOutput io, CommandExecutor commandExecutor) {
        this.io = io;
        this.commandExecutor = commandExecutor;
    }

    public Menu getMainMenu() {
        if (mainMenu == null) {
            this.mainMenu = create(Arrays.asList(
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.NEW),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.SAVE),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.LOAD),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.QUIT)
            ));
        }

        return this.mainMenu;
    }

    public Menu getMovementMenu() {
        if (movementMenu == null) {
            this.movementMenu = create(Arrays.asList(
                    new MoveCommand(actionCommandHandler, Direction.NORTH),
                    new MoveCommand(actionCommandHandler, Direction.EAST),
                    new MoveCommand(actionCommandHandler, Direction.SOUTH),
                    new MoveCommand(actionCommandHandler, Direction.WEST),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.DUMP),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.PAUSE)
            ));
        }

        return this.movementMenu;
    }

    public Menu getPauseMenu() {
        if (pauseMenu == null) {
            this.pauseMenu = create(Arrays.asList(
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.RESUME),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.DUMP),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.NEW),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.SAVE),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.LOAD),
                    new MenuCommand(menuCommandHandler, MenuCommand.Menu.QUIT)
            ));
        }

        return this.pauseMenu;
    }

    public Menu getFightMenu(Direction direction) {
        return create(Arrays.asList(
                new FightCommand(actionCommandHandler, FightCommand.Action.ATTACK, direction),
                new FightCommand(actionCommandHandler, FightCommand.Action.RETREAT, direction)
        ));
    }

    public Menu create(List<Command> commands) {
        return new Menu(io, commandExecutor, commands);
    }
}
