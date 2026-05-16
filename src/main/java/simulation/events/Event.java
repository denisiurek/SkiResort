package simulation.events;

import simulation.LogLevel;
import simulation.Scheduler;
import timeUtils.TimeOperators;

public abstract class Event {
    private final int time;
    private final LogLevel loglevel;
    protected Event(int time, LogLevel loglevel) {
        this.time = time;
        this.loglevel = loglevel;
    }

    public final int getTime() {
        return time;
    }

    public abstract void execute(Scheduler scheduler);

    public final LogLevel getLogLevel() {
        return loglevel;
    }

    protected abstract String getEventDescription();

    public boolean isFinishable() {
        return false;
    }
    public final String toString() {
        return "["+TimeOperators.formatTime(time) +"] " + getEventDescription();
    }

}
