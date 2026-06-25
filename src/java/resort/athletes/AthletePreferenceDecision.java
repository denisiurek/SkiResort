package resort.athletes;

import resort.topology.Connection;
import resort.topology.Lift;
import resort.topology.Node;
import resort.topology.Route;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class AthletePreferenceDecision implements AthleteDecisionPolicy {

    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        List<Route> directRoutes = node.getAllOutgoingRoutes();
        List<Lift> lifts = node.getAllOutgoingLifts();

        Optional<Route> bestDirect = directRoutes.stream()
                .max(Comparator.comparingDouble(r -> rateRoute(athlete, r)));

        record LiftRoute(Lift lift, Route route) {}

        Optional<LiftRoute> bestViaLift = lifts.stream()
                .flatMap(lift -> lift.getDestination().getAllOutgoingRoutes().stream()
                        .map(route -> new LiftRoute(lift, route)))
                .max(Comparator.comparingDouble(lr -> rateRoute(athlete, lr.route())));

        double directRating = bestDirect.map(r -> rateRoute(athlete, r)).orElse(Double.NEGATIVE_INFINITY);
        double liftRating = bestViaLift.map(lr -> rateRoute(athlete, lr.route())).orElse(Double.NEGATIVE_INFINITY);

        if (liftRating > directRating) {
            return bestViaLift.get().lift();
        }
        return bestDirect.orElse(null);
    }

    private double rateRoute(Athlete athlete, Route route) {
        double difficultyRating = getDifficultyRating(athlete, route);
        double wearRating = route.getWear();
        return difficultyRating * athlete.getWeightDifficulty() + wearRating * athlete.getWeightWear();
    }

    private double getDifficultyRating(Athlete athlete, Route route) {
        if (route.getDifficulty() >= athlete.getSkill() + 5) {
            return 0;
        } else if (route.getDifficulty() >= athlete.getSkill()) {
            return 1 - (route.getDifficulty() - athlete.getSkill()) / 5.0;
        } else {
            return Math.max(0.2, 1 - (athlete.getSkill() - route.getDifficulty()) / 7.0);
        }
    }
}
