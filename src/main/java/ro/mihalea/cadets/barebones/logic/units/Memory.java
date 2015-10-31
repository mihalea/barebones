package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.NoValueAssignedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Logic unit that handles all the variables and their declaration
 */
public class Memory {
    /**
     * HashMap containing the values for all the variables used in the current run
     */
    private HashMap<String, Long> variables;

    /**
     * Instantiating the field
     */
    public Memory() {
        variables = new HashMap<>();
    }

    /**
     * Gets the value if previously declared, otherwise throws an exception
     * @param key Name of the variable
     * @return Value of the variable
     * @throws NoValueAssignedException No value has been previously assigned to the varible
     */
    public long get(String key) throws NoValueAssignedException {
        if(variables.containsKey(key))
            return variables.get(key);
        else
            throw new NoValueAssignedException(-1);
    }

    /**
     * Creates or sets the variable with the value provided
     * @param key Name of the variable
     * @param value Value of the variable
     */
    public void set(String key, long value) {
        variables.put(key, value);
    }

    /**
     * Returns whether the variable has been previously declared in the current environment
     * @param key Name of the variable
     * @return True if variable has been previously declared
     */
    public boolean exists(String key){
        return variables.containsKey(key);
    }

    /**
     * Prints the variables and their values to the console
     * Used mainly for debugging purposes
     */
    public void print() {
        System.out.println("\nMEMORY DUMP:");
        for(Map.Entry<String, Long> entry : variables.entrySet())
            System.out.println(entry.getKey() + ": " + entry.getValue());
        System.out.println();
    }

    /**
     * Clear the memory
     */
    public void clear() {
        variables.clear();
    }
}
