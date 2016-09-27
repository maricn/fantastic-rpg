package com.github.maricn.fantasticrpg.command.menu.model;

/**
 * Command representing load game action.
 *
 * @author nikola
 */
public class LoadMenuCommand extends MenuCommand {

    private char choice;
    private String saveName;

    public LoadMenuCommand(char choice, String saveName) {
        super(Menu.LOADGAME);
        this.choice = choice;
        this.saveName = saveName;
    }

    @Override
    public String getName() {
        return saveName;
    }

    @Override
    public char getAbbreviation() {
        return choice;
    }

    public char getChoice() {
        return choice;
    }

    public void setChoice(char choice) {
        this.choice = choice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoadMenuCommand that = (LoadMenuCommand) o;

        return choice == that.choice;
    }

    @Override
    public int hashCode() {
        return choice;
    }
}
