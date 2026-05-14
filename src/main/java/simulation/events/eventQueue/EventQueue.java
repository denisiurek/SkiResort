package simulation.events.eventQueue;

import simulation.events.Event;
import simulation.events.EventQueueEmptyException;


public interface EventQueue {
    void add(Event event);
    Event fetch() throws EventQueueEmptyException;
    boolean isEmpty();
}
