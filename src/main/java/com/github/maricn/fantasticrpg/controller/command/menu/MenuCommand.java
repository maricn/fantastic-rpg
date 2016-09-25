package com.github.maricn.fantasticrpg.controller.command.menu;

import com.github.maricn.fantasticrpg.controller.command.Command;

/**
 * @author nikola
 */
// @TODO: refactor to class with field of which command (save/load, pause) instead of interface and
// multiple impls (https://www.cuttingedge.it/blogs/steven/pivot/entry.php?id=91)
public class MenuCommand implements Command {
    public enum Menu {
        NEW("New game", 'G'),
        PAUSE("Pause", 'P'),
        RESUME("Resume", 'R'),
        SAVE("Save", 'S'),
        LOAD("Load", 'L'),
        QUIT("Quit", 'Q'),
        DUMP("Dump", 'M'),
        MAIN(),
        FIGHT();

        private String name;
        private char abbr;

        Menu(String name, char abbr) {
            this.name = name;
            this.abbr = abbr;
        }

        Menu() {
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

}
