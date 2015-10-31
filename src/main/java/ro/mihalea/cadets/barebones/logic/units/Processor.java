package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.instructions.InstructionInterface;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class Processor {
    /**
     * List of instructions waiting to be executed
     */
    private List<InstructionInterface> instructions;

    /**
     * Index of the current line
     */
    private int programCounter;

    /**
     * Holds all the variables
     */
    private Memory memory;

    /**
     * Loads a new list of instructions
     * @param instructions Decoded instructions
     */
    public void load(List<InstructionInterface> instructions) {
        this.instructions = instructions;
        programCounter = 0;
    }

    /**
     * Runs the complete current instruction set
     * @return The final state of the Memory
     */
    public Memory run() {
        while(programCounter < instructions.size())
            this.next();

        return memory;
    }

    /**
     * Runs the next instruction in the instruction set
     * @return
     */
    public Memory next() {
        programCounter = instructions.get(programCounter).execute(programCounter, memory);
        return memory;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}
