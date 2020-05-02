package modules.ErrorHandler;


public class InvalidLiteralNumber extends InvalidToken {

    public InvalidLiteralNumber(String lineContent, String invalidText, int lineNumber, int signPosition) {
        super(lineContent, invalidText, lineNumber, signPosition);
    }
}
