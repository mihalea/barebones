package ro.mihalea.cadets.barebones.logic;

/**
 * Wrapper class to easier pass both fields between objects
 * without the use of a Pair
 */
public class Line {
    /**
     * Raw line of code containing a single instruction
     */
    private String code;

    /**
     * Index of the line in the current environment
     */
    private int index;

    /**
     * Instantiate the wrapper object
     * @param code Raw line of code
     * @param index Index of the line
     */
    public Line(String code, int index) {
        this.code = code;
        this.index = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Line)
            return ((Line)obj).code.equals(this.code);
        else if(obj instanceof String)
            return obj.equals(this.code);
        else
            return false;
    }

    public String getCode() {
        return code;
    }

    public int getIndex() {
        return index;
    }
}