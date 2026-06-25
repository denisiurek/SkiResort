package simulation.events;

import simulation.LogLevel;
import simulation.Scheduler;
import simulation.TimeHelper;

public abstract class Event implements Comparable<Event> {
    private final int time;
    private final LogLevel loglevel;
    private int orderPriority;
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
        return TimeHelper.formatTime(time) + ": " + getEventDescription();
    }

    public int compareTo(Event other) {
         int cmp = Integer.compare(this.time, other.time);

        return cmp != 0 ? cmp : Integer.compare(this.orderPriority, other.orderPriority);
    }

    public void setPriority(int orderPriority) {
        this.orderPriority = orderPriority;
    }
}
