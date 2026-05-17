package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Connection;
import resort.topology.Lift;
import resort.topology.Node;
import resort.topology.Route;
import simulation.LogLevel;
import simulation.Scheduler;
import simulation.events.NonSchedulableEvent;

public class AthleteDecideNextEvent extends AthleteEvent implements NonSchedulableEvent {
    private final Node node;
    private Connection chosenConnection;

    public AthleteDecideNextEvent(int time, Athlete athlete, Node node) {
        super(time, athlete, LogLevel.DEBUG);
        this.node = node;
    }

    @Override
    public void execute(Scheduler scheduler) {
        Connection chosenConnection = athlete.chooseNextConnection(node);
        this.chosenConnection = chosenConnection;
        scheduler.log(this);
        if (chosenConnection instanceof Lift) {
            scheduler.executeEvent(new AthleteEnterLiftQueueEvent(getTime(), athlete, (Lift) chosenConnection));
        } else scheduler.executeEvent(new AthleteEnterRouteEvent(getTime(), athlete, (Route) chosenConnection));
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " decided to take connection " + chosenConnection.getId() + " at node " + node.getId();

    }
}
