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
    private List<String> lines;

    /**
     * List that acts as a stack and holds all the line numbers at which a loop
     * has been started. This field is a pair with {@link ro.mihalea.cadets.barebones.logic.Interpreter#loop_cond}
     */
    private List<Integer> loops;

    /**
     * List that acts as a stack and holds all the variables that the whiles
     * are checked against. This field is a pair with {@link ro.mihalea.cadets.barebones.logic.Interpreter#loops}
     */
    private List<String> loop_cond;

    /**
     * Holds the line number of the current while loop.
     */
    private Integer last_loop;

    /**
     * Holds the name of the variable of current while loop.
     */
    private String last_cond;

    /**
     * State variable that tells the interpreter whether it should
     * keep parsing the next lines.
     */
    private boolean execute;

    /**
     * Field used to tell the interpreter not to parse any of the
     * statements that are inside a while loop that has failed the
     * variable check
     */
    private int toIgnore; //applies for nested loops

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
     * Timestamp in nanoseconds since UNIX era at which the interpreter
     * has started running.
     */
    private long startTime;
    /**
     * Time in milliseconds since the interpreter has started running
     */
    private long elapsed;

    /**
     * Initializes all the lists and maps used by the interpreter
     */
    public Interpreter() {
        vars = new HashMap<>();
        lines = new ArrayList<>();
        loops = new ArrayList<>();
        loop_cond = new ArrayList<>();
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
                    System.out.println("Faulty interpret");
                    return new ErrorResponse(elapsed, false, error);
                }

                System.out.println("Successful compilation");
                return new ResultResponse(elapsed, false, vars);
            }
        };
    }

    /**
     * Resets all the variables used for a new program
     * to be interpreted.
     */
    private void reset() {
        vars.clear();
        lines.clear();
        loops.clear();
        loop_cond.clear();
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
        this.compile();
    }

    /**
     * Method that checks whether the time elapsed running the interpreter
     * is still under the threshold specified under {@link ro.mihalea.cadets.barebones.logic.Interpreter#TIMEOUT}.
     * @return Returns if the interpreter is still under the timeout threshold
     */
    private boolean time_okay() {
        // converts from nano to milli (10^-9 to 10^-3)
        elapsed = (System.nanoTime() - startTime) / 1000000;
        return elapsed < TIMEOUT;
    }

    /**
     * Takes a raw program and converts it into statements that are to be
     * parsed by the interpreter using {@link java.lang.String#split(String, int)} with ";" as a delimiter
     * @param raw Raw program as a continuous {@link java.lang.String}
     */
    private void rawToCode(String raw) {
        String[] split = raw.split(";");

        int skip = 0;
        for(String token : split) {
            if(skip++%10==0?!time_okay():false) {
                this.setError(BonesError.TIMEOUT, -1);
                return;
            }
            if(token.length() > 0)
                lines.add(token.trim());
        }
    }

    /**
     * Starts the compilation of the currently loaded statements.
     */
    private void compile() {
        if(faulty_compile)
            return;

        int len = lines.size();
        for (int i=0 ; i<len && !faulty_compile ; i++) {
            if(!time_okay()) {
                this.setError(BonesError.TIMEOUT, -1);
                return;
            }

            ParseReply reply = parseLine(lines.get(i), i + 1);
            if(reply == ParseReply.START) {
                last_loop = i + 1;
                loops.add(last_loop);
                //System.out.println("Starting while");
            }
            else if(reply == ParseReply.END) {
                if(vars.get(last_cond) != 0)
                    i = last_loop - 1; //handle the for incrementation
                else {
                    loops.remove(loops.size()-1);
                    if(loops.size() > 0)
                        last_loop = loops.get(loops.size()-1);

                    loop_cond.remove(loop_cond.size() - 1);
                    if(loop_cond.size() > 0)
                        last_cond = loop_cond.get(loop_cond.size() - 1);
                }
            }
        }

        if(loops.size() > 0 || loop_cond.size() > 0)
            this.setError(BonesError.NO_END, -1);

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
    private ParseReply parseLine(String line, final int line_no) {
        String var;

        String[] words = line.split(" ");
        if (words.length == 1 && words[0].equals("end")) {
            if(loops.size() == 0) {
                this.setError(BonesError.NO_START, line_no);
                return ParseReply.ERROR;
            }

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
                if(execute) {
                    if(!vars.containsKey(words[1]) || vars.get(words[1]) == 0) {
                        execute = false;
                        return ParseReply.OK;
                    }
                    last_cond = words[1];
                    loop_cond.add(last_cond);
                } else {
                    toIgnore++;
                }

                return ParseReply.START;
            } else if (words[0].equals("while")) {
                this.setError(BonesError.SYNTAX_WHILE, line_no);
                return ParseReply.ERROR;
            } else {
                this.setError(BonesError.SYNTAX_UNKNOWN, line_no);
                return ParseReply.ERROR;
            }

        } else if (words.length == 2 && execute) {
            if (words[0].equals("clear")) {
                var = words[1];
                vars.put(var, 0l);

                //System.out.println("Clear: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("incr")) {
                var = words[1];

                if (!vars.containsKey(var)) {
                    this.setError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) + 1);
                //System.out.println("Incr: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("decr")) {
                var = words[1];

                if (!vars.containsKey(var)) {
                    this.setError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) - 1);
                //System.out.println("Decr: " + var);
                return ParseReply.OK;
            } else {
                this.setError(BonesError.SYNTAX_UNKNOWN, line_no);
                return ParseReply.ERROR;
            }
        }

        this.setError(BonesError.SYNTAX_UNKNOWN, line_no);
        return ParseReply.ERROR;
    }

    /**
     * Generates and sets the {@link ro.mihalea.cadets.barebones.logic.ErrorCaught} while
     * also printing an error message to the console for debugging purposes.
     * @param err Error type
     * @param line Line on which the error occurred
     */
    private void setError(BonesError err, int line) {
        System.err.println("Error caught: " + err.getMessage());
        error = new ErrorCaught(err, line);
        faulty_compile = true;
    }

    /**
     * Enumeration holding the four possible outcomes
     * of the {@link Interpreter#compile()} method.
     */
    private enum ParseReply {
        /**
         * Notifies the {@link Interpreter} that the lines has been parsed successfully
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
