package com.github.maricn.fantasticrpg.controller.command.player;

import com.github.maricn.fantasticrpg.controller.CommandDispatcher;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
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
    private CommandDispatcher commandDispatcher;

    @Mock
    private Menu menu;

    @Mock
    private GameState gameState;

    @Before
    public void setUp() {
        menu = Mockito.mock(Menu.class);
        MenuFactory menuFactory = Mockito.mock(MenuFactory.class);
        when(menuFactory.getMainMenu()).thenReturn(menu);
        when(menuFactory.getFightMenu()).thenReturn(menu);
        when(menuFactory.getPauseMenu()).thenReturn(menu);

        io = Mockito.mock(InputOutput.class);
        commandDispatcher = Mockito.mock(CommandDispatcher.class);
        gameState = Mockito.mock(GameState.class);

        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer()).thenReturn(player);
        when(gameState.getMap()).thenReturn(TestWorldBuilder.buildMap(player));

        actionCommandHandler = new ActionCommandHandler(gameState, io, menuFactory, commandDispatcher);
    }

    @After
    public void tearDown() {
        Mockito.reset(io, commandDispatcher, menu, gameState);
    }

    @Test
    public void testEngage() throws FantasticRpgException {
        FightCommand cmd = new FightCommand(FightCommand.Action.ENGAGE);
        actionCommandHandler.executeCommand(cmd);

        verifyNoMoreInteractions(commandDispatcher);
        verify(menu, times(1)).interact();
    }

    @Test
    public void testRetreat() throws FantasticRpgException {
        FightCommand cmd = new FightCommand(FightCommand.Action.RETREAT);
        actionCommandHandler.executeCommand(cmd);

        verifyNoMoreInteractions(commandDispatcher);
        verify(menu, times(1)).interact();
    }
}
