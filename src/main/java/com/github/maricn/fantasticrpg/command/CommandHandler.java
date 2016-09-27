package com.github.maricn.fantasticrpg.command;

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
     */
    void executeCommand(T command);
}
