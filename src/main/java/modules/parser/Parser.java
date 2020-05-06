package modules.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import modules.ast.Program;
import modules.ast.Signature;
import modules.ast.condition.Condition;
import modules.ast.expression.DoubleNode;
import modules.ast.expression.Expression;
import modules.ast.expression.ExpressionNode;
import modules.ast.expression.Variable;
import modules.ast.function.FunctionDef;
import modules.ast.statement.*;
import modules.data.Token;
import modules.data.TokenAttributes;
import modules.data.TokenType;
import modules.errorHandler.InvalidToken;
import modules.errorHandler.UnexpectedTokenException;
import modules.lexer.Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class Parser {
    @NonNull
    private Lexer lexer;
    private boolean isBufferedToken = false;

    public Program parseProgram() throws IOException, InvalidToken, UnexpectedTokenException {
        Program program = new Program();
        while (peekToken().getType().equals(TokenType.Function)) {
            program.addFunction(parseFunctionDef());
        }
        Token peekedToken = peekToken();
        if (peekedToken.getType().equals(TokenType.EndOfFile)) {
            throw new UnexpectedTokenException(
                    peekedToken.getLineNumber(),
                    peekedToken.getSignPosition(),
                    new ArrayList<>(Arrays.asList(TokenType.Function, TokenType.EndOfFile)),
                    peekedToken.getType()
            );
        }
        return program;
    }

    /*    Token currentToken() {
            return lexer.getToken();
        }

        Token moveTokenPointer() throws IOException, InvalidToken {
            if (isBufferedToken) {
                isBufferedToken = false;
                return lexer.getToken();
            } else {
                return lexer.readNextToken();
            }
        }

        Token getNextToken(TokenType expectedTokenType) throws IOException, InvalidToken, UnexpectedTokenException {
            if (!moveTokenPointer().getType().equals(expectedTokenType)) {
                throw new UnexpectedTokenException(
                        currentToken().getLineNumber(),
                        currentToken().getSignPosition(),
                        expectedTokenType,
                        currentToken().getType()
                );
            } else {
                return currentToken();
            }
        }*/
    Token getNextToken() throws IOException, InvalidToken {
        if (isBufferedToken) {
            isBufferedToken = false;
            return lexer.getToken();
        } else {
            return lexer.readNextToken();
        }
    }

    Token peekToken() throws IOException, InvalidToken {
        if (isBufferedToken) {
            return lexer.getToken();
        } else {
            isBufferedToken = true;
            return lexer.readNextToken();
        }
    }

    Token getCheckedNextTokenType(TokenType expectedTokenType) throws IOException, InvalidToken, UnexpectedTokenException {
        Token token = getNextToken();
        if (token.getType().equals(expectedTokenType)) {
            return token;
        } else {
            throw new UnexpectedTokenException(
                    token.getLineNumber(),
                    token.getSignPosition(),
                    expectedTokenType,
                    token.getType()
            );
        }
    }

    FunctionDef parseFunctionDef() throws UnexpectedTokenException, InvalidToken, IOException {
        FunctionDef functionDef = new FunctionDef();
        getCheckedNextTokenType(TokenType.Function);
        functionDef.setSignature(parseSignature());
        functionDef.setParameters(parseParameters());
        functionDef.setBlock(parseBlock());
        return functionDef;
    }


    Signature parseSignature() throws IOException, InvalidToken, UnexpectedTokenException {
        Signature signature = new Signature();
/*        Token peekedToken = peekToken();
        if (peekedToken.getType().equals(TokenType.VariableType)) {
            signature.setType(getNextToken().getValue());
            signature.setIdentifier(getCheckedNextTokenType(TokenType.Identifier).getValue());
        } else {
            throw new UnexpectedTokenException(
                    peekedToken.getLineNumber(),
                    peekedToken.getSignPosition(),
                    TokenType.VariableType,
                    peekedToken.getType()
            );
        }*/
        signature.setType(getCheckedNextTokenType(TokenType.VariableType).getValue());
        signature.setIdentifier(getCheckedNextTokenType(TokenType.Identifier).getValue());
        return signature;
    }

    List<Signature> parseParameters() throws UnexpectedTokenException, InvalidToken, IOException {
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
                            TokenType.VariableType,
                            peekToken().getType()
                    );
                }
            }
        }
        getCheckedNextTokenType(TokenType.ParenthClose);
        return parameters;
    }

    Block parseBlock() throws UnexpectedTokenException, InvalidToken, IOException {
        Block block = new Block();
        getCheckedNextTokenType(TokenType.BracketOpen);
        while (!peekToken().getType().equals(TokenType.BracketClose)) {
            block.addStatement(parseStatement());
        }
        getCheckedNextTokenType(TokenType.BracketClose);
        return block;
    }

    Statement parseStatement() throws IOException, InvalidToken, UnexpectedTokenException {
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
                        TokenAttributes.statementTypes,
                        peekToken().getType()
                );
        }
    }

    Statement parseInitStatement() throws UnexpectedTokenException, InvalidToken, IOException {
        InitStatement statement = new InitStatement();
        statement.setSignature(parseSignature());
        if (peekToken().getType().equals(TokenType.Assignment)) {
            statement.setAssignable(parseExpression());
        }
        getCheckedNextTokenType(TokenType.Semicolon);
        return statement;
    }

    Statement parseAssignmentOrFunctionCallStatement() throws UnexpectedTokenException, InvalidToken, IOException {
        String identifier = getCheckedNextTokenType(TokenType.Identifier).getValue();
        if (peekToken().getType().equals(TokenType.Assignment)) {
            return parseAssignmentStatement(identifier);
        } else if (peekToken().getType().equals(TokenType.ParenthOpen)) {
            FunctionCall functionCall = parseFunctionCallStatement(identifier);
            getCheckedNextTokenType(TokenType.Semicolon);
            return functionCall;
        } else {
            throw new UnexpectedTokenException(
                    peekToken().getLineNumber(),
                    peekToken().getSignPosition(),
                    TokenAttributes.statementTypes,
                    peekToken().getType()
            );
        }
    }

    Statement parseAssignmentStatement(String identifier) throws UnexpectedTokenException, InvalidToken, IOException {
        AssignmentStatement statement = new AssignmentStatement();
        statement.setIdentifier(identifier);
        statement.setAssignable(parseExpression());
        getCheckedNextTokenType(TokenType.Semicolon);
        return statement;
    }

    FunctionCall parseFunctionCallStatement(String identifier) throws UnexpectedTokenException, InvalidToken, IOException {
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
                            TokenType.VariableType, //TODO cos innego tutaj powinno byc - pomyslec
                            peekToken().getType()
                    );
                }
            }
        }
        getCheckedNextTokenType(TokenType.ParenthClose);
        return statement;
    }

    Statement parseIfStatement() throws UnexpectedTokenException, InvalidToken, IOException {
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

    Statement parseWhileStatement() throws UnexpectedTokenException, InvalidToken, IOException {
        WhileStatement statement = new WhileStatement();
        getCheckedNextTokenType(TokenType.While);
        getCheckedNextTokenType(TokenType.ParenthOpen);
        statement.setCondition(parseCondition());
        getCheckedNextTokenType(TokenType.ParenthClose);

        statement.setLoopBlock(parseSingleLineOrBlockStatement());
        return statement;
    }

    Statement parseSingleLineOrBlockStatement() throws IOException, InvalidToken, UnexpectedTokenException {
        if (peekToken().getType().equals(TokenType.BracketOpen)) {
            return parseBlock();
        } else {
            return parseStatement();
        }
    }

    Statement parseReturnStatement() throws UnexpectedTokenException, InvalidToken, IOException {
        ReturnStatement statement = new ReturnStatement();
        getCheckedNextTokenType(TokenType.Return);
        statement.setReturnedExpression(parseExpression());
        getCheckedNextTokenType(TokenType.Semicolon);
        return statement;
    }

    Condition parseCondition() throws IOException, InvalidToken, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseAndCondition());

        while (peekToken().getType().equals(TokenType.Or)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseAndCondition());
        }
        return condition;
    }

    Condition parseAndCondition() throws IOException, InvalidToken, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseEqualityCondition());

        while (peekToken().getType().equals(TokenType.And)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseEqualityCondition());
        }
        return condition;
    }

    Condition parseEqualityCondition() throws IOException, InvalidToken, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseRelationalCondition());

        while (peekToken().getType().equals(TokenType.Equality) || peekToken().getType().equals(TokenType.Inequality)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseRelationalCondition());
        }
        return condition;
    }

    Condition parseRelationalCondition() throws IOException, InvalidToken, UnexpectedTokenException {
        Condition condition = new Condition();
        condition.addOperand(parseBaseCondition());

        while (peekToken().getType().equals(TokenType.Greater) || peekToken().getType().equals(TokenType.GreaterOrEqual)
                || peekToken().getType().equals(TokenType.Less) || peekToken().getType().equals(TokenType.LessOrEqual)) {
            condition.setOperator(getNextToken().getType());
            condition.addOperand(parseBaseCondition());
        }
        return condition;
    }

    Condition parseBaseCondition() throws IOException, InvalidToken, UnexpectedTokenException {
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

    ExpressionNode parseExpression() throws IOException, InvalidToken, UnexpectedTokenException {
        ExpressionNode expression = new ExpressionNode();
        expression.addOperand(parseMultiplicativeExpression());

        while (peekToken().getType().equals(TokenType.Plus) || peekToken().getType().equals(TokenType.Minus)) {
            expression.addOperators(getNextToken().getType());
            expression.addOperand(parseMultiplicativeExpression());
        }
        return expression;
    }

    Expression parseMultiplicativeExpression() throws IOException, InvalidToken, UnexpectedTokenException {
        ExpressionNode expression = new ExpressionNode();
        expression.addOperand(parsePrimaryExpression());

        while (peekToken().getType().equals(TokenType.Multiply) || peekToken().getType().equals(TokenType.Divide)) {
            expression.addOperators(getNextToken().getType());
            expression.addOperand(parsePrimaryExpression());
        }
        return expression;
    }

    Expression parsePrimaryExpression() throws IOException, InvalidToken, UnexpectedTokenException {
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
                        new ArrayList<>(Arrays.asList(TokenType.ParenthOpen, TokenType.NumberLiteral, TokenType.Identifier)),
                        peekToken().getType()
                );
            }
        }
    }

    Expression parseLiteral() throws IOException, InvalidToken, UnexpectedTokenException { //TODO moze rozdzielic na IntegerNode i DoubleNode???
        int signValue = getSignValue();
        double numberValue = getCheckedNextTokenType(TokenType.NumberLiteral).getLiteralNumber();
        return new DoubleNode(numberValue * signValue);
    }

    int getSignValue() throws IOException, InvalidToken {
        if (peekToken().getType().equals(TokenType.Minus)) {
            getNextToken();
            return -1;
        } else {
            return 1;
        }
    }
}
