package tkom.data;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class KeyWords {
    public static Map<String, TokenType> keywords;
    public static Map<String, TokenType> singleSigns;
    public static Map<String, TokenType> doubleSigns;
    public static Map<String, TokenType> variableTypes;

    static {
        keywords = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        keywords.put("function", TokenType.Function);
        keywords.put("if", TokenType.If);
        keywords.put("else", TokenType.Else);
        keywords.put("while", TokenType.While);
        keywords.put("return", TokenType.Return);

        singleSigns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        singleSigns.put("(", TokenType.ParenthOpen);
        singleSigns.put(")", TokenType.ParenthClose);
        singleSigns.put("{", TokenType.BracketOpen);
        singleSigns.put("}", TokenType.BracketClose);
        singleSigns.put(",", TokenType.Comma);
        singleSigns.put(";", TokenType.Semicolon);
        singleSigns.put("!", TokenType.Negation);
        singleSigns.put("=", TokenType.Assignment);
        singleSigns.put("+", TokenType.Plus);
        singleSigns.put("-", TokenType.Minus);
        singleSigns.put("*", TokenType.Multiply);
        singleSigns.put("/", TokenType.Divide);
        singleSigns.put(">", TokenType.Greater);
        singleSigns.put("<", TokenType.Less);

        doubleSigns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        doubleSigns.put("==", TokenType.Equality);
        doubleSigns.put("<=", TokenType.LessOrEqual);
        doubleSigns.put(">=", TokenType.GreaterOrEqual);
        doubleSigns.put("!=", TokenType.Inequality);
        doubleSigns.put("&&", TokenType.And);
        doubleSigns.put("||", TokenType.Or);

        variableTypes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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
