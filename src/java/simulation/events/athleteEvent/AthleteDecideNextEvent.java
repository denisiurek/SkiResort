package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.*;
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
        if (chosenConnection == null) {
            throw new IllegalStateException("Athlete " + athlete.getId() + " has no available connection at node " + node.getId());
        }
        this.chosenConnection = chosenConnection;
        scheduler.runtimeLog(this);
        switch (chosenConnection) {
            case Lift lift -> scheduler.executeEvent(new AthleteEnterLiftQueueEvent(getTime(), athlete, lift));
            case Route route -> scheduler.executeEvent(new AthleteEnterRouteEvent(getTime(), athlete, route));
        }
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " decided to take connection " + chosenConnection.getId() + " at node " + node.getId();

    }
}
