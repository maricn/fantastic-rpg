package com.github.maricn.fantasticrpg.command.menu;

import com.github.maricn.fantasticrpg.command.Command;
import com.github.maricn.fantasticrpg.command.CommandDispatcher;
import com.github.maricn.fantasticrpg.command.menu.model.LoadMenuCommand;
import com.github.maricn.fantasticrpg.command.menu.model.MenuCommand;
import com.github.maricn.fantasticrpg.io.InputOutput;
import com.github.maricn.fantasticrpg.model.GameState;
import com.github.maricn.fantasticrpg.model.GameStateInfo;
import com.github.maricn.fantasticrpg.model.character.Player;
import com.github.maricn.fantasticrpg.model.exception.FantasticRpgException;
import com.github.maricn.fantasticrpg.model.map.Map;
import com.github.maricn.fantasticrpg.model.map.MapFactory;
import com.github.maricn.fantasticrpg.repository.GameStateRepository;
import com.github.maricn.fantasticrpg.ui.Menu;
import com.github.maricn.fantasticrpg.ui.MenuFactory;
import com.github.maricn.fantasticrpg.util.TestWorldBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static com.github.maricn.fantasticrpg.command.menu.model.MenuCommand.Menu.*;
import static com.github.maricn.fantasticrpg.command.menu.model.MenuCommand.Menu.NEW;
import static com.github.maricn.fantasticrpg.model.GameState.State.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by nikola on 2016-09-27.
 *
 * @author nikola
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(MockitoJUnitRunner.class)
public class MenuCommandHandlerTest {

    @Mock
    private GameState gameState;

    @Mock
    private InputOutput io;

    @Mock
    private CommandDispatcher commandDispatcher;

    @Mock
    private MenuFactory menuFactory;

    @Mock
    private MapFactory mapFactory;

    @Mock
    private GameStateRepository gameStateRepository;

    @InjectMocks
    private MenuCommandHandler menuCommandHandler;

    @Test
    public void testNew() {
        Command[] dispatched = new Command[1];
        doAnswer(invocation -> dispatched[0] = (Command) invocation.getArguments()[0])
                .when(commandDispatcher).offer(any(Command.class));
        doNothing().when(gameState).setPlayer(any(Player.class));
        when(gameState.getState())
                .thenReturn(PAUSED)
                .thenReturn(GameState.State.NEW);
        when(io.read())
                .thenReturn("TestPlayerName")
                .thenReturn("badLevelNumber!@#$%^&*(")
                .thenReturn("16");
        Player player = TestWorldBuilder.buildPlayer();
        when(gameState.getPlayer())
                .thenReturn(null)
                .thenReturn(player);
        when(mapFactory.generateMap(anyInt()))
                .thenReturn(TestWorldBuilder.build2By3Map(player));
        doNothing().when(gameState).setState(any(GameState.State.class));
        doNothing().when(gameState).setMap(any(Map.class));
        doNothing().when(gameState).setPlayer(any(Player.class));

        MenuCommand cmd = new MenuCommand(NEW);
        menuCommandHandler.executeCommand(cmd);

        assertNotNull(dispatched[0]);
        assertTrue(dispatched[0] instanceof MenuCommand);
        assertEquals(NEW, ((MenuCommand) dispatched[0]).getMenu());

        menuCommandHandler.executeCommand((MenuCommand) dispatched[0]);

        verify(io, atLeastOnce()).write(anyString());
        verify(io, atLeast(2)).read();
        verify(mapFactory, times(1)).generateMap(eq(16));
        verify(commandDispatcher, times(1)).offer(eq(dispatched[0]));
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(RESUME)));
        verifyNoMoreInteractions(menuFactory);
    }

    @Test
    public void testQuit() {
        menuCommandHandler.executeCommand(new MenuCommand(QUIT));

        verify(io, atLeast(0)).write(anyString());
        verifyNoMoreInteractions(gameState, commandDispatcher, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testResume() {
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getMovementMenu()).thenReturn(menu);

        menuCommandHandler.executeCommand(new MenuCommand(RESUME));

        verify(io, atLeast(0)).write(anyString());
        verify(gameState, atLeast(0)).setState(eq(ROAMING));
        verify(menuFactory, times(1)).getMovementMenu();
        verifyNoMoreInteractions(commandDispatcher, gameState, io, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testLoad() {
        Instant now = Instant.now();
        when(gameStateRepository.getAllGameStateInfos())
                .thenReturn(Arrays.asList(
                        new GameStateInfo(now, "save0", "p0"),
                        new GameStateInfo(now, "save1", "p1")
                ));
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getLoadMenu(anyListOf(Command.class))).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            List<Command> list = (List<Command>) invocation.getArguments()[0];
            assertNotNull(list);
            assertEquals(3, list.size());
            assertTrue(list.get(0).getMenuOption().contains("p0"));
            assertTrue(list.get(1).getMenuOption().contains("p1"));
            return menu;
        });

        menuCommandHandler.executeCommand(new MenuCommand(LOAD));

        verify(gameStateRepository, times(1)).getAllGameStateInfos();
        verify(menuFactory, times(1)).getLoadMenu(any());
        verifyNoMoreInteractions(gameState, io, commandDispatcher, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testLoadGame() throws FantasticRpgException {
        Instant now = Instant.now();
        when(gameStateRepository.getAllGameStateInfos())
                .thenReturn(Arrays.asList(
                        new GameStateInfo(now, "save0", "p0"),
                        new GameStateInfo(now, "save1", "p1"),
                        new GameStateInfo(now, "save2", "p2")
                ));
        GameState builtGameState = TestWorldBuilder.buildGameState();
        when(gameStateRepository.load(any(GameStateInfo.class)))
                .thenAnswer(invocation -> {
                    assertTrue("save2".equals(((GameStateInfo) invocation.getArguments()[0]).getSaveName()));
                    return builtGameState;
                });
        doNothing().when(this.gameState).setState(any(GameState.State.class));
        doNothing().when(this.gameState).setMap(any(Map.class));
        doNothing().when(this.gameState).setPlayer(any(Player.class));

        LoadMenuCommand lmc = new LoadMenuCommand('2', "save2");
        menuCommandHandler.executeCommand(lmc);

        verify(this.gameState, times(1)).setMap(eq(builtGameState.getMap()));
        verify(this.gameState, times(1)).setPlayer(eq(builtGameState.getPlayer()));
        verify(this.gameState, times(1)).setState(eq(builtGameState.getState()));
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(RESUME)));
        verify(gameStateRepository, times(1)).getAllGameStateInfos();
        verify(gameStateRepository, times(1)).load(any(GameStateInfo.class));
        verifyNoMoreInteractions(gameState, io, commandDispatcher, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testLoadGameError() throws FantasticRpgException {
        Instant now = Instant.now();
        when(gameStateRepository.getAllGameStateInfos())
                .thenReturn(Arrays.asList(
                        new GameStateInfo(now, "save0", "p0"),
                        new GameStateInfo(now, "save1", "p1"),
                        new GameStateInfo(now, "save2", "p2")
                ));
        when(gameStateRepository.load(any(GameStateInfo.class)))
                .thenThrow(new FantasticRpgException("TEST ERROR MESSAGE"));
        when(gameState.getState()).thenReturn(PAUSED);

        LoadMenuCommand lmc = new LoadMenuCommand('2', "save2");
        menuCommandHandler.executeCommand(lmc);

        verify(io, atLeastOnce()).error(eq("TEST ERROR MESSAGE"));
        verify(commandDispatcher, times(1)).offer(eq(new MenuCommand(RESUME)));
        verify(gameStateRepository, times(1)).getAllGameStateInfos();
        verify(gameStateRepository, times(1)).load(any(GameStateInfo.class));
        verifyNoMoreInteractions(io, commandDispatcher, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testSaveInGame() {
        when(gameState.getState()).thenReturn(PAUSED);
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getPauseMenu()).thenReturn(menu);

        menuCommandHandler.executeCommand(new MenuCommand(SAVE));

        verify(gameStateRepository, times(1)).save(eq(gameState));
        verify(gameState, times(1)).getState();
        verify(menuFactory, times(1)).getPauseMenu();
        verify(menu, times(1)).interact();
        verifyNoMoreInteractions(gameState, commandDispatcher, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testSaveInMainMenu() {
        when(gameState.getState()).thenReturn(GameState.State.NEW);
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getMainMenu()).thenReturn(menu);

        menuCommandHandler.executeCommand(new MenuCommand(SAVE));

        verify(gameStateRepository, times(1)).save(eq(gameState));
        verify(gameState, times(1)).getState();
        verify(menuFactory, times(1)).getMainMenu();
        verify(menu, times(1)).interact();
        verifyNoMoreInteractions(gameState, commandDispatcher, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testPause() {
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getPauseMenu()).thenReturn(menu);

        menuCommandHandler.executeCommand(new MenuCommand(PAUSE));

        verify(io, atLeast(0)).write(anyString());
        verify(gameState, atLeast(0)).setState(eq(PAUSED));
        verify(menuFactory, times(1)).getPauseMenu();
        verifyNoMoreInteractions(commandDispatcher, gameState, io, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testDump() {
        Player player = TestWorldBuilder.buildPlayer();
        Map map = TestWorldBuilder.buildExploredMap();
        when(gameState.getMap()).thenReturn(map);
        when(gameState.getPlayer()).thenReturn(player);
        doNothing().when(io).dumpMap(any(Map.class));
        doNothing().when(io).dumpPlayer(any(Player.class));

        menuCommandHandler.executeCommand(new MenuCommand(DUMP));

        verify(io, times(1)).dumpMap(eq(map));
        verify(io, times(1)).dumpPlayer(eq(player));
        verify(commandDispatcher, times(1)).offer(new MenuCommand(RESUME));
        verify(gameState, atLeastOnce()).getMap();
        verify(gameState, atLeastOnce()).getPlayer();
        verifyNoMoreInteractions(commandDispatcher, gameState, io, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testFight() {
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getFightMenu()).thenReturn(menu);

        menuCommandHandler.executeCommand(new MenuCommand(FIGHT));

        verify(menu, times(1)).interact();
        verify(menuFactory, times(1)).getFightMenu();
        verify(gameState, atLeast(0)).setState(FIGHTING);
        verifyNoMoreInteractions(commandDispatcher, gameState, io, menuFactory, mapFactory, gameStateRepository);
    }

    @Test
    public void testMainMenu() {
        Menu menu = Mockito.mock(Menu.class);
        doNothing().when(menu).interact();
        when(menuFactory.getMainMenu()).thenReturn(menu);

        menuCommandHandler.executeCommand(new MenuCommand(MAIN));

        verify(menu, times(1)).interact();
        verify(menuFactory, times(1)).getMainMenu();
        verifyNoMoreInteractions(commandDispatcher, gameState, io, menuFactory, mapFactory, gameStateRepository);
    }
}
