package barebones.logic;

import barebones.events.ErrorResponse;
import barebones.events.EventResponse;
import barebones.events.ResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mircea on 02/10/15.
 */
public class Interpreter {
    private final long TIMEOUT = 5000; //milliseconds

    private HashMap<String, Long> vars;
    private List<String> lines;

    private List<Integer> loops;
    private List<String> loop_cond;

    private Integer last_loop;
    private String last_cond;

    private boolean execute;
    private int toIgnore; //applies for nested loops

    private boolean faulty_compile;

    private ErrorCaught error;

    private long startTime;
    private long elapsed;

    public Interpreter() {
        vars = new HashMap<>();
        lines = new ArrayList<>();
        loops = new ArrayList<>();
        loop_cond = new ArrayList<>();
    }


    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse compile(String code) {
                Interpreter.this.run(code);

                if(faulty_compile) {
                    System.out.println("Faulty compile");
                    return new ErrorResponse(elapsed, false, error);
                }

                System.out.println("Successful compilation");
                return new ResultResponse(elapsed, false, vars);
            }
        };
    }

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

    private void run(String raw) {
        this.reset();
        this.rawToCode(raw);
        this.compile();
    }

    private boolean time_okay() {
        elapsed = (System.nanoTime() - startTime) / 1000000;
        return elapsed < TIMEOUT;
    }

    private void rawToCode(String raw) {
        String[] split = raw.split(";");

        int skip = 0;
        for(String token : split) {
            if(skip++%10==0?!time_okay():false) {
                this.setError(BonesError.TIMEOUT, -1);
                return;
            }
            lines.add(token.trim());
        }
    }

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
                System.out.println("Starting while");
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

        for (Long v : vars.values())
            System.out.println(v);
    }

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

                System.out.println("Clear: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("incr")) {
                var = words[1];

                if (!vars.containsKey(var)) {
                    this.setError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) + 1);
                System.out.println("Incr: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("decr")) {
                var = words[1];

                if (!vars.containsKey(var)) {
                    this.setError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) - 1);
                System.out.println("Decr: " + var);
                return ParseReply.OK;
            } else {
                this.setError(BonesError.SYNTAX_UNKNOWN, line_no);
                return ParseReply.ERROR;
            }
        }

        this.setError(BonesError.SYNTAX_UNKNOWN, line_no);
        return ParseReply.ERROR;
    }

    private void setError(BonesError err, int line) {
        System.out.println("Error caught: " + err.getMessage());
        error = new ErrorCaught(err, line);
        faulty_compile = true;
    }

    private enum ParseReply {
        OK,
        START,
        END,
        ERROR
    }
}
