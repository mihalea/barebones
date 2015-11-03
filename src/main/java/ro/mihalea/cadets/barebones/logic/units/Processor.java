package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidExpressionException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.exceptions.TimeoutException;
import ro.mihalea.cadets.barebones.logic.instructions.BaseInstruction;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class Processor {
    /**
     * Threshold in milliseconds after which the Processor should abort execution
     */
    private final int TIMEOUT = 5000;

    /**
     * List of instructions waiting to be executed
     */
    private List<BaseInstruction> instructions;

    /**
     * Index of the current line
     */
    private int programCounter;

    /**
     * Holds all the variables
     */
    private Memory memory;

    /**
     * Creates a processor with memory assigned
     * @param memory Memory
     */
    public Processor(Memory memory) {
        this.memory = memory;
    }

    /**
     * Loads a new list of instructions
     * @param instructions Decoded instructions
     */
    public void load(List<BaseInstruction> instructions) {
        this.instructions = instructions;
        memory.clear();
        programCounter = 0;
    }

    /**
     * Runs the complete current instruction set
     * @return The final state of the Memory
     */
    public Memory run() throws NotAssignedException, InvalidCharacterException, InvalidExpressionException, TimeoutException {
        long startTime = System.nanoTime();
        while(programCounter != -1) {
            this.next();
            if(timeIsOut(startTime))
                throw new TimeoutException();
        }

        return memory;
    }

    /**
     * Checks whther the interpreter has exceeded the maximum amount of time it is allowed to run
     * @param startTime Start time in nanoseconds
     * @return True is the interpreter should timeout
     */
    private boolean timeIsOut(final long startTime) {
        //Converts nanoseconds (10^-9) to milliseconds (10^-3)
        return ((System.nanoTime() - startTime) / 1000000)  > TIMEOUT;
    }

    /**
     * Runs the next instruction in the instruction set
     * @return Returns the final state of the variables
     */
    public Memory next() throws NotAssignedException, InvalidCharacterException, InvalidExpressionException {
        if(programCounter < instructions.size())
            programCounter = instructions.get(programCounter).execute(programCounter, memory);

        if(programCounter >= instructions.size())
            programCounter = -1;

        return memory;
    }

    /**
     * Returns the line index of the instruction waiting to be executed
     * @return Line index of the next instruction
     */
    public int getLineIndex() {
        if(programCounter != -1)
            return instructions.get(programCounter).getLineIndex();
        else
            return -1;
    }

    public void dump() {
        System.out.println("\n=== INSTRUCTION DUMP===");
        for (BaseInstruction instruction : instructions)
            System.out.println(instruction);
        System.out.println(  "=======================");
    }
}
