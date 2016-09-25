package com.github.maricn.fantasticrpg.controller;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author nikola
 */
public class CommandExecutor implements Runnable {

    private Queue<Command> commands = new ArrayDeque<>();

    public void exec(Command command) {
        commands.offer(command);
    }

    // gameplay moving or in fight
    // always go back to gameplay
    public void run() {
        while (Main.getRunning() && !commands.isEmpty()) {
            Command command = commands.poll();
            try {
                command.getHandler().executeCommand(command);
            } catch (FantasticRpgException e) {
                e.printStackTrace();
            }
        }
    }
}
