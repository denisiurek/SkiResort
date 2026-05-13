package inputParser;

public class SimulationBuilder {
    public void addNode(int height, int x, int y, boolean communicated) {
        System.out.println("node: " + height + " " + x + " " + y + " " + communicated);
    }

    public void addLift(int start, int end, int groupTimeSpread, int maxGroupSize, int liftDuration) {
        System.out.println("lift: " + start + " " + end + " " + groupTimeSpread + " " + maxGroupSize + " " + liftDuration);
    }

    public void addRoute(int startNode, int endNode, int routeDifficulty, int routeDuration, double baseRouteAttractiveness, double routeResilience) {
        System.out.println(startNode + " " + endNode + " " + routeDifficulty + " " + routeDuration + " " + baseRouteAttractiveness + " " + routeResilience);
    }

    public void addAthlete(int skillLevel, double spontaneousness, boolean tracked, double levelMatch, double surfaceTolerance, int startNode, int startTime) {
    }
}
