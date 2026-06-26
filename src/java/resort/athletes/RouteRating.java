package resort.athletes;

import resort.topology.Route;

public final class RouteRating {
    private static final int HARD_CUTOFF = 5;
    private static final double HARD_SCALE = 7.0;
    private static final double EASY_FLOOR = 0.2;

    private RouteRating() {}

    public static double rate(Athlete athlete, Route route) {
        return difficultyRating(athlete, route) * athlete.getWeightDifficulty()
                + route.getWear() * athlete.getWeightWear()
                + athlete.getAlphaZ() * (1 - athlete.getBoredom(route));
    }

    public static double difficultyRating(Athlete athlete, Route route) {
        if (route.getDifficulty() >= athlete.getSkill() + HARD_CUTOFF) {
            return 0;
        } else if (route.getDifficulty() >= athlete.getSkill()) {
            return 1 - (route.getDifficulty() - athlete.getSkill()) / (double) HARD_CUTOFF;
        } else {
            return Math.max(EASY_FLOOR, 1 - (athlete.getSkill() - route.getDifficulty()) / HARD_SCALE);
        }
    }
}
