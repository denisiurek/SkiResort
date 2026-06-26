package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AthleteRandomPolicy implements AthleteDecisionPolicy {
    private final Random random;

    public AthleteRandomPolicy(Random random) {
        this.random = random;
    }

    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        List<Connection> all = new ArrayList<>(node.getAllOutgoingLifts());
        all.addAll(node.getAllOutgoingRoutes());
        if (all.isEmpty()) throw new IllegalStateException("There exists a node " + node.getId() +  " without any " +
                "outgoing connections");
        return all.get(random.nextInt(all.size()));
    }
}
