package com.github.maricn.fantasticrpg.io;

import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.map.Map;

import java.util.List;

/**
 * Handles input and output of a game, ie. reading user input and writing out game output.
 *
 * @author nikola
 */
public interface InputOutput {

    /**
     * Clear output.
     */
    void clear();

    /**
     * Write {@code text} to output.
     *
     * @param text text to write
     */
    void write(String text);

    /**
     * Write {@code commands} available in menu to output.
     *
     * @param commands available commands
     */
    void writeCommands(List<Command> commands);

    /**
     * Write {@code message} as error to output.
     *
     * @param message error
     */
    void error(String message);

    /**
     * Write out {@code map} to output.
     *
     * @param map map
     */
    void dumpMap(Map map);

    /**
     * Write out {@code player} stats to output.
     *
     * @param player player
     */
    void dumpPlayer(Player player);

    /**
     * Read user input.
     *
     * @return user input
     */
    String read();

    /**
     * Read user input, one character.
     *
     * @return user input char
     */
    char readChar();
}
