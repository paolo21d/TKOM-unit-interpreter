package modules.ErrorHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidToken extends Exception {
    private String lineContent;
    private String invalidText;
    private int lineNumber;
    private int signPosition;
}
