package com.github.maricn.fantasticrpg.controller.command;

/**
 *
 * @author nikola
 */
public abstract class AbstractHandledCommand implements Command {
    private final CommandHandler handler;

    public AbstractHandledCommand(CommandHandler commandHandler) {
        this.handler = commandHandler;
    }

    @Override
    public <T extends Command> CommandHandler<T> getHandler() {
        return this.handler;
    }
}
