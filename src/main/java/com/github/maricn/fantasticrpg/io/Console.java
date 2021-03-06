package com.github.maricn.fantasticrpg.io;

import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.model.character.Ability;
import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.map.Field;
import com.github.maricn.fantasticrpg.model.map.Map;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * {@link InputOutput} implementation which writes out stuff to
 * {@code System.out} and accepts user input from {@code System.in} by default.
 *
 * @author nikola
 */
@SuppressWarnings("RedundantStringFormatCall")
public class Console implements InputOutput {

    private PrintStream printStream;

    private BufferedReader bufferedReader;
    private Scanner in;

    public Console() {
        this(System.out, System.in);
    }

    Console(PrintStream printStream, InputStream inputStream) {
        this.printStream = printStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.in = new Scanner(inputStream);
    }

    static class ANSI {
        static final String RESET = "\u001B[0m";
        static final String BLACK = "\u001B[30m";
        static final String RED = "\u001B[31m";
        static final String GREEN = "\u001B[32m";
        static final String YELLOW = "\u001B[33m";
        static final String BLUE = "\u001B[34m";
        static final String PURPLE = "\u001B[35m";
        static final String CYAN = "\u001B[36m";
        static final String WHITE = "\u001B[37m";
        static final String CLS = "\u001B[2J";
        static final String HOME = "\u001B[H";
    }

    @Override
    public void clear() {
        printStream.printf(ANSI.CLS + ANSI.HOME);
        printStream.flush();
    }

    @Override
    public void write(String text) {
        printStream.printf(text);
    }

    @Override
    public void writeCommands(List<Command> commands) {
        printStream.printf(ANSI.YELLOW + "Please choose one of the following:%n");
        StringBuilder choices = new StringBuilder(commands.get(0).getMenuOption());
        for (int i = 1; i < commands.size(); i++) {
            if (choices.length() > 100) {
                printStream.printf(choices + " | %n");
                choices = new StringBuilder();
            }

            choices.append(" | ").append(commands.get(i).getMenuOption());
        }

        printStream.printf(choices + "%n" + ANSI.RESET);
        printStream.flush();
    }

    @Override
    public void error(String message) {
        write(ANSI.RED + message + ANSI.RESET + '\n');
    }

    @Override
    public void dumpMap(Map map) {
        printStream.printf(ANSI.RED + getHorizontalEdge(map.getWidth()) + "+%n");

        for (int i = 0; i < map.getHeight(); i++) {
            printStream.printf("|");
            for (int j = 0; j < map.getWidth(); j++) {
                printStream.printf(stringifyMapField(map.getField(i, j)));
            }

            printStream.printf(ANSI.RED + "|%n");
        }

        printStream.printf(ANSI.RED + getHorizontalEdge(map.getWidth()) + "+%n");
        printStream.flush();
    }

    @Override
    public void dumpPlayer(Player player) {
        if (null == player) return;
        printStream.printf("Player: \t" + player.getName() + "%n");
        printStream.printf("Health points: \t" + player.getHealthPoints() +
                " \t| \tExperience: \t" + player.getExperience() +
                " \t| \tDamage: \t" + player.getDamage() + "%n");
        if (player.getAbilities() != null && !player.getAbilities().isEmpty()) {
            printStream.printf("Abilities: {");
            Iterator<Ability> iterator = player.getAbilities().iterator();
            printStream.printf(iterator.next().name());
            for (; iterator.hasNext(); ) {
                Ability ability = iterator.next();
                printStream.printf(" | " + ability.name());
            }
            printStream.printf("}%n");
        }
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
            this.error(e.getMessage());
        }

        return 0;
    }

    private static String stringifyMapField(Field field) {
        if (!field.getExplored()) {
            return ANSI.RED + '?';
        }

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

    private String getHorizontalEdge(int width) {
        StringBuilder edge = new StringBuilder("+");
        for (int i = 0; i < width; i++) {
            edge.append("-");
        }

        return edge.toString();
    }
}
