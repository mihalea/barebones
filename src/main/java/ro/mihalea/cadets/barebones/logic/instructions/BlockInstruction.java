package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

/**
 * Base introduction for all the block instructions that need a pairing instruction
 */
public abstract class BlockInstruction extends BaseInstruction {
    /**
     * Line index of the pair
     */
    protected int pairIndex = -1;

    public BlockInstruction(int lineIndex) {
        super(lineIndex);
    }

    /**
     * Sets the line index of the pair
     * @param pairIndex Line index of the pair
     */
    public void setPairIndex(int pairIndex) {
        this.pairIndex = pairIndex;
    }
}
