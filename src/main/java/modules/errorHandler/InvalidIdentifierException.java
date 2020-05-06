package modules.errorHandler;

public class InvalidIdentifierException extends InvalidTokenException {

    public InvalidIdentifierException(String lineContent, String invalidText, int lineNumber, int signPosition) {
        super(lineContent, invalidText, lineNumber, signPosition);
    }
}
