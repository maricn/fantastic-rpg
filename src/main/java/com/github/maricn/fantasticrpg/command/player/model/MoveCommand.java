package com.github.maricn.fantasticrpg.command.player.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoveCommand that = (MoveCommand) o;

        return moveDirection == that.moveDirection;

    }

    @Override
    public int hashCode() {
        return moveDirection != null ? moveDirection.hashCode() : 0;
    }
}
