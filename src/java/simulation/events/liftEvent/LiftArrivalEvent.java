package simulation.events.liftEvent;

import resort.athletes.Athlete;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.athleteEvent.AthleteExitLiftEvent;

public class LiftArrivalEvent extends LiftEvent {
    private final Athlete[] boardedAthletes;

    public LiftArrivalEvent(int time, Lift lift, Athlete[] boardedAthletes) {
        super(time, lift, LogLevel.DEBUG);
        this.boardedAthletes = boardedAthletes;
    }

    @Override
    public void execute(Scheduler scheduler) {
        scheduler.log(this);
        for (Athlete athlete : boardedAthletes) {
            scheduler.executeEvent(new AthleteExitLiftEvent(scheduler.getCurrentTime(), athlete, lift));
        }
    }

    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Lift " + lift.getId() + " arrived at node " + lift.getDestination().getId() + " with " + boardedAthletes.length + " athletes.";
    }
}
