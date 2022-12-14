package edu.byuh.cis.cs203.frozensquares;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 8/24/17.
 */

public class Timer extends Handler {

    private List<TickListener> subscribers;

    /**
     * Create a new Timer with zero "subscribers"
     */
    public Timer() {
        subscribers = new ArrayList<>();
        sendEmptyMessageDelayed(0, 50);
    }

    /**
     * Allow a TickListener to receiver Timer events
     * @param t the new subscriber
     */
    public void subscribe(TickListener t) {
        subscribers.add(t);
    }

    /**
     * Remove a TickListener from the list of subscribers
     * @param t the subscriber to remove
     */
    public void unsubscribe(TickListener t) {
        subscribers.remove(t);
    }

    private void notifyListeners() {
        for (TickListener tl : subscribers) {
            tl.tick();
        }
    }

    @Override
    public void handleMessage(Message m) {
        notifyListeners();
        sendEmptyMessageDelayed(0, 50);
    }

}
