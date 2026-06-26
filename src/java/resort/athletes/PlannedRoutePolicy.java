package resort.athletes;

import resort.topology.Connection;
import resort.topology.GraphSearch;
import resort.topology.Node;
import resort.topology.Route;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public abstract class PlannedRoutePolicy implements AthleteDecisionPolicy {
    protected final List<Route> allRoutes;
    private final Deque<Connection> plan = new ArrayDeque<>();
    private final Random random;
    private final AthleteDecisionPolicy randomPolicy;

    protected PlannedRoutePolicy(List<Route> allRoutes, Random random) {
        this.allRoutes = allRoutes;
        this.random = random;
        this.randomPolicy = new
                AthleteRandomPolicy(random);
    }

    @Override
    public final Connection chooseConnection(Athlete athlete, Node node) {
        if (plan.isEmpty()) {
            if (random.nextDouble() <= athlete.getSpontaneousness()) {
                return randomPolicy.chooseConnection(athlete, node);
            }
            Route target = selectTargetRoute(athlete, node);
            if (node != target.getSource()) {
                List<Connection> path = GraphSearch.bfsPath(node, target.getSource());
                if (path.isEmpty()) {
                    throw new IllegalStateException("No path from node " + node.getId()
                        + " to route " + target.getId() + " source node " + target.getSource().getId());
                }
                plan.addAll(path);
            }
            plan.add(target);
        }
        return plan.poll();
    }

    protected abstract Route selectTargetRoute(Athlete athlete, Node currentNode);
}
