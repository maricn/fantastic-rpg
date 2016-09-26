package com.github.maricn.fantasticrpg.controller.command.player;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.CommandHandler;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.Ability;
import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.AbilityNotFoundException;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.map.Field;
import com.github.maricn.fantasticrpg.model.map.FieldType;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.ui.MenuFactory;

import java.time.Instant;
import java.util.Random;

/**
 * Handles gaming commands, ie. moving player, attacking monsters, etc.
 *
 * @author nikola
 */
public class ActionCommandHandler implements CommandHandler<ActionCommand> {

    // Fights PRNG different from fixed seeds level generator PRNG
    private final Random random = new Random(Instant.now().getEpochSecond());

    private final GameState gameState;
    private final InputOutput io;
    private final MenuFactory menuFactory;
    private final CommandDispatcher commandDispatcher;

    public ActionCommandHandler(GameState gameState, InputOutput io, MenuFactory menuFactory, CommandDispatcher commandDispatcher) {
        this.gameState = gameState;
        this.io = io;
        this.menuFactory = menuFactory;
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void executeCommand(ActionCommand command) throws FantasticRpgException {
        try {
            if (command instanceof MoveCommand) executeCommand((MoveCommand) command);
            if (command instanceof FightCommand) executeCommand((FightCommand) command);
        } catch (FantasticRpgException exception) {
            io.error(exception.getMessage());
            commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.RESUME));
//            menuFactory.getMovementMenu().interact();
        }
    }

    private void executeCommand(MoveCommand command) throws FantasticRpgException {
        Player player = gameState.getPlayer();
        int newX = player.getCurrX() + command.getMoveDirection().getDeltaX();
        int newY = player.getCurrY() + command.getMoveDirection().getDeltaY();
        player.setFacing(command.getMoveDirection());

        Map map = gameState.getMap();
        Field field;
        try {
            field = map.getField(newY, newX);
        } catch (IndexOutOfBoundsException ignored) {
            return;
        }

        if (FieldType.WATER.equals(field.getType()) && !player.getAbilities().contains(Ability.SWIMMING)) {
            throw new AbilityNotFoundException(Ability.SWIMMING);
        }
        if (FieldType.WALL.equals(field.getType()) && !player.getAbilities().contains(Ability.GODMODE)) {
            throw new AbilityNotFoundException(Ability.GODMODE);
        }

        if (field.getOccupying() != null) {
            Monster monster = (Monster) field.getOccupying();
            if (monster.getMinXp() > player.getExperience()) {
                throw new FantasticRpgException("Not enough experience to fight this monster. minXP = " + monster.getMinXp());
            }

            gameState.setState(GameState.State.FIGHTING);
            commandDispatcher.offer(new FightCommand(FightCommand.Action.ENGAGE));
            return;
        }

        map.getField(player.getCurrY(), player.getCurrX()).setOccupying(null);
        player.setCurrX(newX);
        player.setCurrY(newY);
        map.explore(newY, newX);
        map.getField(newY, newX).setOccupying(player);

        menuFactory.getMovementMenu().interact();
    }

    private void executeCommand(FightCommand command) throws FantasticRpgException {
        Player player = gameState.getPlayer();
        Map map = gameState.getMap();
        Field targetField = map.getField(
                player.getCurrY() + player.getFacing().getDeltaY(),
                player.getCurrX() + player.getFacing().getDeltaX()
        );
        Monster monster = (Monster) targetField.getOccupying();
        switch (command.getAction()) {
            case ENGAGE:
                io.write("Your stats: " + player + "\n");
                io.write("You are facing " + monster + "\n");
                menuFactory.getFightMenu().interact();
                break;
            case ATTACK:
                int damage = random.nextInt(player.getDamage());
                io.write("Your attack made " + damage + " damage!\n");
                monster.setHealthPoints(monster.getHealthPoints() - damage);

                if (monster.getHealthPoints() <= 0) {
                    // Experience gain from monster
                    player.setExperience(player.getExperience() + monster.getExperience());

                    // Damage increase when killing, up to third of monster XP
                    player.setDamage(player.getDamage() + random.nextInt(monster.getExperience() / 3));

                    // Life steal when killing (amount damage)
                    player.setHealthPoints(player.getHealthPoints() + damage);

                    map.setNumOfMonsters(map.getNumOfMonsters() - 1);
                    map.getField(player.getCurrY(), player.getCurrX()).setOccupying(null);
                    player.setCurrX(player.getCurrX() + player.getFacing().getDeltaX());
                    player.setCurrY(player.getCurrY() + player.getFacing().getDeltaY());
                    targetField.setOccupying(player);
                    io.write("You killed the evil " + monster.getType() + "!\n");
                    if (map.getNumOfMonsters() == 0) {
                        io.write("Congratulations! You completed the level!\n");
                        gameState.setState(GameState.State.PAUSED);
                        menuFactory.getMainMenu().interact();
                        break;
                    } else {
                        gameState.setState(GameState.State.ROAMING);
                        menuFactory.getMovementMenu().interact();
                        //                        menuFactory.getMovementMenu().interact();
                        break;
                    }
                } else {
                    int monsterDamage = random.nextInt(monster.getDamage());
                    io.write(monster.getType().name() + " made " + monsterDamage + " damage!\n");
                    player.setHealthPoints(player.getHealthPoints() - monsterDamage);
                    if (player.getHealthPoints() <= 0) {
                        io.write("You were killed!!!");
                        Main.setRunning(false);
                        return;
                    }
                }

                io.write("Your stats: " + player + "\n");
                io.write("You are facing " + monster + "\n");
                commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.FIGHT));
//                menuFactory.getFightMenu(command.getFightDirection()).interact();
                break;
            case RETREAT:
                gameState.setState(GameState.State.ROAMING);
                commandDispatcher.offer(new MenuCommand(MenuCommand.Menu.RESUME));
//                menuFactory.getMovementMenu().interact();
                break;
        }
    }
}
