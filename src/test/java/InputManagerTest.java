import modules.InputManager.InputManager;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class InputManagerTest {

    @Test(expected = FileNotFoundException.class)
    public void notExistingFile() throws FileNotFoundException {
        InputManager inputManager = new InputManager(new FileReader(""));
    }

    @Test
    public void checkEOF() throws IOException {
        InputManager input = new InputManager(new StringReader(""));
        assertEquals('\uFFFF', input.getNextChar());
        assertEquals('\uFFFF', input.getNextChar());
    }

    @Test
    public void checkNextChar() throws IOException {
        String value = "AB C\nDEF\n GH ";
        InputManager input = new InputManager(new StringReader(value));
        for (char sign : value.toCharArray()) {
            assertEquals(sign, input.getNextChar());
        }
    }

    @Test
    public void checkPeekChar() throws IOException {
        String value = "AB C\nDEF\n GH ";
        InputManager input = new InputManager(new StringReader(value));
        for (char sign : value.toCharArray()) {
            assertEquals(sign, input.peekChar());
            input.getNextChar();
        }
    }

    @Test
    public void checkLineNumber() throws IOException {
        InputManager input = new InputManager(new StringReader("A\nB\nC"));
        assertEquals(1, input.getLineNumber());
        input.getNextChar();
        input.getNextChar();
        assertEquals(2, input.getLineNumber());
        input.getNextChar();
        input.getNextChar();
        assertEquals(3, input.getLineNumber());
    }

    @Test
    public void checkSignPosition() throws IOException {
        InputManager input = new InputManager(new StringReader("ABC\nDEF"));
        assertEquals(0, input.getSignPosition());
        input.getNextChar();
        assertEquals(1, input.getSignPosition());
        input.getNextChar();
        assertEquals(2, input.getSignPosition());
        input.getNextChar();
        assertEquals(3, input.getSignPosition());
        input.getNextChar();
        assertEquals(0, input.getSignPosition());
    }

    @Test
    public void checkCurrentLineContent() throws IOException {
        InputManager input = new InputManager(new StringReader("ABC\nDEF"));
        input.getNextChar();
        assertEquals("A", input.getCurrentLine());
        input.getNextChar();
        assertEquals("AB", input.getCurrentLine());
        input.getNextChar();
        assertEquals("ABC", input.getCurrentLine());
        input.getNextChar();
        assertEquals("", input.getCurrentLine());
        input.getNextChar();
        assertEquals("D", input.getCurrentLine());
        input.getNextChar();
        assertEquals("DE", input.getCurrentLine());
        input.getNextChar();
        assertEquals("DEF", input.getCurrentLine());
    }
}
