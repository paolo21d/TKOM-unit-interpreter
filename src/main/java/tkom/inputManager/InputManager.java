package tkom.inputManager;

import lombok.Getter;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class InputManager {
    private PushbackReader reader;
    @Getter
    private int lineNumber = 1;
    @Getter
    private int signPosition = 0;
    private StringBuilder currentLineBuilder = new StringBuilder();

    public InputManager(Reader reader) {
        this.reader = new PushbackReader(reader);
    }

    public char getNextChar() throws IOException {
        char sign = (char) reader.read();
        if (sign == '\n') {
            lineNumber++;
            signPosition = 0;
            currentLineBuilder = new StringBuilder();
            return sign;
        }
        signPosition++;
        currentLineBuilder.append(sign);
        return sign;
    }

    public char peekChar() throws IOException {
        int pickedSign = reader.read();
        reader.unread(pickedSign);
        return (char) pickedSign;
    }

    public String getCurrentLine() {
        return currentLineBuilder.toString();
    }
}
