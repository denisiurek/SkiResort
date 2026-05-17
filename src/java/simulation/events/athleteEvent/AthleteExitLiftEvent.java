package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;

public class AthleteExitLiftEvent extends AthleteEvent {
    private final Lift lift;

    public AthleteExitLiftEvent(int time, Athlete athlete, Lift lift) {
        super(time, athlete, athlete.isTracked() ? LogLevel.PRODUCTION : LogLevel.DEBUG);
        this.lift = lift;
    }

    @Override
    public void execute(Scheduler scheduler) {
        lift.registerExit();
        scheduler.log(this);
        scheduler.scheduleEvent(new AthleteArriveAtNodeEvent(scheduler.getCurrentTime(), athlete, lift.getDestination()));
    }


    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " exited lift " + lift.getId() + " at node " + lift.getDestination().getId();
    }
}
