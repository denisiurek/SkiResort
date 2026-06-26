package resort.athletes;

import resort.topology.Node;
import resort.topology.Route;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GreedyRoutePolicy extends PlannedRoutePolicy {

    public GreedyRoutePolicy(List<Route> allRoutes, Random random) {
        super(allRoutes, random);
    }

    @Override
    protected Route selectTargetRoute(Athlete athlete, Node currentNode) {
        return allRoutes.stream()
            .max(Comparator.comparingDouble(r -> RouteRating.rate(athlete, r)))
            .orElseThrow();
    }
}
