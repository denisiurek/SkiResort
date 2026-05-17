package resort.athletes;

import resort.topology.Connection;
import resort.topology.Lift;
import resort.topology.Node;
import resort.topology.Route;

public class AthletePreferenceDecision implements AthleteDecisionPolicy {
    public AthletePreferenceDecision() {
    }
    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        Route[] routes = node.getAllOutgoingRoutes();
        Lift[] lifts = node.getAllOutgoingLifts();
        Lift neededLiftToTake = null;
        Route chosenRoute = null;
        double currentBestRouteRating = Double.NEGATIVE_INFINITY;
        for (Route route : routes) {
            if (rateRoute(athlete, route) > currentBestRouteRating) {
                currentBestRouteRating = rateRoute(athlete, route);
                chosenRoute = route;
            }
        }
        for (Lift lift : lifts) {
            for (Route route : lift.getDestination().getAllOutgoingRoutes()) {
                if (rateRoute(athlete, route) > currentBestRouteRating) {
                    currentBestRouteRating = rateRoute(athlete, route);
                    neededLiftToTake = lift;
                    chosenRoute = route;
                }
            }
        }
        if (neededLiftToTake != null) {
            return neededLiftToTake;
        }
        return chosenRoute;
    }
        private double rateRoute(Athlete athlete, Route route) {
        double difficultyRating = getDifficultyRating(athlete, route);
        double wearRating = route.getWear();
        return difficultyRating * athlete.getWeightDifficulty() + wearRating * athlete.getWeightWear();
    }

    private double getDifficultyRating(Athlete athlete, Route route) {
        if (route.getDifficulty() >= athlete.getSkill() + 5) {
            return 0;
        } else if (athlete.getSkill() + 5 > route.getDifficulty() && route.getDifficulty() >= athlete.getSkill()) {
            return 1 - (route.getDifficulty() - athlete.getSkill()) / 5.0;
        } else {
            return Math.max(0.2, 1 - (athlete.getSkill() - route.getDifficulty()) / 7.0);
        }
    }
}
