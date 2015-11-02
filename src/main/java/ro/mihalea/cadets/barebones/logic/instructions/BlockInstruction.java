package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

/**
 * Created by Mircea on 02-Nov-15.
 */
public abstract class BlockInstruction extends BaseInstruction {
    protected int pairIndex = -1;

    public void setPairIndex(int pairIndex) {
        this.pairIndex = pairIndex;
    }
}
