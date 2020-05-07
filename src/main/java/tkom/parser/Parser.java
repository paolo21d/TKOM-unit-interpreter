package tkom.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tkom.ast.Program;
import tkom.ast.Signature;
import tkom.ast.condition.Condition;
import tkom.ast.expression.DoubleNode;
import tkom.ast.expression.Expression;
import tkom.ast.expression.ExpressionNode;
import tkom.ast.expression.Variable;
import tkom.ast.function.FunctionDef;
import tkom.ast.statement.*;
import tkom.data.Token;
import tkom.data.TokenAttributes;
import tkom.data.TokenType;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.UnexpectedTokenException;
import tkom.lexer.Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class Parser {
    @NonNull
    private Lexer lexer;
    private boolean isBufferedToken = false;

    public Program parseProgram() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Program program = new Program();
        while (peekToken().getType().equals(TokenType.Function)) {
            program.addFunction(parseFunctionDef());
        }
        Token peekedToken = peekToken();
        if (peekedToken.getType().equals(TokenType.EndOfFile)) {
            throw new UnexpectedTokenException(
                    peekedToken.getLineNumber(),
                    peekedToken.getSignPosition(),
                    peekedToken.getLineContent(),
                    new ArrayList<>(Arrays.asList(TokenType.Function, TokenType.EndOfFile)),
                    peekedToken.getType()
            );
        }
        return program;
    }

    Token getNextToken() throws IOException, InvalidTokenException {
        if (isBufferedToken) {
            isBufferedToken = false;
            return lexer.getToken();
        } else {
            return lexer.readNextToken();
        }
    }

    Token peekToken() throws IOException, InvalidTokenException {
        if (isBufferedToken) {
            return lexer.getToken();
        } else {
            isBufferedToken = true;
            return lexer.readNextToken();
        }
    }

    Token getCheckedNextTokenType(TokenType expectedTokenType) throws IOException, InvalidTokenException, UnexpectedTokenException {
        Token token = getNextToken();
        if (token.getType().equals(expectedTokenType)) {
            return token;
        } else {
            throw new UnexpectedTokenException(
                    token.getLineNumber(),
                    token.getSignPosition(),
                    token.getLineContent(),
                    expectedTokenType,
                    token.getType()
            );
        }
    }

    FunctionDef parseFunctionDef() throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionDef functionDef = new FunctionDef();
        getCheckedNextTokenType(TokenType.Function);
        functionDef.setSignature(parseSignature());
        functionDef.setParameters(parseParameters());
        functionDef.setBlock(parseBlock());
        return functionDef;
    }


    Signature parseSignature() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Signature signature = new Signature();
        signature.setType(getCheckedNextTokenType(TokenType.VariableType).getValue());
        signature.setIdentifier(getCheckedNextTokenType(TokenType.Identifier).getValue());
        return signature;
    }

    List<Signature> parseParameters() throws UnexpectedTokenException, InvalidTokenException, IOException {
        List<Signature> parameters = new ArrayList<>();
        getCheckedNextTokenType(TokenType.ParenthOpen);
        while (!peekToken().getType().equals(TokenType.ParenthClose)) {
            parameters.add(parseSignature());
            if (peekToken().getType().equals(TokenType.Comma)) {
                getNextToken(); //consume comma
                if (!peekToken().getType().equals(TokenType.VariableType)) { //after comma must be VariableType
                    throw new UnexpectedTokenException(
                            peekToken().getLineNumber(),
                            peekToken().getSignPosition(),
                            peekToken().getLineContent(),
                            TokenType.VariableType,
                            peekToken().getType()
                    );
                }
            }
        }
        getCheckedNextTokenType(TokenType.ParenthClose);
        return parameters;
    }

    Block parseBlock() throws UnexpectedTokenException, InvalidTokenException, IOException {
        Block block = new Block();
        getCheckedNextTokenType(TokenType.BracketOpen);
        while (!peekToken().getType().equals(TokenType.BracketClose)) {
            block.addStatement(parseStatement());
        }
        getCheckedNextTokenType(TokenType.BracketClose);
        return block;
    }

    Statement parseStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        switch (peekToken().getType()) {
            case VariableType:
                return parseInitStatement();
            case Identifier:
                return parseAssignmentOrFunctionCallStatement();
            case If:
                return parseIfStatement();
            case While:
                return parseWhileStatement();
            case Return:
                return parseReturnStatement();
            default:
                throw new UnexpectedTokenException(
                        peekToken().getLineNumber(),
                        peekToken().getSignPosition(),
                        peekToken().getLineContent(),
                        TokenAttributes.statementTypes,
                        peekToken().getType()
                );
        }
    }

    Statement parseInitStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        InitStatement statement = new InitStatement();
        statement.setSignature(parseSignature());
        if (peekToken().getType().equals(TokenType.Assignment)) {
            getCheckedNextTokenType(TokenType.Assignment);
            statement.setAssignable(parseExpression());
        }
        getCheckedNextTokenType(TokenType.Semicolon);
        return statement;
    }

    Statement parseAssignmentOrFunctionCallStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        String identifier = getCheckedNextTokenType(TokenType.Identifier).getValue();
        if (peekToken().getType().equals(TokenType.Assignment)) {
            getCheckedNextTokenType(TokenType.Assignment);
            return parseAssignmentStatement(identifier);
        } else if (peekToken().getType().equals(TokenType.ParenthOpen)) {
            FunctionCall functionCall = parseFunctionCallStatement(identifier);
            getCheckedNextTokenType(TokenType.Semicolon);
            return functionCall;
        } else {
            throw new UnexpectedTokenException(
                    peekToken().getLineNumber(),
                    peekToken().getSignPosition(),
                    peekToken().getLineContent(),
                    TokenAttributes.statementTypes,
                    peekToken().getType()
            );
        }
    }

    Statement parseAssignmentStatement(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        AssignmentStatement statement = new AssignmentStatement();
        statement.setIdentifier(identifier);
        statement.setAssignable(parseExpression());
        getCheckedNextTokenType(TokenType.Semicolon);
        return statement;
    }

    FunctionCall parseFunctionCallStatement(String identifier) throws UnexpectedTokenException, InvalidTokenException, IOException {
        FunctionCall statement = new FunctionCall(identifier);
        getCheckedNextTokenType(TokenType.ParenthOpen);
        while (!peekToken().getType().equals(TokenType.ParenthClose)) {
            statement.addArgument(parseExpression());
            if (peekToken().getType().equals(TokenType.Comma)) { //TODO pomyslec nad tym bo jest raczej zle
                getNextToken(); //consume comma
                if (peekToken().getType().equals(TokenType.ParenthClose)) { //after comma can't be ParenthClose
                    throw new UnexpectedTokenException(
                            peekToken().getLineNumber(),
                            peekToken().getSignPosition(),
                            peekToken().getLineContent(),
                            TokenType.VariableType, //TODO cos innego tutaj powinno byc - pomyslec
                            peekToken().getType()
                    );
                }
            }
        }
        getCheckedNextTokenType(TokenType.ParenthClose);
        return statement;
    }

    Statement parseIfStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        IfStatement statement = new IfStatement();
        getCheckedNextTokenType(TokenType.If);
        getCheckedNextTokenType(TokenType.ParenthOpen);
        statement.setCondition(parseCondition());
        getCheckedNextTokenType(TokenType.ParenthClose);

        statement.setTrueStatement(parseSingleLineOrBlockStatement());
        if (peekToken().getType().equals(TokenType.Else)) {
            getCheckedNextTokenType(TokenType.Else);
            statement.setFalseStatement(parseSingleLineOrBlockStatement());
        }
        return statement;
    }

    Statement parseWhileStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        WhileStatement statement = new WhileStatement();
        getCheckedNextTokenType(TokenType.While);
        getCheckedNextTokenType(TokenType.ParenthOpen);
        statement.setCondition(parseCondition());
        getCheckedNextTokenType(TokenType.ParenthClose);

        statement.setLoopBlock(parseSingleLineOrBlockStatement());
        return statement;
    }

    Statement parseSingleLineOrBlockStatement() throws IOException, InvalidTokenException, UnexpectedTokenException {
        if (peekToken().getType().equals(TokenType.BracketOpen)) {
            return parseBlock();
        } else {
            return parseStatement();
        }
    }

    Statement parseReturnStatement() throws UnexpectedTokenException, InvalidTokenException, IOException {
        ReturnStatement statement = new ReturnStatement();
        getCheckedNextTokenType(TokenType.Return);
        statement.setReturnedExpression(parseExpression());
        getCheckedNextTokenType(TokenType.Semicolon);
        return statement;
    }

    Condition parseCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseAndCondition());

        while (peekToken().getType().equals(TokenType.Or)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseAndCondition());
        }
        return condition;
    }

    Condition parseAndCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseEqualityCondition());

        while (peekToken().getType().equals(TokenType.And)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseEqualityCondition());
        }
        return condition;
    }

    Condition parseEqualityCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseRelationalCondition());

        while (peekToken().getType().equals(TokenType.Equality) || peekToken().getType().equals(TokenType.Inequality)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseRelationalCondition());
        }
        return condition;
    }

    Condition parseRelationalCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseBaseCondition());

        while (peekToken().getType().equals(TokenType.Greater) || peekToken().getType().equals(TokenType.GreaterOrEqual)
                || peekToken().getType().equals(TokenType.Less) || peekToken().getType().equals(TokenType.LessOrEqual)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseBaseCondition());
        }
        return condition;
    }

    Condition parseBaseCondition() throws IOException, InvalidTokenException, UnexpectedTokenException {
        Condition condition = new Condition();
        if (peekToken().getType().equals(TokenType.Negation)) {
            getNextToken();
            condition.setNegated(true);
        }

        if (peekToken().getType().equals(TokenType.ParenthOpen)) {
            getNextToken();
            condition.addOperand(parseCondition());
            getCheckedNextTokenType(TokenType.ParenthClose);
        } else {
            condition.addOperand(parseExpression());
        }
        return condition;
    }

    ExpressionNode parseExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        ExpressionNode expression = new ExpressionNode();
        expression.addOperand(parseMultiplicativeExpression());

        while (peekToken().getType().equals(TokenType.Plus) || peekToken().getType().equals(TokenType.Minus)) {
            expression.addOperators(getNextToken().getType());
            expression.addOperand(parseMultiplicativeExpression());
        }
        return expression;
    }

    Expression parseMultiplicativeExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        ExpressionNode expression = new ExpressionNode();
        expression.addOperand(parsePrimaryExpression());

        while (peekToken().getType().equals(TokenType.Multiply) || peekToken().getType().equals(TokenType.Divide)) {
            expression.addOperators(getNextToken().getType());
            expression.addOperand(parsePrimaryExpression());
        }
        return expression;
    }

    Expression parsePrimaryExpression() throws IOException, InvalidTokenException, UnexpectedTokenException {
        switch (peekToken().getType()) {
            case Minus:
            case NumberLiteral:
                return parseLiteral();
            case ParenthOpen: {
                getCheckedNextTokenType(TokenType.ParenthOpen);
                Expression expression = parseExpression();
                getCheckedNextTokenType(TokenType.ParenthClose);
                return expression;
            }
            case Identifier: {
                String identifier = getCheckedNextTokenType(TokenType.Identifier).getValue();
                if (peekToken().getType().equals(TokenType.ParenthOpen)) {
                    return parseFunctionCallStatement(identifier);
                } else {
                    return new Variable(identifier);
                }

            }
            default: {
                throw new UnexpectedTokenException(
                        peekToken().getLineNumber(),
                        peekToken().getSignPosition(),
                        peekToken().getLineContent(),
                        new ArrayList<>(Arrays.asList(TokenType.ParenthOpen, TokenType.NumberLiteral, TokenType.Identifier)),
                        peekToken().getType()
                );
            }
        }
    }

    Expression parseLiteral() throws IOException, InvalidTokenException, UnexpectedTokenException { //TODO moze rozdzielic na IntegerNode i DoubleNode???
        int signValue = getSignValue();
        double numberValue = getCheckedNextTokenType(TokenType.NumberLiteral).getLiteralNumber();
        return new DoubleNode(numberValue * signValue);
    }

    int getSignValue() throws IOException, InvalidTokenException {
        if (peekToken().getType().equals(TokenType.Minus)) {
            getNextToken();
            return -1;
        } else {
            return 1;
        }
    }
}
