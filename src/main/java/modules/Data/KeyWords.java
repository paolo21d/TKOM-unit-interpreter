package modules.Data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class KeyWords {
    public static Map<String, TokenType> keywords;
    public static Map<String, TokenType> definedSigns;
    public static Map<String, TokenType> variableTypes;

    static {
        keywords = new HashMap<>();
        keywords.put("function", TokenType.Function);
        keywords.put("if", TokenType.If);
        keywords.put("else", TokenType.Else);
        keywords.put("while", TokenType.While);
        keywords.put("return", TokenType.Return);

        definedSigns = new HashMap<>();
        definedSigns.put("(", TokenType.ParenthOpen);
        definedSigns.put(")", TokenType.ParenthClose);
        definedSigns.put("{", TokenType.BracketOpen);
        definedSigns.put("}", TokenType.BracketClose);
        definedSigns.put(",", TokenType.Comma);
        definedSigns.put(";", TokenType.Semicolon);
        definedSigns.put("!", TokenType.Negation);
        definedSigns.put("=", TokenType.Assignment);
        definedSigns.put("+", TokenType.Plus);
        definedSigns.put("-", TokenType.Minus);
        definedSigns.put("*", TokenType.Multiply);
        definedSigns.put("/", TokenType.Divide);

        variableTypes = new HashMap<>();
        variableTypes.put("kilo", TokenType.VariableType);
        variableTypes.put("mili", TokenType.VariableType);
        variableTypes.put("centy", TokenType.VariableType);
        variableTypes.put("mega", TokenType.VariableType);
        variableTypes.put("giga", TokenType.VariableType);
        variableTypes.put("integer", TokenType.VariableType);
        variableTypes.put("double", TokenType.VariableType);

    }

    public static void addVariableType(String typeName) {
        variableTypes.put(typeName.toLowerCase(), TokenType.VariableType);
    }
}
