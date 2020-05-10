package tkom.parser;

import org.junit.Test;
import tkom.ast.Program;
import tkom.ast.Signature;
import tkom.ast.condition.Condition;
import tkom.ast.expression.*;
import tkom.ast.function.FunctionDef;
import tkom.ast.statement.*;
import tkom.data.KeyWords;
import tkom.data.Token;
import tkom.data.TokenType;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.UnexpectedTokenException;
import tkom.lexer.Lexer;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.junit.Assert.*;

public class ParserTest {
    private static final List<String> embeddedVariableTypes = Collections.unmodifiableList(
            new ArrayList<String>() {{
                for (Map.Entry<String, TokenType> type : KeyWords.variableTypes.entrySet()) {
                    add(type.getKey());
                }
            }}
    );
    private Parser parser;

    private Parser initializeParser(String input) {
        Lexer lexer = new Lexer(new StringReader(input));
        parser = new Parser(lexer);
        return parser;
    }

    private Parser initializeParser(String input, List<String> units) {
        for (String unit : units) {
            KeyWords.addVariableType(unit);
        }
        return initializeParser(input);
    }

    private void assertToken(Token token, TokenType tokenType, String value) {
        assertEquals(token.getType(), tokenType);
        assertEquals(token.getValue(), value);
    }

    private void assertToken(Token token, TokenType tokenType, double value) { //TODO maybe remove
        assertEquals(token.getType(), tokenType);
        assertEquals(token.getLiteralNumber(), value);
    }

    private void assertFunctionDef(FunctionDef functionDef, String type, String identifier, List<Signature> parameters) {
        assertEquals(functionDef.getType(), type);
        assertEquals(functionDef.getIdentifier(), identifier);
        assertEquals(functionDef.getParameters(), parameters);
    }

    private void assertExceptionParameters(String input, String errorMessage) {
        try {
            initializeParser(input);
            parser.parseIfStatement();
            fail(errorMessage);
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }
    }

    private void assertExceptionIfStatement(String input, String errorMessage) {
        try {
            initializeParser(input);
            parser.parseParameters();
            fail(errorMessage);
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }
    }

    private void assertExceptionInitStatement(String input, String errorMessage) {
        try {
            initializeParser(input);
            parser.parseInitStatement();
            fail(errorMessage);
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }
    }

    private void assertExceptionReturnStatement(String input, String errorMessage) {
        try {
            initializeParser(input);
            parser.parseReturnStatement();
            fail(errorMessage);
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }
    }

    private Condition castCondition(Expression expression) {
        return (Condition) expression;
    }

    //////////////////////////////////////////////////////////////
    @Test
    public void checkGetNextToken() throws IOException, InvalidTokenException {
        initializeParser("a b c d");
        assertToken(parser.getNextToken(), TokenType.Identifier, "a");
        assertToken(parser.getNextToken(), TokenType.Identifier, "b");
        assertToken(parser.getNextToken(), TokenType.Identifier, "c");
        assertToken(parser.getNextToken(), TokenType.Identifier, "d");
    }

    @Test
    public void checkPeekTokenTheSameToken() throws IOException, InvalidTokenException {
        initializeParser("a b c d");
        assertToken(parser.peekToken(), TokenType.Identifier, "a");
        assertToken(parser.peekToken(), TokenType.Identifier, "a");
        assertToken(parser.peekToken(), TokenType.Identifier, "a");
    }

    @Test
    public void checkGetNextTokenAfterPeekToken() throws IOException, InvalidTokenException {
        initializeParser("a b c d");
        assertToken(parser.peekToken(), TokenType.Identifier, "a");
        assertToken(parser.getNextToken(), TokenType.Identifier, "a");
        assertToken(parser.peekToken(), TokenType.Identifier, "b");
        assertToken(parser.getNextToken(), TokenType.Identifier, "b");
        Token peekedToken = parser.peekToken();
        assertToken(parser.getNextToken(), peekedToken.getType(), peekedToken.getValue());
    }

    @Test(expected = UnexpectedTokenException.class)
    public void checkGetCheckedNextTokenTypeUnexpectedTokenException() throws UnexpectedTokenException, InvalidTokenException, IOException { //TODO byc moze zrobic taak jak LexerRecognitionTokenType
        initializeParser("a b c d");
        parser.getCheckedNextTokenType(TokenType.ParenthClose);
    }

    @Test
    public void checkParseProgram() throws UnexpectedTokenException, InvalidTokenException, IOException {
        String input = "function KILO max(KILO unit1, KILO unit2) {\n" +
                "\tif(unit1 > unit2) {\n" +
                "\t\treturn unit1;\n" +
                "    } else {\n" +
                "\t    return unit2;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "function INTEGER main() {\n" +
                "\tKILO smallUnit = 10;\n" +
                "\tMEGA bigUnit = 1;\n" +
                "\tKILO result = max(smallUnit, bigUnit);\n" +
                "\treturn 0;\n" +
                "}";

        initializeParser(input);
        Program program = parser.parseProgram();
        assertEquals(program.getFunctions().size(), 2);
        assertFunctionDef(program.getFunctions().get(0), "KILO", "max", Arrays.asList(new Signature("KILO", "unit1"), new Signature("KILO", "unit2")));
        assertFunctionDef(program.getFunctions().get(1), "INTEGER", "main", new ArrayList<>());
    }

    @Test
    public void checkParseFunctionDef() throws IOException, InvalidTokenException, UnexpectedTokenException {
        String input1 = "function GIGA func1(){return 0;}" +
                "function GIGA func2(INTEGER a){return a;}" +
                "function GIGA func3(INTEGER a, DOUBLE b){return a+b;}";
        initializeParser(input1);
        assertFunctionDef(parser.parseFunctionDef(), "GIGA", "func1", new ArrayList<Signature>());
        assertFunctionDef(parser.parseFunctionDef(), "GIGA", "func2", Arrays.asList(new Signature("INTEGER", "a")));
        assertFunctionDef(parser.parseFunctionDef(), "GIGA", "func3", Arrays.asList(new Signature("INTEGER", "a"), new Signature("DOUBLE", "b")));
    }

    @Test
    public void checkParseSignature() throws UnexpectedTokenException, InvalidTokenException, IOException {
        List<String> types = new ArrayList<>(embeddedVariableTypes);
        List<String> newTypes = new ArrayList<>(Arrays.asList("PETA", "MIKRO"));
        types.addAll(newTypes);

        StringBuilder inputBuilder = new StringBuilder();
        for (String type : types) {
            inputBuilder.append(type).append(" variable\n");
        }
        initializeParser(inputBuilder.toString(), newTypes);
        for (String type : types) {
            assertEquals(parser.parseSignature(), new Signature(type, "variable"));
        }
    }

    @Test
    public void checkParseParameters() throws IOException, InvalidTokenException, UnexpectedTokenException {
        String input = "() (integer a) (integer a, integer b) (integer a, integer b, integer c)";
        initializeParser(input);
        assertEquals(parser.parseParameters(),
                new ArrayList<Signature>());
        assertEquals(parser.parseParameters(),
                Arrays.asList(new Signature("integer", "a")));
        assertEquals(parser.parseParameters(),
                Arrays.asList(new Signature("integer", "a"), new Signature("integer", "b")));
        assertEquals(parser.parseParameters(),
                Arrays.asList(new Signature("integer", "a"), new Signature("integer", "b"), new Signature("integer", "c")));
    }

    @Test()
    public void checkParseParametersException() {
        assertExceptionParameters("aaa", "Passed parameters without open parent at begin");
        assertExceptionParameters("(integer )", "Passed parameters without variable name");
        assertExceptionParameters("(integer a, )", "Passed parameters with missing variable declaration");
    }

    @Test
    public void checkParseBlock() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("{}");
        assertEquals(parser.parseBlock().getStatements().size(), 0);

        initializeParser("{integer a;return a;}");
        assertEquals(parser.parseBlock().getStatements().size(), 2);
    }

    @Test
    public void checkParseBlockException() {
        try {
            initializeParser("a}");
            parser.parseParameters();
            fail("Missing open bracket at begin");
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }

        try {
            initializeParser("{");
            parser.parseParameters();
            fail("Missing close bracket at end");
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }
    }

    @Test
    public void checkParseStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("integer a;");
        assertTrue(parser.parseStatement() instanceof InitStatement);

        initializeParser("a = 10;");
        assertTrue(parser.parseStatement() instanceof AssignmentStatement);

        initializeParser("functionCall();");
        assertTrue(parser.parseStatement() instanceof FunctionCall);

        initializeParser("if(1==2) return 0;");
        assertTrue(parser.parseStatement() instanceof IfStatement);

        initializeParser("while(1) return 0;");
        assertTrue(parser.parseStatement() instanceof WhileStatement);

        initializeParser("return 1;");
        assertTrue(parser.parseStatement() instanceof ReturnStatement);
    }

    @Test
    public void checkParseInitStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("integer a = 1;");
        InitStatement statement = (InitStatement) parser.parseInitStatement();
        assertEquals("integer", statement.getType());
        assertEquals("a", statement.getIdentifier());
        assertNotNull(statement.getAssignable());

        assertExceptionInitStatement("integer a", "Missing assignment character");
        assertExceptionInitStatement("integer a = 1", "Missing semicolon");
        assertExceptionInitStatement("integer a = ;", "Missing expression");
    }

    @Test
    public void checkParseAssignmentOrFunctionCallStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("a = 10;");
        assertTrue(parser.parseAssignmentOrFunctionCallStatement() instanceof AssignmentStatement);

        initializeParser("functionCall();");
        assertTrue(parser.parseAssignmentOrFunctionCallStatement() instanceof FunctionCall);
    }

    @Test
    public void checkParseFunctionCall() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("printMethod(\"text to print\");");
        FunctionCall functionCallStatement = (FunctionCall) parser.parseAssignmentOrFunctionCallStatement();
        assertEquals("printMethod", functionCallStatement.getIdentifier());
        assertTrue(((ExpressionNode) functionCallStatement.getArguments().get(0)).getOperands().get(0) instanceof StringNode);
        assertEquals("text to print", ((StringNode) ((ExpressionNode) functionCallStatement.getArguments().get(0)).getOperands().get(0)).getValue());
    }

    @Test
    public void checkParseIfStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("if(1==2) return 0;");
        assertNull(((IfStatement) parser.parseIfStatement()).getFalseStatement());

        assertExceptionIfStatement("if aaa", "Missing open parenth");
        assertExceptionIfStatement("if (1==2", "Missing close parenth");
        assertExceptionIfStatement("if ()", "Missing condition");
        assertExceptionIfStatement("if (1==2)", "Missing true statement");
    }

    @Test
    public void checkParseIfElse() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("if(a==2) return 1; else return 2;");
        IfStatement statement = (IfStatement) parser.parseIfStatement();
        assertNotNull(statement.getCondition());
        assertNotNull(statement.getTrueStatement());
        assertNotNull(statement.getFalseStatement());

        initializeParser("if(a==2) {return 2;} else if(a==3) return 3;");
        statement = (IfStatement) parser.parseIfStatement();
        assertTrue(statement.getFalseStatement() instanceof IfStatement);
    }

    @Test
    public void checkParseWhileStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("while(a<2) {}");
        WhileStatement statement = (WhileStatement) parser.parseWhileStatement();
        assertNotNull(statement.getCondition());
        assertEquals(0, ((Block) statement.getLoopBlock()).getStatements().size());
    }

    @Test
    public void checkParseReturnStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser("return 1;");
        assertNotNull(((ReturnStatement) parser.parseReturnStatement()).getReturnedExpression());

        assertExceptionReturnStatement("return a", "Missing semicolon");
    }


    @Test
    public void checkParseCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("a || b || c");
        Condition condition = parser.parseCondition();
        assertEquals(TokenType.Or, condition.getOperator());
        assertEquals(3, condition.getOperands().size());

        initializeParser("a || b && c");
        condition = parser.parseCondition();
        assertEquals(2, condition.getOperands().size());
        assertEquals(TokenType.And, ((Condition) condition.getOperands().get(1)).getOperator());
        assertEquals(2, ((Condition) condition.getOperands().get(1)).getOperands().size());

        initializeParser("a && (b || c==4 && d>0)");
        condition = parser.parseCondition();
        assertNull(condition.getOperator());
        assertEquals(1, condition.getOperands().size());
        assertFalse(condition.getNegated());
        assertEquals(TokenType.And, castCondition(condition.getOperands().get(0)).getOperator());
        assertEquals(2, castCondition(condition.getOperands().get(0)).getOperands().size());
    }

    @Test
    public void checkParseAndCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("a && b && c");
        Condition condition = parser.parseAndCondition();
        assertEquals(TokenType.And, condition.getOperator());
        assertEquals(3, condition.getOperands().size());
    }

    @Test
    public void checkParseEqualityCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("a == 4");
        Condition condition = parser.parseEqualityCondition();
        assertEquals(TokenType.Equality, condition.getOperator());

        initializeParser("a != b");
        condition = parser.parseEqualityCondition();
        assertEquals(TokenType.Inequality, condition.getOperator());
    }

    @Test
    public void checkParseRelationalCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("a > 4");
        Condition condition = parser.parseRelationalCondition();
        assertEquals(TokenType.Greater, condition.getOperator());

        initializeParser("a >= b");
        condition = parser.parseRelationalCondition();
        assertEquals(TokenType.GreaterOrEqual, condition.getOperator());

        initializeParser("a < b");
        condition = parser.parseRelationalCondition();
        assertEquals(TokenType.Less, condition.getOperator());

        initializeParser("a <= b");
        condition = parser.parseRelationalCondition();
        assertEquals(TokenType.LessOrEqual, condition.getOperator());
    }

    @Test
    public void checkParseBaseCondition() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("!a");
        Condition condition = parser.parseBaseCondition();
        assertTrue(condition.getNegated());

        initializeParser("a");
        condition = parser.parseBaseCondition();
        assertFalse(condition.getNegated());

        initializeParser("a + b");
        condition = parser.parseBaseCondition();
        assertNull(condition.getOperator());
        assertEquals(TokenType.Plus, ((ExpressionNode) condition.getOperands().get(0)).getOperators().get(0));

        initializeParser("(a || b)");
        condition = parser.parseBaseCondition();
        assertNull(condition.getOperator());
        assertEquals(TokenType.Or, ((Condition) condition.getOperands().get(0)).getOperator());

        try {
            initializeParser("(a==0");
            parser.parseReturnStatement();
            fail("Missing parenth close");
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }
    }

    @Test
    public void checkParseExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("a+b-c+d");
        ExpressionNode expression = parser.parseExpression();
        assertEquals(Arrays.asList(TokenType.Plus, TokenType.Minus, TokenType.Plus), expression.getOperators());
        assertEquals(4, expression.getOperands().size());

        initializeParser("a+b*c");
        expression = parser.parseExpression();
        assertEquals(2, expression.getOperands().size());
        assertEquals(2, ((ExpressionNode) expression.getOperands().get(1)).getOperands().size());
        assertEquals(TokenType.Multiply, ((ExpressionNode) expression.getOperands().get(1)).getOperators().get(0));

        initializeParser("a*(b+c/d)");
        expression = parser.parseExpression();
        assertEquals(0, expression.getOperators().size());
        assertEquals(1, expression.getOperands().size());
        assertEquals(Arrays.asList(TokenType.Multiply), ((ExpressionNode) expression.getOperands().get(0)).getOperators());
        assertEquals(new Variable("a"), ((ExpressionNode) expression.getOperands().get(0)).getOperands().get(0));
        assertEquals(Arrays.asList(TokenType.Plus), ((ExpressionNode) ((ExpressionNode) expression.getOperands().get(0)).getOperands().get(1)).getOperators());
        assertEquals(new Variable("b"), ((ExpressionNode) ((ExpressionNode) ((ExpressionNode) expression.getOperands().get(0)).getOperands().get(1)).getOperands().get(0)).getOperands().get(0));
        assertEquals(Arrays.asList(TokenType.Divide), ((ExpressionNode) ((ExpressionNode) ((ExpressionNode) expression.getOperands().get(0)).getOperands().get(1)).getOperands().get(1)).getOperators());
        assertEquals(new Variable("c"), ((ExpressionNode) ((ExpressionNode) ((ExpressionNode) expression.getOperands().get(0)).getOperands().get(1)).getOperands().get(1)).getOperands().get(0));
        assertEquals(new Variable("d"), ((ExpressionNode) ((ExpressionNode) ((ExpressionNode) expression.getOperands().get(0)).getOperands().get(1)).getOperands().get(1)).getOperands().get(1));
    }

    @Test
    public void checkParseMultiplicativeExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("a*b/c*d");
        ExpressionNode expression = (ExpressionNode) parser.parseMultiplicativeExpression();
        assertEquals(Arrays.asList(TokenType.Multiply, TokenType.Divide, TokenType.Multiply), expression.getOperators());
        assertEquals(4, expression.getOperands().size());
    }

    @Test
    public void checkParsePrimaryExpression() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("1.1");
        Expression expression = parser.parsePrimaryExpression();
        assertTrue(expression instanceof NumberNode);

        initializeParser("-1.1");
        expression = parser.parsePrimaryExpression();
        assertTrue(expression instanceof NumberNode);

        //check ParenthOpen
        initializeParser("(a)");
        ExpressionNode expressionNode = (ExpressionNode) parser.parsePrimaryExpression();
        assertEquals(0, expressionNode.getOperators().size());
        assertEquals(1, expressionNode.getOperands().size());
        assertTrue(((ExpressionNode) expressionNode.getOperands().get(0)).getOperands().get(0) instanceof Variable);

        try {
            initializeParser("(a");
            parser.parseIfStatement();
            fail("Missing close parenth");
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedTokenException);
        }

        //check identifier
        initializeParser("identifier");
        Expression identifierExpression = parser.parsePrimaryExpression();
        assertTrue(identifierExpression instanceof Variable);
        assertEquals("identifier", ((Variable) identifierExpression).getIdentifier());

        initializeParser("functionIdentifier()");
        identifierExpression = parser.parsePrimaryExpression();
        assertTrue(identifierExpression instanceof FunctionCall);
        assertEquals("functionIdentifier", ((FunctionCall) identifierExpression).getIdentifier());
    }

    @Test
    public void checkParseLiteral() throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser("1.1");
        NumberNode expression = (NumberNode) parser.parseLiteral();
        assertEquals(Double.valueOf(1.1), Double.valueOf(expression.getValue()));

        initializeParser("-1.1");
        expression = (NumberNode) parser.parseLiteral();
        assertEquals(Double.valueOf(-1.1), Double.valueOf(expression.getValue()));
    }

    @Test
    public void checkGetSignValue() throws IOException, InvalidTokenException {
        initializeParser("-");
        assertEquals(-1, parser.getSignValue());

        initializeParser("a");
        assertEquals(1, parser.getSignValue());
    }

}
