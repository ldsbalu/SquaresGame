package com.example.splashscreen;

/**
 * Tick listener interface, which allows us to implement the handler subclass <code>MoveHandler</code>
 * as it's own separate file class, instead of an inner class.
 * */
public interface TickListener {
    void tick();
}
