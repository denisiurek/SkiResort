package simulation.events.athleteEvent;

import resort.athletes.Athlete;
import resort.topology.Node;
import simulation.LogLevel;
import simulation.Scheduler;

public class AthleteArriveAtNodeEvent extends AthleteEvent {
    private final Node node;

    public AthleteArriveAtNodeEvent(int time, Athlete athlete, Node node) {
        super(time, athlete, LogLevel.DEBUG);
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public void execute(Scheduler scheduler) {
        node.registerVisit();
        scheduler.log(this);
        scheduler.executeEvent(new AthleteDecideNextEvent(scheduler.getCurrentTime(), athlete, node));
    }

    @Override
    public boolean isFinishable() {
        return true;
    }

    @Override
    protected String getEventDescription() {
        return "Athlete " + athlete.getId() + " arrived at node " + node.getId();
    }
}
