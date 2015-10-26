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
     * Error 0x1:
     * This error gets thrown every time the user does not follow the
     * syntax for the "while" loop imposed by the specification.
     */
    public static BonesError SYNTAX_WHILE = new BonesError() {
        @Override
        public int getCode() {
            return 0x1;
        }

        @Override
        public String getMessage() {
            return "While does not have a proper syntax";
        }
    };

    /**
     * Error 0x2:
     * This error gets thrown every time the syntax does not follow
     * any known specifications and the interpreter has no methods
     * of understanding that line(s).
     */
    public static BonesError SYNTAX_UNKNOWN = new BonesError() {
        @Override
        public int getCode() {
            return 0x2;
        }

        @Override
        public String getMessage() {
            return "Syntax unknown";
        }
    };

    /**
     * Error 0x3:
     * This error gets thrown every time the user tries to use a variable
     * without declaring it beforehand. This error may become deprecated
     * in further versions.
     */
    public static BonesError NOT_CLEARED = new BonesError() {
        @Override
        public int getCode() {
            return 0x3;
        }

        @Override
        public String getMessage() {
            return "Variable not cleared beforehand";
        }
    };

    /**
     * Error 0x4:
     * This error gets thrown every time the user has written a
     * "while" loop that has not "end;" statement as declared in the specification.
     */
    public static BonesError NO_END = new BonesError() {
        @Override
        public int getCode() {
            return 0x4;
        }

        @Override
        public String getMessage() {
            return "One or more while's do not have an end";
        }
    };

    /**
     * Error 0x5:
     * This error gets thrown every time the interpreter has been
     * running for over 5000 milliseconds.
     */
    public static BonesError TIMEOUT = new BonesError() {
        @Override
        public int getCode() {
            return 0x5;
        }

        @Override
        public String getMessage() {
            return "You might have an infinite while. Or some super code. Whatever. I won't interpret anymore.";
        }
    };

    /**
     * Error 0x6:
     * This error gets thrown every time the user has used an "end;" statement
     * without having a matching "while".
     */
    public static BonesError NO_START = new BonesError() {
        @Override
        public int getCode() {
            return 0x6;
        }

        @Override
        public String getMessage() {
            return "You are trying to end a while that doesn't even exist";
        }
    };
}
