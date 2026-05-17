package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import simulation.LogLevel;
import simulation.events.Event;

public abstract class AthleteEvent extends Event {
    protected final Athlete athlete;

    protected AthleteEvent(int time, Athlete athlete, LogLevel logLevel) {
        super(time, logLevel);
        this.athlete = athlete;
    }
    public Athlete getAthlete() {
        return athlete;
    }
}
