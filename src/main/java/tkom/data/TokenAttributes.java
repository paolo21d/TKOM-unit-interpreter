package tkom.data;

import java.util.ArrayList;
import java.util.List;

public final class TokenAttributes {

    public static List<TokenType> statementTypes = new ArrayList<>();
    public static List<TokenType> additiveOperators = new ArrayList<>();

    static {
        statementTypes.add(TokenType.VariableType);
        statementTypes.add(TokenType.Identifier);
        statementTypes.add(TokenType.If);
        statementTypes.add(TokenType.While);
        statementTypes.add(TokenType.Return);

        additiveOperators.add(TokenType.Plus);
        additiveOperators.add(TokenType.Minus);
    }
}
