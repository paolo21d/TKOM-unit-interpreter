package tkom.errorHandler;

public class InvalidStringException extends InvalidTokenException {
    public InvalidStringException(String lineContent, String invalidText, int lineNumber, int signPosition) {
        super(lineContent, invalidText, lineNumber, signPosition);
    }
}
