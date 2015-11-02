package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.instructions.BaseInstruction;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class Processor {
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
    public Memory run() throws NotAssignedException{
        while(programCounter != -1)
            this.next();

        return memory;
    }

    /**
     * Runs the next instruction in the instruction set
     * @return Returns the final state of the variables
     */
    public Memory next() throws NotAssignedException {
        if(programCounter < instructions.size())
            programCounter = instructions.get(programCounter).execute(programCounter, memory);

        if(programCounter >= instructions.size())
            programCounter = -1;

        return memory;
    }

    public int getProgramCounter() {
        return programCounter;
    }
}
