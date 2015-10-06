package barebones.logic;

/**
 * Created by mm8g15 on 10/5/2015.
 */
public abstract class BonesError {
    public abstract int getCode();
    public abstract String getMessage();

    @Override
    public String toString() {
        return "Error " + getCode() + ": " + getMessage();
    }

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

    public static BonesError NO_DELIMITER = new BonesError() {
        @Override
        public int getCode() {
            return 0x7;
        }

        @Override
        public String getMessage() {
            return "There were no tokens identified. Probably you have no delimiters";
        }
    };
}
