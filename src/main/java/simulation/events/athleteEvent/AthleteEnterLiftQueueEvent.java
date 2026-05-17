package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;

public class AthleteEnterLiftQueueEvent extends AthleteEvent  {
    private final Lift lift;
    public AthleteEnterLiftQueueEvent(int time, Athlete athlete, Lift lift) {
        super(time, athlete, athlete.isTracked() ? LogLevel.PRODUCTION : LogLevel.DEBUG);
        this.lift = lift;
    }

    @Override
    public void execute(Scheduler scheduler) {
        lift.enqueue(athlete);
        scheduler.log(this);
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " entered the queue for lift " + lift.getId();
    }
}
