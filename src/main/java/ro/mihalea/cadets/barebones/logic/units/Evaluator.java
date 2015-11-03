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
 * Class used for evaluating expressions
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
    private static String RX_OPERATOR = "[\\Q+-*/()^\\E]";

    /**
     * Memory on which to apply actions
     */
    private Memory memory;

    /**
     * All evaluators must take a Memory object
     * @param memory Memory on which to work
     */
    public Evaluator(Memory memory) {
        this.memory = memory;
    }

    /**
     * Calculates the value of the expression if possible
     * @param elements Queue of values and operators in infix notation
     * @return Result of the expression
     * @throws NotAssignedException Value not assigned before using
     * @throws InvalidCharacterException Invalid character used as an operator
     * @throws InvalidExpressionException Invalid expression
     */
    public long evaluate(LinkedList<String> elements) throws NotAssignedException, InvalidCharacterException,
            InvalidExpressionException {
        //Values get stored here until they are used
        Stack<Long> values = new Stack<>();
        //Operators are stored here until they are used
        Stack<Character> operators = new Stack<>();
        //Copy the values so that we can use the same instruction multiple times
        LinkedList<String> input = new LinkedList<>(elements);

        //Current token
        String token;
        while(!input.isEmpty()) {
            token = input.remove();

            if(Evaluator.isNumber(token))
                //IfConditional number push it to value stack
                values.push(Long.parseLong(token));
            else if(Evaluator.isVariable(token))
                //IfConditional variable get value and push it to value stack
                values.push(memory.get(token));
            else if(Evaluator.isOperator(token)) {
                char operator = token.charAt(0);

                switch (operator) {
                    case '(':
                        //IfConditional open parenthesis push it to operator stack
                        operators.push(operator);
                        break;
                    case ')':
                        //IfConditional closed parenthesis remove all operators from the queue until the pair is
                        //encountered and then dispose both of them
                        while(!operators.isEmpty() && operators.peek() != '(')
                            values.push(operate(values.pop(), values.pop(), operators.pop()));
                        if(operators.peek() == '(')
                            operators.pop();
                        else
                            throw new InvalidExpressionException("Unmatched paranthesis");
                        break;
                    default:
                        //IfConditional not parenthesis, then it must be an operator, so use it on the value queue
                        while(!operators.isEmpty() && precedence(operators.peek()) >= precedence(operator)) {
                            values.push(operate(values.pop(), values.pop(), operators.pop()));
                        }
                        operators.push(operator);
                }


            }
        }

        //Operate on all remaining operators
        while(!operators.isEmpty()) {
            values.push(operate(values.pop(), values.pop(), operators.pop()));
        }

        //IfConditional there are more values remaining, then the expression is invalid
        if(values.size() != 1)
            throw new InvalidExpressionException();

        return values.peek();
    }

    /**
     *
     * @param a First term
     * @param b Second term
     * @param operator Operator to be used
     * @return Value of the operation
     * @throws InvalidCharacterException Invalid character used as an operator
     */
    private long operate(long a, long b, char operator) throws InvalidCharacterException {
        switch (operator){
            case '+':
                return b+a;
            case '-':
                return b-a;
            case '*':
                return b*a;
            case '/':
                return b/a;
            case '^':
                return (long) Math.pow(b, a);
            default:
                throw new InvalidCharacterException("Operator not valid");
        }
    }

    /**
     * Returns whether the string is a valid variable name
     * @param variable Variable name to check
     * @return True if the naming meets the specification
     */
    public static boolean isVariable(String variable) {
        return Pattern.matches(RX_NAME, variable);
    }

    /**
     * Returns whether the string is a number
     * @param numeric Number to check
     * @return True if the string is a valid number
     */
    public static boolean isNumber(String numeric) {
        return Pattern.matches(RX_NUMBER, numeric);
    }

    /**
     * Returns whether the string is a valid operator
     * @param operator Operator to check
     * @return True if the operator is valid
     */
    public static boolean isOperator(String operator) {
        return Pattern.matches(RX_OPERATOR, operator);
    }

    /**
     * Returns the precedence level of the operator
     * @param operator Operator to be used
     * @return Precedence level
     */
    private int precedence(char operator) {
        switch(operator) {
            case '^':
                return 4;
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
