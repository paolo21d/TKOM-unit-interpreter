package modules.data;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(" TokenType: ").append(type.toString());
        if (type.equals(TokenType.NumberLiteral)) {
            builder.append(" LiteralNumber: ").append(literalNumber);
        } else if (type.equals(TokenType.Identifier)) {
            builder.append(" Value: ").append(value);
        }
        return builder.toString();
    }
}
