package ro.mihalea.cadets.barebones.logic.exceptions;

import ro.mihalea.cadets.barebones.logic.units.Decoder;

/**
 * Exception thrown when trying to {@link Decoder#fetch()} without having all
 * the block statements closed
 */
public class BlockNotClosedException extends BonesException{
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public BlockNotClosedException(int line) {
        super("One or more block statements don't have an end", line);
    }
}
