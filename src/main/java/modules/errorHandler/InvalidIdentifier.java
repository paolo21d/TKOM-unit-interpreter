package modules.errorHandler;

public class InvalidIdentifier extends InvalidToken {

    public InvalidIdentifier(String lineContent, String invalidText, int lineNumber, int signPosition) {
        super(lineContent, invalidText, lineNumber, signPosition);
    }
}
