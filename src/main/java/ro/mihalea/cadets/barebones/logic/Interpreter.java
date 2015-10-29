package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.events.ResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class in which all the backend logic takes place.
 * The objects listens to any {@link Listener#interpret(String)} events triggered
 * and then act accordingly.
 */
public class Interpreter {
    /**
     * Constant field which describes the amount of milliseconds after the
     * interpreter should return {@link ro.mihalea.cadets.barebones.events.ErrorResponse}
     * with the code 0x5.
     */
    private final long TIMEOUT = 5000; //milliseconds

    /**
     * Map that holds all the variables that are used by the interpreter
     * in one run of a program. At every new run the map gets cleared.
     */
    private HashMap<String, Long> vars;

    /**
     * List that holds all statements after being split with the
     * {@link java.lang.String#split(String, int)} with ";" as a delimiter
     */
    private List<String> statements;

    /**
     * List that acts as a stack and holds all the line numbers at which a loop
     * has been started. This field is a pair with {@link ro.mihalea.cadets.barebones.logic.Interpreter#whileVariable}
     */
    private List<Integer> whileStartIndex;

    /**
     * List that acts as a stack and holds all the variables that the whiles
     * are checked against. This field is a pair with {@link ro.mihalea.cadets.barebones.logic.Interpreter#whileStartIndex}
     */
    private List<String> whileVariable;

    /**
     * Holds the line number of the current while loop.
     */
    private Integer lastWhileIndex;

    /**
     * Holds the name of the variable of current while loop.
     */
    private String lastWhileVariable;

    /**
     * State variable which when false indicates that the interpreter
     * has ran into a while that failed to verify its condition
     * therefore any of the statements inside are not to be executed
     */
    private boolean execute;

    /**
     * Field used to also ignore the nested whiles inside one which failed
     * to verify. When it gets to zero it means that we have also exited
     * the initial while which set the {@link ro.mihalea.cadets.barebones.logic.Interpreter#execute}
     * to true;
     */
    private int toIgnore;

    /**
     * Field which holds whether the interpreter has faulted and should
     * return an {@link ro.mihalea.cadets.barebones.events.ErrorResponse}
     */
    private boolean faulty_compile;

    /**
     * Error which should be returned in the event response
     */
    private ErrorCaught error;

    /**
     * Epoch time at which the interpreter has started running.
     */
    private long startTime;

    /**
     * Time in milliseconds since the interpreter has started running
     */
    private long elapsedTime;

    /**
     * Initializes all the lists and maps used by the interpreter
     */
    public Interpreter() {
        vars = new HashMap<>();
        statements = new ArrayList<>();
        whileStartIndex = new ArrayList<>();
        whileVariable = new ArrayList<>();
    }


    /**
     * Method which sets up a new instance of the {@link ro.mihalea.cadets.barebones.logic.Listener} with
     * its abstract method implemented inline which passes the raw code to the interpreter
     * @return New instance of the {@link ro.mihalea.cadets.barebones.logic.Listener}
     */
    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse interpret(String code) {
                Interpreter.this.run(code);

                if(faulty_compile) {
                    System.err.println("Faulty interpret");
                    return new ErrorResponse(elapsedTime, false, error);
                }

                System.out.println("Successful compilation");
                return new ResultResponse(elapsedTime, false, vars);
            }
        };
    }

    /**
     * Resets all the variables used for a new program
     * to be interpreted.
     */
    private void reset() {
        vars.clear();
        statements.clear();
        whileStartIndex.clear();
        whileVariable.clear();
        error = null;
        execute = true;
        toIgnore = 0;
        faulty_compile = false;
        startTime = System.nanoTime();

        System.out.println("Compiling...");
    }

    /**
     * Starts the compiler with a new sequence of code
     * @param raw Raw unmodified program
     */
    private void run(String raw) {
        this.reset();
        this.rawToCode(raw);
        this.interpret();
    }

    /**
     * Method that checks whether the time elapsed running the interpreter
     * is still under the threshold specified under {@link ro.mihalea.cadets.barebones.logic.Interpreter#TIMEOUT}.
     * @return Returns if the interpreter is still under the timeout threshold
     */
    private boolean time_okay() {
        // converts from nano to milli (10^-9 to 10^-3)
        elapsedTime = (System.nanoTime() - startTime) / 1000000;
        return elapsedTime < TIMEOUT;
    }

    /**
     * Takes a raw program and converts it into statements that are to be
     * parsed by the interpreter using {@link java.lang.String#split(String, int)} with ";" as a delimiter
     * @param raw Raw program as a continuous {@link java.lang.String}
     */
    private void rawToCode(String raw) {
        String[] split = raw.split(";");


        for(String token : split) {
            /**
             * Once every 1k lines it checks whether the interpreter
             * has overcome the {@link TIMEOUT} threshold.
             */
            if(statements.size()%1000==0 && !time_okay()) {
                this.throwError(BonesError.TIMEOUT, -1);
                return;
            }

            /**
             *  Only non-empty lines should be added
             */
            if(token.length() > 0)
                statements.add(token.trim());
        }
    }

    /**
     * Starts the compilation of the currently loaded statements.
     */
    private void interpret() {
        //Is this good programming practice?
        if(faulty_compile)
            return;

        int len = statements.size();
        for (int i=0 ; i<len && !faulty_compile ; i++) {
            /**
             * Checks whether the interpreter should parse any more lines
             * depending on whether it is still under the time threshold
             */
            if(!time_okay()) {
                this.throwError(BonesError.TIMEOUT, i+1);
                return;
            }

            ParseReply reply = parseStatement(statements.get(i), i + 1);
            /**
             * A while has started
             */
            if(reply == ParseReply.START) {
                /**
                 * Set current line as the index of the last while started
                 * and add it to the list
                 */
                lastWhileIndex = i + 1;
                whileStartIndex.add(lastWhileIndex);
                //System.out.println("Starting while");
            }
            /**
             * A while has ended
             */
            else if(reply == ParseReply.END) {
                /**
                 * Check the while condition: if the variable is still bigger
                 * than 0
                 */
                if(vars.get(lastWhileVariable) != 0)
                    /**
                     * Setting the i as the lastWhileIndex so that after
                     * it is incremented in will parse the first line
                     */
                    i = lastWhileIndex - 1;
                else {
                    /**
                     * After the variable gets to 0 remove the last elements
                     * from the lists and if there are more whiles (nested)
                     * set the current while vars to the previous in the lists
                     */
                    whileStartIndex.remove(whileStartIndex.size()-1);
                    if(whileStartIndex.size() > 0)
                        lastWhileIndex = whileStartIndex.get(whileStartIndex.size()-1);

                    whileVariable.remove(whileVariable.size() - 1);
                    if(whileVariable.size() > 0)
                        lastWhileVariable = whileVariable.get(whileVariable.size() - 1);
                }
            }
        }

        /**
         * Throw an error if there are no more lines to be parsed
         * but there are one or more whiles that have not finished
         */
        if(whileStartIndex.size() > 0 || whileVariable.size() > 0)
            this.throwError(BonesError.NO_END, -1);

        /**
         * Print the keys to the console for debugging purposes
         */
        for (Map.Entry<String, Long> e : vars.entrySet())
            System.out.println(e.getKey() + " = " + e.getValue());
    }

    /**
     * Analyzes the line and return a {@link ro.mihalea.cadets.barebones.logic.Interpreter.ParseReply}
     * depending on the outcome of the parser
     * @param line Text containing the statement
     * @param line_no Line number
     * @return Response signaling the outcome of the parser
     */
    private ParseReply parseStatement(String line, final int line_no) {
        //name of the current variable
        String var;

        //tokens in the current statement
        String[] words = line.split(" ");

        /**
         * Because we have a limited syntax we know what kind of instructions
         * we should expect depending on the number of tokes
         */
        if (words.length == 1 && words[0].equals("end")) {
            /**
             * Checks whether a while has been started before trying to end it
             */
            if(whileStartIndex.size() == 0) {
                this.throwError(BonesError.NO_START, line_no);
                return ParseReply.ERROR;
            }

            /**
             * Checks to see if there are more whiles to be ignored because an
             * outer one has failed to verify its condition
             * If {@link toIgnore} gets to zero that means that we have exited
             * the while that failed to verify and we resume normal operation
             */
            if(!execute) {
                if (toIgnore == 0)
                    execute = true;
                else
                    toIgnore--;

                return ParseReply.OK;
            }
            return ParseReply.END;
        } else if (words.length == 5) {
            if (words[0].equals("while") && words[2].equals("not") &&
                    words[3].equals("0") && words[4].equals("do")) {

                /**
                 * Parse only if we are not inside a while that failed to check
                 * its condition
                 */
                if(execute) {
                    /**
                     * Checks the variable as the initial condition check. If failing to
                     * find the variable or it is already set to zero it will
                     * skip parsing until it reaches its matching "end" statement
                     */
                    if(!vars.containsKey(words[1]) || vars.get(words[1]) == 0) {
                        execute = false;
                        return ParseReply.OK;
                    }
                    lastWhileVariable = words[1];
                    whileVariable.add(lastWhileVariable);
                } else {

                    /**
                     * A while has been detected inside another one that failed
                     * to verify its initial condition
                     */
                    toIgnore++;
                }

                return ParseReply.START;
            } else if (words[0].equals("while")) {
                /**
                 * Verifying loosely if the current statement should be a while
                 * but without a correct syntax
                 */
                this.throwError(BonesError.SYNTAX_WHILE, line_no);
                return ParseReply.ERROR;
            } else {

                /**
                 * If all other branches failed to verify it means that the current
                 * statement is unknown to the compiler
                 */
                this.throwError(BonesError.SYNTAX_UNKNOWN, line_no);
                return ParseReply.ERROR;
            }

        } else if (words.length == 2 && execute) {
            if (words[0].equals("clear")) {
                var = words[1];
                vars.put(var, 0l); //THIS IS ZERO L, NOT ZERO ONE

                //System.out.println("Clear: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("incr")) {
                var = words[1];

                /**
                 * Checks whether the variable has been declared beforehand
                 */
                if (!vars.containsKey(var)) {
                    this.throwError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) + 1);
                //System.out.println("Incr: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("decr")) {
                var = words[1];

                /**
                 * Checks whether the variable has been declared beforehand
                 */
                if (!vars.containsKey(var)) {
                    this.throwError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) - 1);
                //System.out.println("Decr: " + var);
                return ParseReply.OK;
            } else {
                /**
                 * If all other branches failed to verify it means that the current
                 * statement is unknown to the compiler
                 */

                this.throwError(BonesError.SYNTAX_UNKNOWN, line_no);
                return ParseReply.ERROR;
            }
        }

        /**
         * If all other branches failed to verify it means that the current
         * statement is unknown to the compiler
         */
        this.throwError(BonesError.SYNTAX_UNKNOWN, line_no);
        return ParseReply.ERROR;
    }

    /**
     * Generates and sets the {@link ro.mihalea.cadets.barebones.logic.ErrorCaught} while
     * also printing an error message to the console for debugging purposes.
     * @param err Error type
     * @param line Line on which the error occurred
     */
    private void throwError(BonesError err, int line) {
        System.err.println("Error caught: " + err.getMessage());
        error = new ErrorCaught(err, line);
        faulty_compile = true;
    }

    /**
     * Enumeration holding the four possible outcomes
     * of the {@link Interpreter#interpret()} method.
     */
    private enum ParseReply {
        /**
         * Notifies the {@link Interpreter} that the statements has been parsed successfully
         */
        OK,

        /**
         * Notifies the {@link Interpreter} that a "while" block has begun
         */
        START,

        /**
         * Notifies the {@link Interpreter} that a "while" block has ended
         */
        END,

        /**
         * Notifies the {@link Interpreter} that the line has failed to be parsed
         */
        ERROR
    }
}
