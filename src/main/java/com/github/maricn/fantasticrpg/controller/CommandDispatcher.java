package com.github.maricn.fantasticrpg.controller;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommandHandler;
import com.github.maricn.fantasticrpg.controller.command.player.ActionCommand;
import com.github.maricn.fantasticrpg.controller.command.player.ActionCommandHandler;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Class representing game's main loop. Dispatches queued commands to the appropriate command executors.
 *
 * @author nikola
 */
public class CommandDispatcher {

    private Queue<Command> commands = new ArrayDeque<>();
    private MenuCommandHandler menuCommandHandler;
    private ActionCommandHandler actionCommandHandler;

    /**
     * Put a command in a command queue.
     *
     * @param command command to be put in queue
     */
    public void offer(Command command) {
        commands.offer(command);
    }

    /**
     * Main game loop.
     */
    public void run() {
        while (Main.getRunning() && !commands.isEmpty()) {
            Command command = commands.poll();
            try {
                dispatch(command);
            } catch (FantasticRpgException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(Command cmd) throws FantasticRpgException {
        if (cmd instanceof MenuCommand) {
            menuCommandHandler.executeCommand((MenuCommand) cmd);
        } else {
            actionCommandHandler.executeCommand((ActionCommand) cmd);
        }
    }

    public void setActionCommandHandler(ActionCommandHandler actionCommandHandler) {
        this.actionCommandHandler = actionCommandHandler;
    }

    public void setMenuCommandHandler(MenuCommandHandler menuCommandHandler) {
        this.menuCommandHandler = menuCommandHandler;
    }
}
