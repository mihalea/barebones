package barebones.logic;

import barebones.events.ErrorResponse;
import barebones.events.EventResponse;
import barebones.events.ResultResponse;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mircea on 02/10/15.
 */
public class Interpreter implements Runnable {
    private final long TIMEOUT = 5000; //milliseconds

    private HashMap<String, Long> vars;
    private List<String> lines;
    private String raw;

    private List<Integer> loops;
    private List<String> loop_cond;

    private Integer last_loop;
    private String last_cond;

    private boolean execute;
    private int toIgnore; //applies for nested loops

    private boolean faulty_compile;

    private List<ErrorCaught> errors;

    private long startTime;

    public Interpreter() {
        vars = new HashMap<>();
        lines = new ArrayList<>();
        loops = new ArrayList<>();
        loop_cond = new ArrayList<>();
        errors = new ArrayList<>();
    }

    private HashMap<String, Long> start() {
        return vars;
    }

    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse compile(String code) {
                Interpreter.this.raw = code;
                Interpreter.this.run();

                if(faulty_compile) {
                    System.out.println("Faulty compile");
                    return new ErrorResponse(errors, false);
                }

                System.out.println("Successful compilation");
                return new ResultResponse(vars, false);
            }
        };
    }

    private void reset() {
        vars.clear();
        lines.clear();
        loops.clear();
        loop_cond.clear();
        errors.clear();
        execute = true;
        toIgnore = 0;
        faulty_compile = false;
        startTime = System.nanoTime();

        System.out.println("Compiling...");
    }

    @Override
    public void run() {
        this.reset();
        this.rawToCode();
        this.compile();
    }

    private boolean time_okay() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        return elapsed < TIMEOUT;
    }

    private void rawToCode() {
        lines.clear();

        Pattern pattern = Pattern.compile("(.+?);");
        Matcher matcher = pattern.matcher(raw);

        int skip = 0;
        while(matcher.find() && (skip%10==0?time_okay():true))
            lines.add(matcher.group(1).trim());
    }

    private void compile() {
        int len = lines.size();
        for (int i=0 ; i<len && !faulty_compile ; i++) {
            if(!time_okay()) {
                this.addError(BonesError.TIMEOUT, -1);
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
            this.addError(BonesError.NO_END, -1);

        for (Long v : vars.values())
            System.out.println(v);
    }

    private ParseReply parseLine(String line, final int line_no) {
        String var;

        String[] words = line.split(" ");
        if (words.length == 1 && words[0].equals("end")) {
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
            } else if (!words[0].equals("while")) {
                this.addError(BonesError.SYNTAX_WHILE, line_no);
                return ParseReply.ERROR;
            } else {
                this.addError(BonesError.SYNTAX_UNKNOWN, line_no);
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
                    this.addError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) + 1);
                System.out.println("Incr: " + var);
                return ParseReply.OK;
            } else if (words[0].equals("decr")) {
                var = words[1];

                if (!vars.containsKey(var)) {
                    this.addError(BonesError.NOT_CLEARED, line_no);
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) - 1);
                System.out.println("Decr: " + var);
                return ParseReply.OK;
            }
        }

        this.addError(BonesError.SYNTAX_UNKNOWN, line_no);
        return ParseReply.ERROR;
    }

    private void addError(BonesError error, int line) {
        System.out.println("Error caught: " + error.getMessage());
        errors.add(new ErrorCaught(error, line));
        faulty_compile = true;
    }

    private enum ParseReply {
        OK,
        START,
        END,
        ERROR
    }
}
