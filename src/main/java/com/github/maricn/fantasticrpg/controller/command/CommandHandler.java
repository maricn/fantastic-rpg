package com.github.maricn.fantasticrpg.controller.command;

import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;

/**
 * Interface for all <code>CommandHandler</code>s.
 *
 * @author nikola
 */
public interface CommandHandler<T extends Command> {
    /**
     * Executes specified command.
     *
     * @param command command to be executed
     * @throws FantasticRpgException
     */
    void executeCommand(T command) throws FantasticRpgException;
}
