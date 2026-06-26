package simulation;

import simulation.events.Event;

public interface Scheduler {
    void scheduleEvent(Event event);
    void executeEvent(Event event);
    int getCurrentTime();

    void runtimeLog(Event event);
}
