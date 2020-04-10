package modules.Data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type) {
        this.type = type;
    }
}
