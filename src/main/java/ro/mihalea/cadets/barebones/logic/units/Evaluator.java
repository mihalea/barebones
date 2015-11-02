package ro.mihalea.cadets.barebones.logic.units;

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
    private static String RX_OPERATOR = "[" + Pattern.quote("+-*/") + "]";

    private LinkedList<String> elements;
    private Stack<Long> values;
    private Memory memory;

    public Evaluator(Memory memory) {
        this.memory = memory;
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
                return 1;
            case '+':
            case '-':
                return 2;
            default:
                return -1;
        }
    }
}
