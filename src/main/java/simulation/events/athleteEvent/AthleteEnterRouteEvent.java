package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Route;
import simulation.LogLevel;
import simulation.Scheduler;

public class AthleteEnterRouteEvent extends AthleteEvent  {
    private final Route route;
    public AthleteEnterRouteEvent(int time, Athlete athlete, Route route) {
        super(time, athlete, athlete.isTracked() ? LogLevel.PRODUCTION : LogLevel.DEBUG);
        this.route = route;
    }

    @Override
    public void execute(Scheduler scheduler) {
        route.registerEntry();
        scheduler.log(this);
        scheduler.scheduleEvent(new AthleteExitRouteEvent(getTime() + route.getTravelTime(), athlete, route));
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " entered route " + route.getId() + " from " + route.getSource().getId() + " to " + route.getDestination().getId();
    }
}
