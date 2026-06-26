package inputParser;

import resort.athletes.*;
import resort.topology.*;
import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;
import simulation.events.athleteEvent.AthleteArriveAtNodeEvent;
import simulation.events.liftEvent.LiftDepartureEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class SimulationBuilder {
    private final SimulationEngine engine;
    private SkiResort resort;
    private List<Athlete> athletes;
    private List<Node> nodes;
    private List<Lift> lifts;
    private List<Route> routes;
    private final SimulationConfig config;
    private final List<NodeDef> nodeDefs;
    private final List<LiftDef> liftDefs;
    private final List<RouteDef> routeDefs;
    private final List<AthleteDef> athleteDefs;

    SimulationBuilder(SimulationConfig config, Logger logger) {
        this.nodeDefs = new ArrayList<>();
        this.liftDefs = new ArrayList<>();
        this.routeDefs = new ArrayList<>();
        this.athleteDefs = new ArrayList<>();
        this.config = config;
        this.engine = new SimulationEngine(logger, config);
    }

    public void addNode(int height, int x, int y, boolean communicated) {
        nodeDefs.add(new NodeDef(height, x, y, communicated));
    }

    public void addLift(int startNode, int endNode, int groupTimeSpread, int maxGroupSize, int liftDuration) {

        liftDefs.add(new LiftDef(startNode, endNode, groupTimeSpread, maxGroupSize, liftDuration));
    }

    public void addRoute(int startNode, int endNode, int routeDifficulty, int routeDuration, double baseRouteAttractiveness, double routeResilience) {

        routeDefs.add(new RouteDef(startNode, endNode, routeDifficulty, routeDuration, baseRouteAttractiveness,
                routeResilience));
    }

    public void addAthleteGroup(int groupSize, int skillLevel, double spontaneousness,
                                double beta, String type, boolean tracked,
                                double levelMatch, double surfaceTolerance, double alphaZ,
                                int startNode, int startTime, int spread) {
        for (int j = 0; j < groupSize; j++)
            athleteDefs.add(new AthleteDef(skillLevel, spontaneousness, beta, type, tracked,
                    levelMatch, surfaceTolerance, alphaZ, startNode, startTime + j * spread));
    }

    public SimulationEngine build() {
        buildResort();
        buildAthletes();
        scheduleInitialEvents();

        engine.setResort(resort);
        engine.setAthletes(athletes);
        return engine;
    }

    private void buildResort() {
        nodes = new ArrayList<>();
        lifts = new ArrayList<>();
        routes = new ArrayList<>();
        nodeDefs.forEach(nodeDef -> nodes.add(new Node(nodes.size(), nodeDef.height, nodeDef.x, nodeDef.y,
                nodeDef.communicated)));

        liftDefs.forEach(liftDef -> {
            lifts.add(new Lift(lifts.size(), nodes.get(liftDef.startNode),
                    nodes.get(liftDef.endNode),
                    liftDef.liftDuration
                    , liftDef.groupTimeSpread, liftDef.maxGroupSize, config.softStopTime()));
            nodes.get(liftDef.startNode).addOutgoingLift(lifts.getLast());
        });

        routeDefs.forEach(routeDef -> {
            routes.add(new Route(routes.size(), nodes.get(routeDef.startNode), nodes.get(routeDef.endNode),
                    routeDef.routeDuration, routeDef.routeDifficulty, routeDef.routeResilience,
                    routeDef.baseRouteAttractiveness));
            nodes.get(routeDef.startNode).addOutgoingRoute(routes.getLast());

        });

        List<Connection> connections = new ArrayList<>(lifts);
        connections.addAll(routes);
        resort = new SkiResort(nodes, connections, Collections.unmodifiableList(routes));

    }

    private void buildAthletes() {
        athletes = new ArrayList<>();
        int routeCount = routes.size();
        List<Route> unmodifiableRoutes = resort.routes();
        java.util.Random random = engine.getRandomGenerator();
        AthleteDecisionPolicy localPolicy = new AthletePreferencePolicy(random);
        athleteDefs.forEach(def -> {
            AthleteDecisionPolicy policy = switch (def.type) {
                case "L" -> localPolicy;
                case "Z" -> new GreedyRoutePolicy(unmodifiableRoutes, random);
                case "K" -> new HoarderRoutePolicy(unmodifiableRoutes, random);
                default -> throw new IllegalArgumentException("Unknown athlete type: " + def.type);
            };
            athletes.add(new Athlete(athletes.size(), def.skillLevel, def.spontaneousness,
                    def.levelMatch, def.surfaceTolerance, def.beta, def.alphaZ, routeCount,
                    def.tracked, policy));
        });
    }

    private void scheduleInitialEvents() {
        lifts.forEach(lift -> engine.scheduleEvent(new LiftDepartureEvent(config.startTime(), lift)));

        IntStream.range(0, athletes.size()).forEach(i -> {
            AthleteDef def = athleteDefs.get(i);
            engine.scheduleEvent(new AthleteArriveAtNodeEvent(def.startTime, athletes.get(i), nodes.get(def.startNode)));
        });
    }

    private record NodeDef(int height, int x, int y, boolean communicated) {}

    private record LiftDef(int startNode, int endNode, int groupTimeSpread, int maxGroupSize, int liftDuration) {}

    private record RouteDef(int startNode, int endNode, int routeDifficulty, int routeDuration,
                            double baseRouteAttractiveness, double routeResilience) {}

    private record AthleteDef(int skillLevel, double spontaneousness, double beta, String type, boolean tracked,
                              double levelMatch, double surfaceTolerance, double alphaZ,
                              int startNode, int startTime) {}
}
