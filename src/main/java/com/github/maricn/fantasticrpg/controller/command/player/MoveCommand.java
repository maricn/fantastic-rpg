package com.github.maricn.fantasticrpg.controller.command.player;

import com.github.maricn.fantasticrpg.controller.command.AbstractHandledCommand;
import com.github.maricn.fantasticrpg.controller.command.CommandHandler;

/**
 *
 * @author nikola
 */
public class MoveCommand extends AbstractHandledCommand implements ActionCommand {

    private final Direction moveDirection;

    public MoveCommand(CommandHandler handler, Direction moveDirection) {
        super(handler);
        this.moveDirection = moveDirection;
    }

    @Override
    public String getName() {
        return moveDirection.getMenuOption();
    }

    @Override
    public char getAbbreviation() {
        return moveDirection.getAbbreviation();
    }

    public Direction getMoveDirection() {
        return moveDirection;
    }
}
