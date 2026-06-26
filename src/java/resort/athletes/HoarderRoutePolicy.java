package resort.athletes;

import resort.topology.GraphSearch;
import resort.topology.Node;
import resort.topology.Route;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HoarderRoutePolicy extends PlannedRoutePolicy {
    private final Map<Route, Integer> visitCounts;

    public HoarderRoutePolicy(List<Route> allRoutes, Random random) {
        super(allRoutes, random);
        this.visitCounts = allRoutes.stream().collect(Collectors.toMap(Function.identity(), route -> 0));
    }

    @Override
    protected Route selectTargetRoute(Athlete athlete, Node currentNode) {
        Map<Node, Integer> distances = GraphSearch.bfsDistances(currentNode);
        // first visitcounts (min), if not decided - by distance, if not decided by rating
        return allRoutes.stream()
            .min(Comparator.comparingInt((Route r) -> visitCounts.get(r))
                .thenComparingInt(r -> distances.getOrDefault(r.getSource(), Integer.MAX_VALUE))
                .thenComparing(Comparator.comparingDouble(
                    (Route r) -> RouteRating.rate(athlete, r)).reversed()))
            .orElseThrow();
    }

    @Override
    public void onRouteEntry(Athlete athlete, Route route) {
        visitCounts.merge(route, 1, Integer::sum);
    }
}
