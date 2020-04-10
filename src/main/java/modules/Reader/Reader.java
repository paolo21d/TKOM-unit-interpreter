package modules.Reader;

import lombok.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Data
public class Reader {
/*    private FileReader fileReader;
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
    }*/

    private BufferedReader reader;
    private String currentLine="";
    private Long currentLineNumber = 0L;
    private int lastCharIndex=0;

    public Reader(String fileName) throws FileNotFoundException {
        reader = new BufferedReader( new FileReader(fileName));
    }

    public char getChar() throws IOException {
        lastCharIndex++;
        if( lastCharIndex >= currentLine.length()) {
            currentLineNumber++;
            currentLine = reader.readLine();
            lastCharIndex = 0;
            if(currentLine == null) { //EndOfFile
                return 0;
            }
        }
        if(currentLine.length() == 0){
            return getChar();
        }else {
            return currentLine.charAt(lastCharIndex);
        }
    }

    public void ungetChar() {
        lastCharIndex--;
    }
}
