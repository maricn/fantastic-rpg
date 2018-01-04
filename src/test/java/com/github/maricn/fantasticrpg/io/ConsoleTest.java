package com.github.maricn.fantasticrpg.io;

import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.command.menu.model.LoadMenuCommand;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.command.player.model.Direction;
import com.github.maricn.fantasticrpg.command.player.model.FightCommand;
import com.github.maricn.fantasticrpg.command.player.model.MoveCommand;
import com.github.maricn.fantasticrpg.model.character.Ability;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.util.TestWorldBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-25.
 *
 * @author nikola
 */
@SuppressWarnings("RedundantStringFormatCall")
@RunWith(JUnit4.class)
public class ConsoleTest {

    private InputOutput io;

    @Mock
    private PrintStream printStream;

    @Before
    public void setUp() {
        printStream = Mockito.mock(PrintStream.class);
        InputStream inputStream = new ByteArrayInputStream("cafebabe".getBytes());
        io = new Console(printStream, inputStream);
    }

    @After
    public void tearDown() {
        Mockito.reset(printStream);
    }

    @Test
    public void testReadChar() {
        char ch = io.readChar();

        assertEquals('c', ch);
    }

    @Test
    public void testRead() {
        String read = io.read();

        assertEquals("cafebabe", read);
    }

    @Test
    public void testClear() {
        io.clear();

        verify(printStream, atLeastOnce()).flush();
    }

    @Test
    public void testDumpMap() {
        Map map = TestWorldBuilder.buildExploredMap();
        io.dumpMap(map);

        verify(printStream, times(2)).printf(contains("+--+"));
        verify(printStream, atLeastOnce()).printf(contains("|"));
        verify(printStream, atLeastOnce()).printf(contains("@"));
        verify(printStream, atLeastOnce()).printf(contains("~"));
        verify(printStream, atLeastOnce()).printf(contains("S"));
        verify(printStream, atLeastOnce()).printf(contains("D"));
        verify(printStream, atLeastOnce()).printf(contains("R"));
    }

    @Test
    public void testDumpPlayer() {
        Player player = new Player(123, 456, 789, "playerName", 1, 1, new HashSet<Ability>() {
            {
                add(Ability.SWIMMING);
                add(Ability.GODMODE);
            }
        });
        io.dumpPlayer(player);

        verify(printStream).printf(contains("123"));
        verify(printStream).printf(contains("456"));
        verify(printStream).printf(contains("789"));
        verify(printStream).printf(contains("playerName"));
        verify(printStream).printf(contains("SWIMMING"));
        verify(printStream).printf(contains("GODMODE"));
    }

    @Test
    public void testCommands() {
        FightCommand fightCommand = new FightCommand(FightCommand.Action.ATTACK);
        List<Command> commands = Arrays.asList(
                new MenuCommand(MenuCommand.Menu.NEW),
                fightCommand,
                new LoadMenuCommand('3', "Three"),
                new LoadMenuCommand('5', "Very long load menu command for testing new line if text is too long... :)"),
                new MoveCommand(Direction.SOUTH)
        );

        io.writeCommands(commands);

        verify(printStream, atLeastOnce()).printf(contains(MenuCommand.Menu.NEW.getName()));
        verify(printStream, atLeastOnce()).printf(contains(String.valueOf(MenuCommand.Menu.NEW.getAbbr())));
        verify(printStream, atLeastOnce()).printf(contains(fightCommand.getMenuOption()));
        verify(printStream, atLeastOnce()).printf(contains("Three"));
        verify(printStream, atLeastOnce()).printf(contains("3"));
        verify(printStream, atLeastOnce()).printf(contains(Direction.SOUTH.getName()));
        verify(printStream, atLeastOnce()).printf(contains(String.valueOf(Direction.SOUTH.getAbbreviation())));
    }

    @Test
    public void testWrite() {
        String s = "testTexgtasvo\\@$241";

        io.write(s);

        verify(printStream, times(1)).printf(contains(s));
    }

    @Test
    public void testError() {
        String s = "testTexgtasvo\\@$241";

        io.error(s);

        verify(printStream, times(1)).printf(contains(s));
    }
}
