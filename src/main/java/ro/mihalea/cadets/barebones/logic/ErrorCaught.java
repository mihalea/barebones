package ro.mihalea.cadets.barebones.logic;

/**
 * Created by mm8g15 on 10/5/2015.
 */
public class ErrorCaught {
    public BonesError error;
    public int line;

    public ErrorCaught(BonesError error, int line) {
        this.error = error;
        this.line = line;
    }
}
