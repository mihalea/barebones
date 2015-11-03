package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidExpressionException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;

import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Mircea on 02-Nov-15.
 */
public class EvaluatorTest extends TestCase {
    public void testAddition() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 2);
        memory.set("b", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("+");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(5, result);
    }

    public void testSubtraction() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 5);
        memory.set("b", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("-");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(2, result);
    }

    public void testMultiplication() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 12);
        memory.set("b", 11);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("*");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(132, result);
    }

    public void testDivision() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 30);
        memory.set("b", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("/");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(10, result);
    }

    public void testPrecedence() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 30);
        memory.set("b", 3);
        memory.set("c", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("+");
        expression.add("b");
        expression.add("*");
        expression.add("c");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(39, result);
    }

    public void testNumerical() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 30);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("+");
        expression.add("100");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(130, result);
    }

    public void testParanthesis() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 5);
        memory.set("b", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("(");
        expression.add("a");
        expression.add("+");
        expression.add("3");
        expression.add(")");
        expression.add("*");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(24, result);
    }

    public void testGreaterLesser() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 5);
        memory.set("b", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("<");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);

        assertEquals(0, result);


        expression.clear();
        expression.add("a");
        expression.add(">");
        expression.add("b");

        assertEquals(1, evaluator.evaluate(expression));

        expression.clear();
        expression.add("a");
        expression.add(">=");
        expression.add("5");

        assertEquals(1, evaluator.evaluate(expression));

        expression.clear();
        expression.add("a");
        expression.add(">=");
        expression.add("10");

        assertEquals(0, evaluator.evaluate(expression));

        expression.clear();
        expression.add("b");
        expression.add("<=");
        expression.add("3");

        assertEquals(1, evaluator.evaluate(expression));

        expression.clear();
        expression.add("b");
        expression.add("<=");
        expression.add("2");

        assertEquals(0, evaluator.evaluate(expression));

        expression.clear();
        expression.add("a");
        expression.add(">=");
        expression.add("5");

        assertEquals(1, evaluator.evaluate(expression));

        expression.clear();
        expression.add("a");
        expression.add(">=");
        expression.add("22");

        assertEquals(0, evaluator.evaluate(expression));
    }

    public void testEquality() throws InvalidCharacterException, NotAssignedException, InvalidExpressionException {
        Memory memory = new Memory();
        memory.set("a", 5);
        memory.set("b", 3);

        LinkedList<String> expression = new LinkedList<>();
        expression.add("a");
        expression.add("==");
        expression.add("b");

        Evaluator evaluator = new Evaluator(memory);
        assertEquals(0, evaluator.evaluate(expression));

        expression.clear();
        expression.add("a");
        expression.add("==");
        expression.add("5");

        assertEquals(1, evaluator.evaluate(expression));

        expression.clear();
        expression.add("a");
        expression.add("!=");
        expression.add("5");

        assertEquals(0, evaluator.evaluate(expression));

        expression.clear();
        expression.add("b");
        expression.add("!=");
        expression.add("3");

        assertEquals(0, evaluator.evaluate(expression));
    }
}