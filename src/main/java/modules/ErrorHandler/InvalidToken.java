package modules.ErrorHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidToken extends Exception {
    private String lineContent;
    private int lineNumber;
}
