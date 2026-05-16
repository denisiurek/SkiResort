package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.athletes.AthleteDecision;
import resort.topology.Connection;
import resort.topology.Lift;
import resort.topology.Route;
import resort.topology.Node;
import simulation.LogLevel;
import simulation.Scheduler;

public class AthleteDecideNextEvent extends AthleteEvent {
    private final Node node;
    private Connection chosenConnection;
    public AthleteDecideNextEvent(int time, Athlete athlete, Node node) {
        super(time, athlete, LogLevel.DEBUG);
        this.node = node;
    }

    @Override
    public void execute(Scheduler scheduler) {
        Connection chosenConnection = new AthleteDecision().chooseConnection(athlete, node);
        if (chosenConnection instanceof Lift) {
            scheduler.scheduleEvent(new AthleteEnterLiftQueueEvent(getTime(), athlete, (Lift) chosenConnection));
        } else scheduler.scheduleEvent(new AthleteEnterRouteEvent(getTime(), athlete, (Route) chosenConnection));
        this.chosenConnection = chosenConnection;
        scheduler.log(this);

    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " decided to take connection " + chosenConnection.getId() + " at node " + node.getId();

    }
}
