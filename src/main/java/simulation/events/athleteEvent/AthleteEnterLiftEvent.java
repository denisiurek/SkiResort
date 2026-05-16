package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.NonSchedulableEvent;

public class AthleteEnterLiftEvent extends AthleteEvent implements NonSchedulableEvent {
    private final Lift lift;
    public AthleteEnterLiftEvent(int time, Athlete athlete, Lift lift) {
        super(time, athlete, athlete.isTracked() ? LogLevel.PRODUCTION : LogLevel.DEBUG);
        this.lift = lift;
    }

    @Override
    public void execute(Scheduler scheduler) {
        lift.registerEntry();
        scheduler.log(this);
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " entered lift " + lift.getId() + " from " + lift.getSource().getId() + " to " + lift.getDestination().getId();
    }
}
