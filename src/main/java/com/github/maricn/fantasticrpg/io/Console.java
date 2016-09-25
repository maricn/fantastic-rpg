package com.github.maricn.fantasticrpg.io;

import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.map.Field;
import com.github.maricn.fantasticrpg.model.map.Map;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nikola on 2016-09-20.
 *
 * @author nikola
 */
public class Console implements InputOutput {

    private PrintStream printStream;

    private BufferedReader bufferedReader;
    private Scanner in;

    public Console() {
        this(System.out, System.in);
    }

    public Console(PrintStream printStream, InputStream inputStream) {
        this.printStream = printStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.in = new Scanner(inputStream);
    }

    static class ANSI {
        public static final String RESET = "\u001B[0m";
        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";
        public static final String CLS = "\u001B[2J";
        public static final String HOME = "\u001B[H";
    }

    @Override
    public void clear() {
        printStream.print(ANSI.CLS + ANSI.HOME);
        printStream.flush();
    }

    @Override
    public void write(String text) {
        printStream.print(text);
    }

    @Override
    public void writeCommands(List<Command> commands) {
        printStream.printf(ANSI.RESET + "Please choose one of the following:%n");
        String choices = commands.get(0).getMenuOption();
        for (int i = 1; i < commands.size(); i++) {
            choices += " | " + commands.get(i).getMenuOption();
        }

        printStream.print(choices + "\n");
        printStream.flush();
    }

    @Override
    public void error(String message) {
        write(ANSI.RED + message + ANSI.RESET);
    }

    @Override
    public void dumpMap(Map map) {
        clear();
        writeHorizontalEdge(map.getWidth());

        for (int i = 0; i < map.getHeight(); i++) {
            printStream.printf("|");
            for (int j = 0; j < map.getWidth(); j++) {
                try {
                    printStream.printf(stringifyMapField(map.getField(i, j)));
                } catch (FantasticRpgException e) {
                    error(e.getMessage());
                }
            }

            printStream.printf(ANSI.RED + "|%n");
        }

        writeHorizontalEdge(map.getWidth());
        printStream.flush();
    }

    @Override
    public String read() {
        return in.nextLine();
    }

    @Override
    public char readChar() {
        try {
            int read = bufferedReader.read();
            return (char) read;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static String stringifyMapField(Field field) {
        // @TODO: debug?
//        if (!field.getExplored()) {
//            return ANSI.RED + '?';
//        }

        if (field.getOccupying() != null && field.getOccupying() instanceof Monster) {
            Monster monster = (Monster) field.getOccupying();
            switch (monster.getType()) {
                case BAT:
                    return ANSI.WHITE + 'B';
                case DEICIDE:
                    return ANSI.BLACK + 'D';
                case ELF:
                    return ANSI.GREEN + 'E';
                case ROBOT:
                    return ANSI.GREEN + 'R';
                case SHARK:
                    return ANSI.BLUE + 'S';
                case WIZARD:
                    return ANSI.PURPLE + 'W';
                default:
                    return ANSI.RED + '?';
            }
        }

        if (field.getOccupying() != null && field.getOccupying() instanceof Player) {
            return ANSI.YELLOW + '@';
        }

        switch (field.getType()) {
            case WATER:
                return ANSI.CYAN + '~';
            case WALL:
                return ANSI.WHITE + '#';
            case EMPTY:
            default:
                return ANSI.RESET + ' ';
        }
    }

    private void writeHorizontalEdge(int width) {
        printStream.printf(ANSI.RED + "+");
        for (int i = 0; i < width; i++) {
            printStream.printf("-");
        }

        printStream.printf("+%n" + ANSI.RESET);
        printStream.flush();
    }
}
