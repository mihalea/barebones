package ro.mihalea.cadets.barebones.logic.exceptions;

public class NotTerminatedExpection extends BonesException {
    public NotTerminatedExpection() {
        this("", -1);
    }

    public NotTerminatedExpection(String additional) {
        this(additional, -1);
    }

    public NotTerminatedExpection(int line) {
        this("", line);
    }

    public NotTerminatedExpection(String additional, int line) {
        super("One or more instructions are not terminated using the semicolon", additional, line);
    }
}
