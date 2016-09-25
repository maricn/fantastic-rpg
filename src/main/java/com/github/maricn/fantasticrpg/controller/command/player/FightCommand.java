package com.github.maricn.fantasticrpg.controller.command.player;

import com.github.maricn.fantasticrpg.controller.command.AbstractHandledCommand;
import com.github.maricn.fantasticrpg.controller.command.CommandHandler;


/**
 *
 * @author nikola
 */
public class FightCommand extends AbstractHandledCommand implements ActionCommand {
    private Action action;
    private Direction direction;

    public FightCommand(CommandHandler commandHandler, Action action, Direction direction) {
        super(commandHandler);
        this.action = action;
        this.direction = direction;
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

    public Direction getFightDirection() {
        return direction;
    }
}
