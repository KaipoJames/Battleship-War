package edu.byuh.cis.cs203.bw_i18n_2019.misc;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class Timer extends Handler {

    private List<TickListener> observers;
    private boolean paused;

    /**
     * the public constructor. Gets the timer started.
     */
    public Timer() {
        observers = new ArrayList<>();
        resume();
    }

    /**
     * lets you register new observers to the timer
     * @param t an object that wants to be notified of timer events
     */
    public void subscribe(TickListener t) {
        observers.add(t);
    }

    /**
     * lets you remove observers from the timer
     * @param t an object who no longer wants to be notified of timer events
     */
    public void unsubscribe(TickListener t) {
        observers.remove(t);
    }

//    public void unsubscribe(Predicate<TickListener> p) {
//        observers.removeIf(p);
//    }

    public void unsubscribeAll(List<TickListener> batch) {
        observers.removeAll(batch);
    }

    /**
     * We override this method to notify the observers that the timer went off
     * @param msg we're not using this parameter
     */
    @Override
    public void handleMessage(Message msg) {
        notifyObservers();
        if (!paused) {
            sendMessageDelayed(obtainMessage(), 50);
        }
    }

    private void notifyObservers() {
        for (TickListener t : observers) {
            t.tick();
        }
    }

    /**
     * Temporarily stop the timer
     */
    public void pause() {
        paused = true;
    }

    /**
     * Restart the timer after it's been paused
     */
    public void resume() {
        paused = false;
        sendMessageDelayed(obtainMessage(), 0);
    }
}
