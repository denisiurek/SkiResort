package simulation.events.liftEvent;

import resort.athletes.Athlete;
import resort.topology.Lift;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.athleteEvent.AthleteEnterLiftEvent;

public class LiftDepartureEvent extends LiftEvent {
    private Athlete[] boardedAthletes;

    public LiftDepartureEvent(int time, Lift lift) {
        super(time, lift, LogLevel.DEBUG);
    }

    @Override
    public void execute(Scheduler scheduler) {

        boardedAthletes = lift.takePassengers(); // If thee lift carts were to be tracked this could become
        // a lift cart object that could interpolate its position based on nodes and departure-arrival times.
        for (Athlete athlete : boardedAthletes) {
            new AthleteEnterLiftEvent(getTime(), athlete, lift).execute(scheduler);
        }
        scheduler.scheduleEvent(new LiftArrivalEvent(getTime() + lift.getTravelTime(), lift, boardedAthletes));
        scheduler.scheduleEvent(new LiftDepartureEvent(getTime() + lift.getDepartureSpread(), lift));
        scheduler.log(this);
    }

    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Lift " + lift.getId() + " departed from " + lift.getSource().getId() + " with " + boardedAthletes.length + " athletes.";
    }
}
