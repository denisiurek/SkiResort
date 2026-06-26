package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Connection;
import resort.topology.Route;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.TravelEvent;

public class AthleteExitRouteEvent extends AthleteEvent implements TravelEvent {
    private final Route route;

    public AthleteExitRouteEvent(int time, Athlete athlete, Route route) {
        super(time, athlete, athlete.isTracked() ? LogLevel.PRODUCTION : LogLevel.DEBUG);
        this.route = route;
    }

    @Override
    public void execute(Scheduler scheduler) {
        route.registerExit();
        scheduler.runtimeLog(this);
        scheduler.executeEvent(new AthleteArriveAtNodeEvent(scheduler.getCurrentTime(), athlete, route.getDestination()));
    }

    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " exited route " + route.getId() + " at node " + route.getDestination().getId();
    }
    @Override
    public Connection getConnection() {
        return this.route;
    }
}
