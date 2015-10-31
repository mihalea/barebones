package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Exception thrown when one or more invalid characters
 * have been fed into the {@link ro.mihalea.cadets.barebones.logic.units.Fetcher}
 * Regex tested against: ^[a-zA-Z0-9\\W]
 */
public class InvalidCharacterException extends BonesException {
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public InvalidCharacterException(int line) {
        super("One or more invalid characters have been inputted", line);
    }
}
