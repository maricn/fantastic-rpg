package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.controller.CommandExecutor;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.controller.command.Command;

import java.util.List;

/**
 * Created by nikola on 2016-09-22.
 *
 * @author nikola
 */
public class Menu {

    protected InputOutput io;
    protected CommandExecutor commandExecutor;

    protected List<Command> commands;

    public Menu(InputOutput io, CommandExecutor commandExecutor, List<Command> commands) {
        this.io = io;
        this.commandExecutor = commandExecutor;
        this.commands = commands;
    }

    public void interact() {
//        io.clear();
        Command command = null;
        do {
            char chosen = Character.toUpperCase(io.readChar());
            if (chosen < 48 || chosen > 122) {
                continue;
            }

            for (Command cmd : commands) {
                if ((chosen == cmd.getAbbreviation())) {
                    command = cmd;
                    break;
                }
            }

            if (command == null) {
                io.writeCommands(commands);
            }
        } while (null == command);

        commandExecutor.exec(command);
    }
}
