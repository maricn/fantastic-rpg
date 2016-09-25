package com.github.maricn.fantasticrpg.controller.command.player;

import com.github.maricn.fantasticrpg.controller.command.Command;

/**
 * @author nikola
 */
public interface ActionCommand extends Command {
    @Override
    default boolean isActionCommand() {
        return true;
    }
}
