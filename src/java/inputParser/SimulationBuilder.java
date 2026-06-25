package inputParser;

import resort.athletes.Athlete;
import resort.athletes.AthleteCombinedDecision;
import resort.athletes.AthleteDecisionPolicy;
import resort.topology.*;
import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;
import simulation.events.athleteEvent.AthleteArriveAtNodeEvent;
import simulation.events.liftEvent.LiftDepartureEvent;

import java.util.ArrayList;
import java.util.List;

public class SimulationBuilder {
    private final SimulationEngine engine;
    private final int startTime;
    // Hard defined simulation parameters, per task specification.
    private SkiResort resort;
    private List<Athlete> athletes;
    private List<Node> nodes;
    private List<Lift> lifts;
    private List<Route> routes;

    private final List<NodeDef> nodeDefs;
    private final List<LiftDef> liftDefs;
    private final List<RouteDef> routeDefs;
    private final List<AthleteDef> athleteDefs;

    SimulationBuilder(SimulationConfig config, Logger logger) {
        this.nodeDefs = new ArrayList<>();
        this.liftDefs = new ArrayList<>();
        this.routeDefs = new ArrayList<>();
        this.athleteDefs = new ArrayList<>();

        this.startTime = config.startTime();
        this.engine = new SimulationEngine(logger, config);
    }

    public void addNode(int height, int x, int y, boolean communicated) {
        nodeDefs.add( new NodeDef(height, x, y, communicated));
    }

    public void addLift(int startNode, int endNode, int groupTimeSpread, int maxGroupSize, int liftDuration) {

        liftDefs.add(new LiftDef(startNode, endNode, groupTimeSpread, maxGroupSize, liftDuration));
    }

    public void addRoute(int startNode, int endNode, int routeDifficulty, int routeDuration, double baseRouteAttractiveness, double routeResilience) {

        routeDefs.add( new RouteDef(startNode, endNode, routeDifficulty, routeDuration, baseRouteAttractiveness,
                routeResilience));
    }

    public void addAthlete(int skillLevel, double spontaneousness, boolean tracked, double levelMatch, double surfaceTolerance, int startNode, int startTime) {

        athleteDefs.add( new AthleteDef(skillLevel, spontaneousness, tracked, levelMatch, surfaceTolerance, startNode,
                startTime));
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

        liftDefs.forEach(liftDef -> {lifts.add(new Lift(lifts.size(), nodes.get(liftDef.startNode),
                nodes.get(liftDef.endNode),
                liftDef.liftDuration
                , liftDef.groupTimeSpread, liftDef.maxGroupSize));
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
        resort = new SkiResort(nodes, connections);

    }

    private void buildAthletes() {
        athletes = new ArrayList<>();
        AthleteDecisionPolicy decisionPolicy = new AthleteCombinedDecision(engine.getRandomGenerator());
        athleteDefs.forEach(athleteDef -> athletes.add(new Athlete(athletes.size(), athleteDef.skillLevel, athleteDef.spontaneousness,
                athleteDef.levelMatch,
                athleteDef.surfaceTolerance,
                athleteDef.tracked, decisionPolicy)));
    }

    private void scheduleInitialEvents() {
        for (Lift lift : lifts) {
            engine.scheduleEvent(new LiftDepartureEvent(startTime, lift));
        }

        for (int i = 0; i < athletes.size(); i++) { // ugly rework
            AthleteDef def = athleteDefs.get(i);
            engine.scheduleEvent(new AthleteArriveAtNodeEvent(def.startTime, athletes.get(i),
                    nodes.get(def.startNode)));
        }
    }


    private record NodeDef(int height, int x, int y, boolean communicated) {}

    private record LiftDef(int startNode, int endNode, int groupTimeSpread, int maxGroupSize, int liftDuration) {}

    private record RouteDef(int startNode, int endNode, int routeDifficulty, int routeDuration,
                            double baseRouteAttractiveness, double routeResilience) {}

    private record AthleteDef(int skillLevel, double spontaneousness, boolean tracked, double levelMatch,
                              double surfaceTolerance, int startNode, int startTime) {}
}
