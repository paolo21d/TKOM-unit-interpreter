package modules.data;

import lombok.Data;

@Data
public class Token {
    private TokenType type;
    private String value;
    private Double literalNumber;
    private int lineNumber;
    private int signPosition;
    private String lineContent;

    public Token(TokenType type, int lineNumber, int signPosition, String lineContent) {
        this.type = type;
        this.lineNumber = lineNumber;
        this.signPosition = signPosition;
        this.lineContent = lineContent;
    }

    public Token(TokenType type, String value, int lineNumber, int signPosition, String lineContent) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
        this.signPosition = signPosition;
        this.lineContent = lineContent;
    }

    public Token(TokenType type, Double literalNumber, int lineNumber, int signPosition, String lineContent) {
        this.type = type;
        this.literalNumber = literalNumber;
        this.lineNumber = lineNumber;
        this.signPosition = signPosition;
        this.lineContent = lineContent;
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
