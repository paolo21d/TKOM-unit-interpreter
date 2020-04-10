package modules.Reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private FileReader fileReader;
    private Character ungetedChar = null;
    private Character lastGetedChar = null;

    public Reader(String fileName) throws FileNotFoundException {
        fileReader = new FileReader(fileName);
    }

    public char getChar() throws IOException {
        char toReturn;
        if (ungetedChar != null) {
            lastGetedChar = ungetedChar;
            toReturn = ungetedChar;
            ungetedChar = null;
        } else {
            int result = fileReader.read();
            if (result == -1) {
                toReturn = 0;
            } else {
                lastGetedChar = (char) result;
                toReturn = (char) result;
            }
        }
        return toReturn;
    }

    public void ungetChar() {
        ungetedChar = lastGetedChar;
    }
}
