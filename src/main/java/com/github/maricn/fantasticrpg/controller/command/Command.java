package com.github.maricn.fantasticrpg.controller.command;

/**
 * Created by nikola on 2016-09-22.
 *
 * @author nikola
 */
public interface Command {

    String getName();

    char getAbbreviation();

    default String getMenuOption() {
        return "(" + getAbbreviation() + ") " + getName();
    }

    default boolean isActionCommand() {
        return false;
    }

    <T extends Command> CommandHandler<T> getHandler();
}
