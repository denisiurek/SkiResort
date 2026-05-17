package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

import java.util.Random;

public class AthleteRandomDecision implements AthleteDecisionPolicy{
    private final Random random;
    public AthleteRandomDecision(Random random) {
        this.random = random;
    }

    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        int totalConnections = node.getOutgoingLiftCount() + node.getOutgoingRouteCount();
        if (totalConnections == 0) return null; // Safe guard
        int randomIndex = random.nextInt(totalConnections);
        if (randomIndex < node.getOutgoingLiftCount()) {
            return node.getOutgoingLift(randomIndex);
        } else {
            return node.getOutgoingRoute(randomIndex - node.getOutgoingLiftCount());
        }
    }
}
