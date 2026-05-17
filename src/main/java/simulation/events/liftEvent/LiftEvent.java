package simulation.events.liftEvent;

import resort.topology.Lift;
import simulation.LogLevel;
import simulation.events.Event;

abstract public class LiftEvent extends Event {
    protected final Lift lift;
    protected LiftEvent(int time, Lift lift, LogLevel logLevel) {
        super(time, logLevel);
        this.lift = lift;
    }
}
