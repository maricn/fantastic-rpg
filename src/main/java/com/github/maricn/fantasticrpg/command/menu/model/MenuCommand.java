package com.github.maricn.fantasticrpg.command.menu.model;

import com.github.maricn.fantasticrpg.command.Command;

/**
 * Menu option command.
 *
 * @author nikola
 */
public class MenuCommand implements Command {

    /**
     * Enum representing different menu options.
     */
    public enum Menu {
        NEW("New game", 'G'),
        PAUSE("Pause", 'P'),
        RESUME("Resume", 'R'),
        SAVE("Save", 'S'),
        LOAD("Load", 'L'),
        QUIT("Quit", 'Q'),
        DUMP("Dump", 'M'),
        MAIN(),
        LOADGAME(),
        FIGHT();

        private String name;
        private char abbr;

        Menu(String name, char abbr) {
            this.name = name;
            this.abbr = abbr;
        }

        Menu() {
        }

        public String getName() {
            return name;
        }

        public char getAbbr() {
            return abbr;
        }
    }

    private final Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public MenuCommand(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String getName() {
        return menu.name;
    }

    @Override
    public char getAbbreviation() {
        return menu.abbr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuCommand that = (MenuCommand) o;

        return menu == that.menu;
    }

    @Override
    public int hashCode() {
        return menu != null ? menu.hashCode() : 0;
    }
}
