package collections.eventQueue;

import simulation.events.Event;


public interface EventQueue {
    void add(Event event);

    Event fetch() throws EventQueueEmptyException;

    Event peek() throws EventQueueEmptyException;
    boolean isEmpty();
}
