package ro.mihalea.cadets.barebones.logic.exceptions;

public class BlockUnfinishedException extends BonesException {
    public BlockUnfinishedException() {
        this("", -1);
    }

    public BlockUnfinishedException(String additional) {
        this(additional, -1);
    }

    public BlockUnfinishedException(int line) {
        this("", line);
    }

    public BlockUnfinishedException(String additional, int line) {
        super("One or more block instructions do not have an end", additional, line);
    }
}
