package barebones.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mircea on 02/10/15.
 */
public class Interpreter {
    private HashMap<String, Long> vars;
    private List<String> lines;

    private List<Integer> loops;
    private List<String> loop_cond;

    private Integer last_loop;
    private String last_cond;

    private boolean execute;
    private int toIgnore; //applies for nested loops

    public Interpreter() {
        vars = new HashMap<>();
        lines = new ArrayList<>();
        loops = new ArrayList<>();
        loop_cond = new ArrayList<>();
    }

    private HashMap<String, Long> start() {
        return vars;
    }

    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse compile(String code) {
                Interpreter.this.compile(code);
                return new ResultResponse(vars, false);
            }
        };
    }

    private void reset() {
        vars.clear();
        lines.clear();
        loops.clear();
        loop_cond.clear();
        this.execute = true;
        toIgnore = 0;

        System.out.println("Compiling...");
    }

    private void compile(String code) {
        this.reset();

        Pattern pattern = Pattern.compile("(.+?);");
        Matcher matcher = pattern.matcher(code);

        while(matcher.find())
            lines.add(matcher.group(1).trim());

        int len = lines.size();
        for (int i=0 ; i<len ; i++) {
            ParseReply reply = parseLine(lines.get(i));
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

        for (Long v : vars.values())
            System.out.println(v);
    }

    private ParseReply parseLine(String line) {
        //System.out.println(line);
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
                    System.out.println("Variable not cleared");
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) + 1);
                System.out.println("Incr: " + var);
            } else if (words[0].equals("decr")) {
                var = words[1];

                if (!vars.containsKey(var)) {
                    System.out.println("Variable not cleared");
                    return ParseReply.ERROR;
                }

                vars.put(var, vars.get(var) - 1);
                System.out.println("Decr: " + var);
            }
        }

        return ParseReply.ERROR;
    }

    private enum ParseReply {
        OK,
        START,
        END,
        ERROR
    }
}
