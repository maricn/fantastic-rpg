package com.github.maricn.fantasticrpg.controller.command.player;

import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.controller.CommandExecutor;
import com.github.maricn.fantasticrpg.controller.command.Command;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.ui.Menu;
import com.github.maricn.fantasticrpg.ui.MenuFactory;
import com.github.maricn.fantasticrpg.util.TestWorldBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-25.
 *
 * @author nikola
 */
@RunWith(JUnit4.class)
public class ActionCommandHandlerTest {
    private ActionCommandHandler actionCommandHandler;

    @Mock
    private InputOutput io;

    @Mock
    private CommandExecutor commandExecutor;

    @Mock
    private Menu menu;

    @Mock
    private GameState gameState;

    @Before
    public void setUp() {
        menu = Mockito.mock(Menu.class);
        MenuFactory menuFactory = Mockito.mock(MenuFactory.class);
        when(menuFactory.getMainMenu()).thenReturn(menu);
        when(menuFactory.getFightMenu(any(Direction.class))).thenReturn(menu);
        when(menuFactory.getPauseMenu()).thenReturn(menu);
        when(menuFactory.create(anyListOf(Command.class))).thenReturn(menu);

        io = Mockito.mock(InputOutput.class);
        commandExecutor = Mockito.mock(CommandExecutor.class);
        gameState = Mockito.mock(GameState.class);

        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildMap(player));

        actionCommandHandler = new ActionCommandHandler(gameState, io, menuFactory, commandExecutor);
    }

    @After
    public void tearDown() {
        Mockito.reset(io, commandExecutor, menu, gameState);
    }

    @Test
    public void testEngage() throws FantasticRpgException {

        FightCommand cmd = new FightCommand(actionCommandHandler, FightCommand.Action.ENGAGE, Direction.EAST);
        actionCommandHandler.executeCommand(cmd);

        verifyNoMoreInteractions(commandExecutor);
        verify(menu, times(1)).interact();
    }
}
