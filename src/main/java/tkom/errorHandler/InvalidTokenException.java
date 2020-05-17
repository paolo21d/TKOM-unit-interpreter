package tkom.errorHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidTokenException extends Exception {
    private String lineContent;
    private String invalidText;
    private int lineNumber;
    private int signPosition;

    @Override
    public String getMessage() {
        return String.format("Invalid token: %s\nLine number %d Position %d\nLine: %s", invalidText, lineNumber, signPosition, lineContent);
    }
}
