package com.github.maricn.fantasticrpg.command.player.model;

/**
 * Command representing action in fight mode.
 *
 * @author nikola
 */
public class FightCommand implements ActionCommand {
    private Action action;

    public FightCommand(Action action) {
        this.action = action;
    }

    @Override
    public String getName() {
        return action.menuOption;
    }

    @Override
    public char getAbbreviation() {
        return action.abbreviation;
    }

    public enum Action {
        ATTACK('A', "Attack"), RETREAT('R', "Retreat"), ENGAGE('E', "Engage");

        private final char abbreviation;
        private final String menuOption;

        Action(char abbreviation, String menuOption) {
            this.abbreviation = abbreviation;
            this.menuOption = menuOption;
        }
    }

    public Action getAction() {
        return this.action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FightCommand that = (FightCommand) o;

        return action == that.action;
    }

    @Override
    public int hashCode() {
        return action != null ? action.hashCode() : 0;
    }
}
