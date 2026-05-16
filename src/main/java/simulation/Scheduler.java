package simulation;

import simulation.events.Event;

public interface Scheduler {
    void scheduleEvent(Event event);
    int getCurrentTime();
    void log(Event event);
}
