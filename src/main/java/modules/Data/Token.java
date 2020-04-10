package modules.Data;

import lombok.Data;

@Data
public class Token {
    private TokenType type;
    private String value;
    private Double literalNumber;

    public Token(TokenType type) {
        this.type = type;
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token(TokenType type, Double literalNumber) {
        this.type = type;
        this.literalNumber = literalNumber;
    }
}
