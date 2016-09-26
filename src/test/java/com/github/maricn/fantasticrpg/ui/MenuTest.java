package com.github.maricn.fantasticrpg.ui;

import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.controller.command.menu.MenuCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-26.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class MenuTest {

    private Menu menu;

    @Mock
    private InputOutput io;

    @Mock
    private CommandDispatcher commandDispatcher;

    private Command newCommand = new MenuCommand(MenuCommand.Menu.NEW);
    private Command dumpCommand = new MenuCommand(MenuCommand.Menu.DUMP);

    @Before
    public void setUp() {
        io = Mockito.mock(InputOutput.class);
        commandDispatcher = Mockito.mock(CommandDispatcher.class);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testFirstOfTwo_AndNewline() {
        when(io.readChar())
                .thenReturn('\n')
                .thenReturn('\n')
                .thenReturn(MenuCommand.Menu.NEW.getAbbr());

        List<Command> commands = Arrays.asList(newCommand, dumpCommand);
        menu = new Menu(io, commandDispatcher, commands);
        menu.interact();

        verify(io, times(1)).writeCommands(commands);
        verify(commandDispatcher, times(1)).offer(newCommand);
        verifyNoMoreInteractions(commandDispatcher);
    }

    @Test
    public void testSingle_AndSymbolAndWrong() {
        when(io.readChar())
                .thenReturn(' ')
                .thenReturn('!')
                .thenReturn('{')
                .thenReturn('|')
                .thenReturn((char) (MenuCommand.Menu.NEW.getAbbr() + 1))
                .thenReturn(MenuCommand.Menu.NEW.getAbbr());

        List<Command> commands = Collections.singletonList(newCommand);
        menu = new Menu(io, commandDispatcher, commands);
        menu.interact();

        verify(io, atLeastOnce()).writeCommands(commands);
        verify(commandDispatcher, times(1)).offer(newCommand);
        verifyNoMoreInteractions(commandDispatcher);
    }

    @Test
    public void testNone() {
        Exception exc = null;
        try {
            menu = new Menu(io, commandDispatcher, Collections.emptyList());
            menu.interact();
        } catch (Exception e) {
            exc = e;
        }

        assertNotNull(exc);
    }
}
