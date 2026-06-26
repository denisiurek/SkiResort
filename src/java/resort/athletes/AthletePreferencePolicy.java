package resort.athletes;

import resort.topology.Connection;
import resort.topology.Lift;
import resort.topology.Node;
import resort.topology.Route;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AthletePreferencePolicy implements AthleteDecisionPolicy {
    private final Random random;
    private final AthleteDecisionPolicy randomPolicy;

    public AthletePreferencePolicy(Random random) {
        this.random = random;
        this.randomPolicy = new AthleteRandomPolicy(random);
    }

    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        if (random.nextDouble() <= athlete.getSpontaneousness()) {
            return randomPolicy.chooseConnection(athlete, node);
        }
        List<Route> directRoutes = node.getAllOutgoingRoutes();
        List<Lift> lifts = node.getAllOutgoingLifts();

        Optional<Route> bestDirect = directRoutes.stream()
                .max(Comparator.comparingDouble(r -> RouteRating.rate(athlete, r)));

        record LiftRoute(Lift lift, Route route) {}

        Optional<LiftRoute> bestViaLift = lifts.stream()
                .flatMap(lift -> lift.getDestination().getAllOutgoingRoutes().stream()
                        .map(route -> new LiftRoute(lift, route)))
                .max(Comparator.comparingDouble(lr -> RouteRating.rate(athlete, lr.route())));

        double directRating = bestDirect.map(r -> RouteRating.rate(athlete, r)).orElse(Double.NEGATIVE_INFINITY);

        double liftRating = bestViaLift.map(lr -> RouteRating.rate(athlete, lr.route())).orElse(Double.NEGATIVE_INFINITY);

        if (liftRating > directRating) {
            return bestViaLift.get().lift();
        }
        return bestDirect.orElseThrow(() -> new RuntimeException("There exists a node without any outgoing " +
                "connections"));
    }
}
