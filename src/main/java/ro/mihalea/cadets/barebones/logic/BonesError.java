package ro.mihalea.cadets.barebones.logic;

/**
 * Aggregate of all interpreter errors as static fields.
 */
public abstract class BonesError {
    /**
     * Getter that returns the internal error code held by the object
     * @return Internal error code
     */
    public abstract int getCode();

    /**
     * Getter that returns an error message which is user-readable.
     * @return User-readable error message
     */
    public abstract String getMessage();

    /**
     * Formats the object data in a string containing all the information
     * @return Message containing the error data
     */
    @Override
    public String toString() {
        return "Error " + getCode() + ": " + getMessage();
    }

    /**
     * One or more statements have not been terminated by the semicolon
     */
    public static BonesError NOT_TERMINATED = new BonesError() {
        @Override
        public int getCode() {
            return 0;
        }

        @Override
        public String getMessage() {
            return "One or more instructions are not being terminated correctly";
        }
    };
}
