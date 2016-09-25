package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.Command;
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
        this.io = io;
        this.commandDispatcher = commandDispatcher;
        this.commands = commands;
    }

    public void interact() {
//        io.clear();
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

//            if (!outputBeforeInput && command == null) {
//                io.writeCommands(commands);
//            }
        } while (null == command);

        commandDispatcher.offer(command);
    }
}
