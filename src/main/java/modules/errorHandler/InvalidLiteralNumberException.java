package modules.errorHandler;


public class InvalidLiteralNumberException extends InvalidTokenException {

    public InvalidLiteralNumberException(String lineContent, String invalidText, int lineNumber, int signPosition) {
        super(lineContent, invalidText, lineNumber, signPosition);
    }
}
