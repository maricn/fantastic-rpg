package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.controller.command.menu.LoadMenuCommand;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.controller.command.player.Direction;
import com.github.maricn.fantasticrpg.controller.command.player.FightCommand;
import com.github.maricn.fantasticrpg.controller.command.player.MoveCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by nikola on 2016-09-26.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class MenuFactoryTest {

    private MenuFactory menuFactory;

    @Mock
    private InputOutput io;

    @Mock
    private CommandDispatcher commandDispatcher;

    @Before
    public void setUp() {
        io = Mockito.mock(InputOutput.class);
        commandDispatcher = Mockito.mock(CommandDispatcher.class);
        menuFactory = new MenuFactory(io, commandDispatcher);
    }

    @Test
    public void testFightMenu() {
        Menu fightMenu = menuFactory.getFightMenu();

        assertNotNull(fightMenu);
        assertEquals(commandDispatcher, fightMenu.commandDispatcher);
        assertEquals(io, fightMenu.io);
        assertTrue(fightMenu.commands.containsAll(
                Arrays.asList(
                        new FightCommand(FightCommand.Action.ATTACK),
                        new FightCommand(FightCommand.Action.RETREAT)
                )
        ));
        assertTrue(fightMenu.commands.stream()
                .allMatch(command -> null != command.getName() && command.getAbbreviation() > 0));

//        assertEquals(fightMenu, menuFactory.getFightMenu());
    }

    @Test
    public void testMainMenu() {
        Menu mainMenu = menuFactory.getMainMenu();

        assertNotNull(mainMenu);
        assertEquals(commandDispatcher, mainMenu.commandDispatcher);
        assertEquals(io, mainMenu.io);
        assertTrue(mainMenu.commands.containsAll(
                Arrays.asList(
                        new MenuCommand(MenuCommand.Menu.NEW),
                        new MenuCommand(MenuCommand.Menu.SAVE),
                        new MenuCommand(MenuCommand.Menu.LOAD),
                        new MenuCommand(MenuCommand.Menu.QUIT)
                )
        ));
        assertTrue(mainMenu.commands.stream()
                .allMatch(command -> null != command.getName() && command.getAbbreviation() > 0));

//        assertEquals(mainMenu, menuFactory.getMainMenu());
    }

    @Test
    public void testMovementMenu() {
        Menu movementMenu = menuFactory.getMovementMenu();

        assertNotNull(movementMenu);
        assertEquals(commandDispatcher, movementMenu.commandDispatcher);
        assertEquals(io, movementMenu.io);
        assertTrue(movementMenu.commands.containsAll(
                Arrays.asList(
                        new MoveCommand(Direction.EAST),
                        new MoveCommand(Direction.NORTH),
                        new MoveCommand(Direction.WEST),
                        new MoveCommand(Direction.SOUTH)
                )
        ));
        assertTrue(movementMenu.commands.stream()
                .allMatch(command -> null != command.getName() && command.getAbbreviation() > 0));

//        assertEquals(movementMenu, menuFactory.getMovementMenu());
    }

    @Test
    public void testPauseMenu() {
        Menu movementMenu = menuFactory.getPauseMenu();

        assertNotNull(movementMenu);
        assertEquals(commandDispatcher, movementMenu.commandDispatcher);
        assertEquals(io, movementMenu.io);
        assertTrue(movementMenu.commands.containsAll(
                Arrays.asList(
                        new MenuCommand(MenuCommand.Menu.NEW),
                        new MenuCommand(MenuCommand.Menu.SAVE),
                        new MenuCommand(MenuCommand.Menu.LOAD),
                        new MenuCommand(MenuCommand.Menu.QUIT)
                )
        ));
        assertTrue(movementMenu.commands.stream()
                .allMatch(command -> null != command.getName() && command.getAbbreviation() > 0));
    }

    @Test
    public void testLoadMenu() {
        List<Command> loadMenuCommands = Arrays.asList(
                new LoadMenuCommand('0', "0"),
                new LoadMenuCommand('1', "1"),
                new LoadMenuCommand('2', "0"),
                new LoadMenuCommand('3', "0")
        );

        Menu movementMenu = menuFactory.getLoadMenu(loadMenuCommands);

        assertNotNull(movementMenu);
        assertEquals(commandDispatcher, movementMenu.commandDispatcher);
        assertEquals(io, movementMenu.io);
        assertArrayEquals(loadMenuCommands.toArray(), movementMenu.commands.toArray());
    }
}
