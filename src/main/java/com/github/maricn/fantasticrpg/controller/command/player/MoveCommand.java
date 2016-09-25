package com.github.maricn.fantasticrpg.controller.command.player;

/**
 * Command representing player movement action.
 *
 * @author nikola
 */
public class MoveCommand implements ActionCommand {

    private final Direction moveDirection;

    public MoveCommand(Direction moveDirection) {
        this.moveDirection = moveDirection;
    }

    @Override
    public String getName() {
        return moveDirection.getName();
    }

    @Override
    public char getAbbreviation() {
        return moveDirection.getAbbreviation();
    }

    /**
     * Returns movement direction.
     *
     * @return movement direction
     */
    public Direction getMoveDirection() {
        return moveDirection;
    }
}
