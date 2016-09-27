package com.github.maricn.fantasticrpg.command;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.command.menu.MenuCommandHandler;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.command.player.ActionCommandHandler;
import com.github.maricn.fantasticrpg.command.player.model.ActionCommand;

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
            Command cmd = commands.poll();
            if (cmd instanceof MenuCommand) {
                menuCommandHandler.executeCommand((MenuCommand) cmd);
            } else {
                if (cmd instanceof ActionCommand) {
                    actionCommandHandler.executeCommand((ActionCommand) cmd);
                }
            }
        }
    }

    public void setActionCommandHandler(ActionCommandHandler actionCommandHandler) {
        this.actionCommandHandler = actionCommandHandler;
    }

    public void setMenuCommandHandler(MenuCommandHandler menuCommandHandler) {
        this.menuCommandHandler = menuCommandHandler;
    }
}
