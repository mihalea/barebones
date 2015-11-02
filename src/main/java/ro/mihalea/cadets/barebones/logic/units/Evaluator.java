package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidExpressionException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by Mircea on 02-Nov-15.
 */
public class Evaluator {
    /**
     * Regex used for determining whether a variable name is valid or not
     */
    private static String RX_NAME = "[a-zA-Z_][a-zA-Z0-9_]*";

    /**
     * Regex used in determining whether an argument is a valid number
     */
    private static String RX_NUMBER = "[0-9]+";

    /**
     * Regex used in determining wheter an argument is an operator or not
     */
    private static String RX_OPERATOR = "[" + Pattern.quote("+-*/()") + "]";

    private Memory memory;

    public Evaluator(Memory memory) {
        this.memory = memory;
    }

    public long evaluate(LinkedList<String> elements) throws NotAssignedException, InvalidCharacterException,
            InvalidExpressionException {
        Stack<Long> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        String token;
        while(!elements.isEmpty()) {
            token = elements.pop();
            if(Evaluator.isNumber(token))
                values.push(Long.parseLong(token));
            else if(Evaluator.isVariable(token))
                values.push(memory.get(token));
            else if(Evaluator.isOperator(token)) {
                char operator = token.charAt(0);

                switch (operator) {
                    case '(':
                        operators.push(operator);
                        break;
                    case ')':
                        while(!operators.isEmpty() && operators.peek() != '(')
                            values.push(operate(values.pop(), values.pop(), operators.pop()));
                        if(operators.peek() == '(')
                            operators.pop();
                        else
                            throw new InvalidExpressionException("Unmatched paranthesis");
                        break;
                    default:
                        while(!operators.isEmpty() && precedence(operators.peek()) >= precedence(operator)) {
                            values.push(operate(values.pop(), values.pop(), operators.pop()));
                        }
                        operators.push(operator);
                }


            }
        }

        while(!operators.isEmpty()) {
            values.push(operate(values.pop(), values.pop(), operators.pop()));
        }

        if(values.size() != 1)
            throw new InvalidExpressionException();

        return values.peek();
    }

    public long operate(long a, long b, char operator) throws InvalidCharacterException {
        switch (operator){
            case '+':
                return b+a;
            case '-':
                return b-a;
            case '*':
                return b*a;
            case '/':
                return b/a;
            default:
                throw new InvalidCharacterException("Operator not valid");
        }
    }

    public static boolean isVariable(String variable) {
        return Pattern.matches(RX_NAME, variable);
    }

    public static boolean isNumber(String numeric) {
        return Pattern.matches(RX_NUMBER, numeric);
    }

    public static boolean isOperator(String operator) {
        return Pattern.matches(RX_OPERATOR, operator);
    }

    private int precedence(char operator) {
        switch(operator) {
            case '*':
            case '/':
                return 3;
            case '+':
            case '-':
                return 2;
            case '(':
                return 1;
            default:
                return -1;
        }
    }
}
