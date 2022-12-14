package com.example.spellingGameOne.sys;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * MoveHandler
 */
public class MoveHandler extends Handler {

    private List<TickListener> listeners;
    protected int delay;
    private static MoveHandler instance;
    /**
     * Constructor, starts the delayedMessage cycle
     */
    private MoveHandler(int d) {
        delay = d;
        listeners = new ArrayList<>();
        sendMessageDelayed(obtainMessage(), 0);
    }

    /**
     * Adds the supplied t to the listeners array
     * @param t
     */
    public void register(TickListener t) {
        listeners.add(t);
    }

    /**
     * Removes the t from the listeners array
     * @param t
     */
    public void unregister(TickListener t) {
        listeners.remove(t);
    }

    /**
     * send the message to tick to the various listeners!
     */
    public void alertListeners() {
        for (TickListener t : listeners) {
            t.tick();
        }
    }

    /**
     * alert the listeners
     */
    @Override
    public void handleMessage(Message e) {

        alertListeners();
        sendMessageDelayed(obtainMessage(), delay);

    }

    /**
     * Return the singleton instance of the MoveHandler Class
     */
    public static MoveHandler getInstance(int d) {
        if(instance == null) {
            instance = new MoveHandler(d);
        }
        return instance;
    }
}
