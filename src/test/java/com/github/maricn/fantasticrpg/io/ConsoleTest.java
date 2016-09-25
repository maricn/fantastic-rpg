package com.github.maricn.fantasticrpg.io;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by nikola on 2016-09-25.
 *
 * @author nikola
 */
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
    public void testReadChar() throws IOException {
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
}
