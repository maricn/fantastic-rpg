package com.github.maricn.fantasticrpg.controller.command;

import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

/**
 *
 * @author nikola
 */
public interface CommandHandler<T extends Command> {
    void executeCommand(T command) throws FantasticRpgException;
}
