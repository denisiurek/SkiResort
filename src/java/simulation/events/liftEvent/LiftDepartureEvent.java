package simulation.events.liftEvent;

import resort.athletes.Athlete;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.athleteEvent.AthleteEnterLiftEvent;

import java.util.List;

public class LiftDepartureEvent extends LiftEvent {
    private List<Athlete> boardedAthletes;

    public LiftDepartureEvent(int time, Lift lift) {
        super(time, lift, LogLevel.DEBUG);
    }

    @Override
    public void execute(Scheduler scheduler) {

        boardedAthletes = lift.takePassengers(getTime());

        for (Athlete athlete : boardedAthletes) {
            scheduler.executeEvent(new AthleteEnterLiftEvent(getTime(), athlete, lift));
        }
        scheduler.runtimeLog(this);
        scheduler.scheduleEvent(new LiftArrivalEvent(getTime() + lift.getTravelTime(), lift, boardedAthletes));
        scheduler.scheduleEvent(new LiftDepartureEvent(getTime() + lift.getDepartureSpread(), lift));

    }

    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Lift " + lift.getId() + " departed from " + lift.getSource().getId() + " with " + boardedAthletes.size() + " athletes.";
    }
}
