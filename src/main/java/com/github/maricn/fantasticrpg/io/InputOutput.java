package com.github.maricn.fantasticrpg.io;

import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.controller.command.Command;

import java.util.List;

/**
 *
 * @author nikola
 */
public interface InputOutput {
    void clear();

    void write(String text);

    void writeCommands(List<Command> commands);

    void error(String message);

    void dumpMap(Map map);

    String read();

    char readChar();
}
