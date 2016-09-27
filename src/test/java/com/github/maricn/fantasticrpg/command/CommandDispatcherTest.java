package com.github.maricn.fantasticrpg.command;

import com.github.maricn.fantasticrpg.Main;
import com.github.maricn.fantasticrpg.command.CommandDispatcher;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.command.menu.MenuCommandHandler;
import com.github.maricn.fantasticrpg.command.player.*;
import com.github.maricn.fantasticrpg.command.player.model.ActionCommand;
import com.github.maricn.fantasticrpg.command.player.model.Direction;
import com.github.maricn.fantasticrpg.command.player.model.FightCommand;
import com.github.maricn.fantasticrpg.command.player.model.MoveCommand;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-27.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class CommandDispatcherTest {

    private CommandDispatcher commandDispatcher;

    @Mock
    private ActionCommandHandler actionCommandHandler;

    @Mock
    private MenuCommandHandler menuCommandHandler;

    @Before
    public void setUp() {
        commandDispatcher = new CommandDispatcher();
        actionCommandHandler = Mockito.mock(ActionCommandHandler.class);
        commandDispatcher.setActionCommandHandler(actionCommandHandler);
        menuCommandHandler = Mockito.mock(MenuCommandHandler.class);
        commandDispatcher.setMenuCommandHandler(menuCommandHandler);
        Main.setRunning(true);
    }

    @After
    public void tearDown() {
        Mockito.reset(actionCommandHandler, menuCommandHandler);
    }

    @Test
    public void testSingleAction() throws FantasticRpgException {
        doNothing().when(actionCommandHandler).executeCommand(any(ActionCommand.class));

        ActionCommand cmd = new MoveCommand(Direction.NORTH);
        commandDispatcher.offer(cmd);
        commandDispatcher.run();

        verify(actionCommandHandler, times(1)).executeCommand(eq(cmd));
        verifyNoMoreInteractions(menuCommandHandler);
    }

    @Test
    public void testMultiMenu() throws FantasticRpgException {
        doNothing()
                .when(menuCommandHandler)
                .executeCommand(any(MenuCommand.class));

        MenuCommand cmd1 = new MenuCommand(MenuCommand.Menu.PAUSE);
        commandDispatcher.offer(cmd1);
        MenuCommand cmd2 = new MenuCommand(MenuCommand.Menu.FIGHT);
        commandDispatcher.offer(cmd2);
        commandDispatcher.run();

        verify(menuCommandHandler, times(1)).executeCommand(eq(cmd1));
        verify(menuCommandHandler, times(1)).executeCommand(eq(cmd2));
        verifyNoMoreInteractions(actionCommandHandler);
    }

    @Test
    public void testMultiMenuNotRunning() throws FantasticRpgException {
        MenuCommand cmd1 = new MenuCommand(MenuCommand.Menu.PAUSE);
        commandDispatcher.offer(cmd1);
        MenuCommand cmd2 = new MenuCommand(MenuCommand.Menu.RESUME);
        commandDispatcher.offer(cmd2);
        Main.setRunning(false);
        commandDispatcher.run();

        verifyNoMoreInteractions(actionCommandHandler);
        verifyNoMoreInteractions(menuCommandHandler);
    }
}
