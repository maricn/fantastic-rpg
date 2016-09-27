package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.command.CommandDispatcher;
import com.github.maricn.fantasticrpg.io.InputOutput;

import java.util.List;

/**
 * Handles user interaction. Reads user's commands and writes out instructions/options.
 *
 * @author nikola
 */
public class Menu {

    protected InputOutput io;
    protected CommandDispatcher commandDispatcher;

    protected List<Command> commands;

    public Menu(InputOutput io, CommandDispatcher commandDispatcher, List<Command> commands) {
        if (commands == null || commands.isEmpty()) throw new RuntimeException("No commands passed to menu!");
        this.io = io;
        this.commandDispatcher = commandDispatcher;
        this.commands = commands;
    }

    /**
     * Outputs options and reads user input. When user has successfully selected an option,
     * forwards command to command dispatcher for execution.
     */
    public void interact() {
        Command command = null;
        boolean skipOut = false;
        do {
            if (!skipOut) {
                io.writeCommands(commands);
            }

            char chosen = Character.toUpperCase(io.readChar());
            if (chosen == '\n') {
                skipOut = true;
            }
            if (chosen < 48 || chosen > 122) {
                continue;
            }

            for (Command cmd : commands) {
                if ((chosen == cmd.getAbbreviation())) {
                    command = cmd;
                    break;
                }
            }
        } while (null == command);

        commandDispatcher.offer(command);
    }
}
