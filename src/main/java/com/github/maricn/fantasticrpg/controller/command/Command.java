package com.github.maricn.fantasticrpg.controller.command;

/**
 * Command used in game. Can be gaming (moving, fighting, etc.) or menu (map, save, load, etc.) command.
 *
 * @author nikola
 */
public interface Command {

    /**
     * Returns command name (as to be displayed in menu).
     *
     * @return command name
     */
    String getName();

    /**
     * Returns command short name (single character, shortcut for UI).
     *
     * @return command abbreviation
     */
    char getAbbreviation();

    /**
     * Composes display name for command using abbreviation and full command name.
     *
     * @return command's display name for menus
     */
    default String getMenuOption() {
        return "(" + getAbbreviation() + ") " + getName();
    }

}
