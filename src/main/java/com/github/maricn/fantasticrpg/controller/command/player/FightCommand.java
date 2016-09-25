package com.github.maricn.fantasticrpg.controller.command.player;

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

}
