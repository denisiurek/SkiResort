package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AthleteRandomDecision implements AthleteDecisionPolicy {
    private final Random random;

    public AthleteRandomDecision(Random random) {
        this.random = random;
    }

    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        List<Connection> all = new ArrayList<>(node.getAllOutgoingLifts());
        all.addAll(node.getAllOutgoingRoutes());
        if (all.isEmpty()) return null;
        return all.get(random.nextInt(all.size()));
    }
}
