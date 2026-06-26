package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Connection;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.NonSchedulableEvent;
import simulation.events.TravelEvent;

public class AthleteExitLiftEvent extends AthleteEvent implements NonSchedulableEvent, TravelEvent {
    private final Lift lift;

    public AthleteExitLiftEvent(int time, Athlete athlete, Lift lift) {
        super(time, athlete, athlete.isTracked() ? LogLevel.PRODUCTION : LogLevel.DEBUG);
        this.lift = lift;
    }

    @Override
    public void execute(Scheduler scheduler) {
        lift.registerExit();
        scheduler.runtimeLog(this);
        scheduler.executeEvent(new AthleteArriveAtNodeEvent(scheduler.getCurrentTime(), athlete, lift.getDestination()));
    }


    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " exited lift " + lift.getId() + " at node " + lift.getDestination().getId();
    }

    @Override
    public Connection getConnection() {
        return this.lift;
    }
}
