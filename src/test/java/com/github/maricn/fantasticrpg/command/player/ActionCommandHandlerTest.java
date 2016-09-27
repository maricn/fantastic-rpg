package com.github.maricn.fantasticrpg.command.player;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.command.CommandDispatcher;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.command.player.model.Direction;
import com.github.maricn.fantasticrpg.command.player.model.FightCommand;
import com.github.maricn.fantasticrpg.command.player.model.MoveCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.Ability;
import com.github.maricn.fantasticrpg.model.character.Monster;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.map.Field;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.util.TestWorldBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-25.
 *
 * @author nikola
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionCommandHandlerTest {

    private ActionCommandHandler actionCommandHandler;

    @Mock
    private InputOutput io;

    @Mock
    private CommandDispatcher commandDispatcher;

    @Mock
    private GameState gameState;

    @Mock
    private Random random;

    @Before
    public void setUp() {
        Main.setRunning(true);

        doNothing().when(gameState).setMap(any(Map.class));
        doNothing().when(gameState).setPlayer(any(Player.class));
        doNothing().when(gameState).setState(any(GameState.State.class));

        actionCommandHandler = new ActionCommandHandler(random, gameState, io, commandDispatcher);
    }

    @Test
    public void testEngage() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildFightMapEasy(player));

        FightCommand cmd = new FightCommand(FightCommand.Action.ENGAGE);
        actionCommandHandler.executeCommand(cmd);

        verify(commandDispatcher, times(1)).offer(new MenuCommand(MenuCommand.Menu.FIGHT));
        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testRetreat() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildFightMapEasy(player));

        FightCommand cmd = new FightCommand(FightCommand.Action.RETREAT);
        actionCommandHandler.executeCommand(cmd);

        verify(gameState, times(1)).setState(eq(GameState.State.ROAMING));
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testAttackAndKillAndFinishLevel() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildFightMapEasy(player));
        when(random.nextInt(anyInt()))
                .thenReturn(100)
                .thenReturn(0);

        FightCommand cmd = new FightCommand(FightCommand.Action.ATTACK);
        actionCommandHandler.executeCommand(cmd);

        verify(gameState, times(1)).setState(GameState.State.NEW);
        verify(commandDispatcher, times(1)).offer(new MenuCommand(MenuCommand.Menu.NEW));
        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testAttackAndKill() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        // Add another monster to map, right beside player (his facing direction)
        Field field = map.getField(player.getCurrY() + player.getFacing().getDeltaY(), player.getCurrX() + player.getFacing().getDeltaX());
        field.setOccupying(TestWorldBuilder.buildEasyMonster(Monster.MonsterType.ELF));
        when(gameState.getMap()).thenReturn(map);
        when(random.nextInt(anyInt()))
                .thenReturn(100)
                .thenReturn(0);

        FightCommand cmd = new FightCommand(FightCommand.Action.ATTACK);
        actionCommandHandler.executeCommand(cmd);

        verify(gameState, times(1)).setState(GameState.State.ROAMING);
        verify(commandDispatcher, times(1)).offer(new MenuCommand(MenuCommand.Menu.RESUME));
        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testAttack() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildFightMapEasy(player));
        when(random.nextInt(anyInt()))
                .thenReturn(0)
                .thenReturn(0);

        FightCommand cmd = new FightCommand(FightCommand.Action.ATTACK);
        actionCommandHandler.executeCommand(cmd);

        verify(io, atLeast(0)).write(anyString());
        verify(commandDispatcher, times(1)).offer(new MenuCommand(MenuCommand.Menu.FIGHT));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testAttackAndGetKilled() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildFightMapEasy(player));
        when(random.nextInt(anyInt()))
                .thenReturn(0)
                .thenReturn(999);

        FightCommand cmd = new FightCommand(FightCommand.Action.ATTACK);
        actionCommandHandler.executeCommand(cmd);

        assertFalse(Main.getRunning());

        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testAttackAndKillAndGetAbilitiesAndFinishLevel() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildFightMapHard(player));
        when(random.nextInt(anyInt()))
                .thenReturn(999_999)
                .thenReturn(0);

        FightCommand cmd = new FightCommand(FightCommand.Action.ATTACK);
        actionCommandHandler.executeCommand(cmd);

        for (Ability ability : Ability.values()) {
            assertTrue(player.getAbilities().contains(ability));
        }

        verify(gameState, times(1)).setState(GameState.State.NEW);
        verify(commandDispatcher, times(1)).offer(new MenuCommand(MenuCommand.Menu.NEW));
        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveOverEdge() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.NORTH));

        assertEquals(oldPlayerX, player.getCurrX());
        assertEquals(oldPlayerY, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(io, atLeast(1)).error(anyString());
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToWallNoGodmode() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        map.getField(0, 1).setOccupying(null);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.EAST));

        assertEquals(oldPlayerX, player.getCurrX());
        assertEquals(oldPlayerY, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(io, atLeast(1)).error(anyString());
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToWallWithGodmode() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        player.getAbilities().add(Ability.GODMODE);
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        map.getField(0, 1).setOccupying(null);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.EAST));

        assertEquals(oldPlayerX + 1, player.getCurrX());
        assertEquals(oldPlayerY, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToWallWithGodmodeAndFight() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        player.getAbilities().add(Ability.GODMODE);
        player.setExperience(999_999);
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.EAST));

        assertEquals(oldPlayerX, player.getCurrX());
        assertEquals(oldPlayerY, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(gameState, atLeastOnce()).setState(eq(GameState.State.FIGHTING));
        verify(commandDispatcher, times(1)).offer(eq(new FightCommand(FightCommand.Action.ENGAGE)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToWallWithGodmodeButNoExperience() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        player.getAbilities().add(Ability.GODMODE);
        player.setExperience(0);
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.EAST));

        assertEquals(oldPlayerX, player.getCurrX());
        assertEquals(oldPlayerY, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(io, atLeast(1)).error(anyString());
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToWaterWithSwimming() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        player.getAbilities().add(Ability.SWIMMING);
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.SOUTH));
        actionCommandHandler.executeCommand(new MoveCommand(Direction.EAST));

        assertEquals(oldPlayerX + 1, player.getCurrX());
        assertEquals(oldPlayerY + 1, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(commandDispatcher, times(2)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToWaterNoSwimming() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.SOUTH));
        actionCommandHandler.executeCommand(new MoveCommand(Direction.EAST));

        assertEquals(oldPlayerX, player.getCurrX());
        assertEquals(oldPlayerY + 1, player.getCurrY());

        verify(io, atLeast(0)).write(anyString());
        verify(io, atLeast(1)).error(anyString());
        verify(commandDispatcher, times(2)).offer(eq(new MenuCommand(MenuCommand.Menu.RESUME)));
        verifyNoMoreInteractions(io, commandDispatcher);
    }

    @Test
    public void testMoveToEmpty() throws FantasticRpgException {
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        Map map = TestWorldBuilder.build2By3Map(player);
        when(gameState.getMap()).thenReturn(map);

        int oldPlayerX = player.getCurrX(), oldPlayerY = player.getCurrY();
        actionCommandHandler.executeCommand(new MoveCommand(Direction.SOUTH));

        assertEquals(oldPlayerX, player.getCurrX());
        assertEquals(oldPlayerY + 1, player.getCurrY());

        verify(commandDispatcher, times(1)).offer(new MenuCommand(MenuCommand.Menu.RESUME));
        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(io, commandDispatcher);
    }
}
