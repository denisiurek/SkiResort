package inputParser;

import resort.athletes.Athlete;
import resort.athletes.AthleteDecision;
import resort.topology.SkiResort;
import resort.topology.Connection;
import resort.topology.Lift;
import resort.topology.Node;
import resort.topology.Route;
import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;
import simulation.events.athleteEvent.AthleteArriveAtNodeEvent;
import simulation.events.liftEvent.LiftDepartureEvent;

public class SimulationBuilder {
    // Hard defined simulation parameters, per task specification.
    private SkiResort resort;
    private Athlete[] athletes;
    private Node[] nodes;
    private Lift[] lifts;
    private Route[] routes;
    private int athleteCount;
    private int nodeCount;
    private int liftCount;
    private int routeCount;
    private static final int INITIAL_ARRAY_SIZES = 10;
    private final SimulationEngine engine;
    private final int startTime;
    private NodeDef[] nodeDefs;
    private LiftDef[] liftDefs;
    private RouteDef[] routeDefs;
    private AthleteDef[] athleteDefs;

    SimulationBuilder(SimulationConfig config, Logger logger) {
        this.nodeDefs = new NodeDef[INITIAL_ARRAY_SIZES];
        this.liftDefs = new LiftDef[INITIAL_ARRAY_SIZES];
        this.routeDefs = new RouteDef[INITIAL_ARRAY_SIZES];
        this.athleteDefs = new AthleteDef[INITIAL_ARRAY_SIZES];
        this.athleteCount = 0;
        this.nodeCount = 0;
        this.liftCount = 0;
        this.routeCount = 0;
        this.startTime = config.getStartTime();
        this.engine = new SimulationEngine(logger, config);
    }

    public void addNode(int height, int x, int y, boolean communicated) {
        ensureNodeSize(1);
        nodeDefs[nodeCount++] = new NodeDef(height, x, y, communicated);
    }

    public void addLift(int startNode, int endNode, int groupTimeSpread, int maxGroupSize, int liftDuration) {
        ensureLiftSize(1);
        liftDefs[liftCount++] = new LiftDef(startNode, endNode, groupTimeSpread, maxGroupSize, liftDuration);
    }

    public void addRoute(int startNode, int endNode, int routeDifficulty, int routeDuration, double baseRouteAttractiveness, double routeResilience) {
        ensureRouteSize(1);
        routeDefs[routeCount++] = new RouteDef(startNode, endNode, routeDifficulty, routeDuration, baseRouteAttractiveness, routeResilience);
    }

    public void addAthlete(int skillLevel, double spontaneousness, boolean tracked, double levelMatch, double surfaceTolerance, int startNode, int startTime) {
        ensureAthleteSize(1);
        athleteDefs[athleteCount++] = new AthleteDef(skillLevel, spontaneousness, tracked, levelMatch, surfaceTolerance, startNode, startTime);
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
        nodes = new Node[nodeCount];
        lifts = new Lift[liftCount];
        routes = new Route[routeCount];

        for (int i = 0; i < nodeCount; i++) {
            NodeDef def = nodeDefs[i];
            nodes[i] = new Node(i, def.height, def.x, def.y, def.communicated);
        }

        for (int i = 0; i < liftCount; i++) {
            LiftDef def = liftDefs[i];
            lifts[i] = new Lift(i, nodes[def.startNode], nodes[def.endNode], def.liftDuration, def.groupTimeSpread, def.maxGroupSize);
            nodes[def.startNode].addOutgoingLift(lifts[i]);
        }

        for (int i = 0; i < routeCount; i++) {
            RouteDef def = routeDefs[i];
            routes[i] = new Route(i, nodes[def.startNode], nodes[def.endNode], def.routeDuration, def.routeDifficulty, def.routeResilience, def.baseRouteAttractiveness);
            nodes[def.startNode].addOutgoingRoute(routes[i]);
        }

        Connection[] connections = new Connection[liftCount + routeCount];
        System.arraycopy(lifts, 0, connections, 0, liftCount);
        System.arraycopy(routes, 0, connections, liftCount, routeCount);
        resort = new SkiResort(nodes, connections);
    }

    private void buildAthletes() {
        athletes = new Athlete[athleteCount];
        resort.athletes.AthleteDecisionPolicy decisionPolicy = new AthleteDecision(engine.getRandomGenerator());
        for (int i = 0; i < athleteCount; i++) {
            AthleteDef def = athleteDefs[i];
            athletes[i] = new Athlete(i, def.skillLevel, def.spontaneousness, def.levelMatch, def.surfaceTolerance, def.tracked, decisionPolicy);
        }
    }

    private void scheduleInitialEvents() {
        for (int i = 0; i < liftCount; i++) {
            engine.scheduleEvent(new LiftDepartureEvent(startTime, lifts[i]));
        }

        for (int i = 0; i < athleteCount; i++) {
            AthleteDef def = athleteDefs[i];
            engine.scheduleEvent(new AthleteArriveAtNodeEvent(def.startTime, athletes[i], nodes[def.startNode]));
        }
    }

    private void ensureAthleteSize(int desiredAdditionalSize) {
        if (athleteCount + desiredAdditionalSize >= athleteDefs.length) {
            AthleteDef[] newAthletes = new AthleteDef[(athleteCount + desiredAdditionalSize) * 2];
            System.arraycopy(athleteDefs, 0, newAthletes, 0, athleteCount);
            athleteDefs = newAthletes;
        }
    }
    private void ensureNodeSize(int desiredAdditionalSize) {
        if (nodeCount + desiredAdditionalSize >= nodeDefs.length) {
            NodeDef[] newNodes = new NodeDef[(nodeCount + desiredAdditionalSize) * 2];
            System.arraycopy(nodeDefs, 0, newNodes, 0, nodeCount);
            nodeDefs = newNodes;
        }
    }
    private void ensureLiftSize(int desiredAdditionalSize) {
        if (liftCount + desiredAdditionalSize >= liftDefs.length) {
            LiftDef[] newLifts = new LiftDef[(liftCount + desiredAdditionalSize) * 2];
            System.arraycopy(liftDefs, 0, newLifts, 0, liftCount);
            liftDefs = newLifts;
        }
    }
    private void ensureRouteSize(int desiredAdditionalSize) {
        if (routeCount + desiredAdditionalSize >= routeDefs.length) {
            RouteDef[] newRoutes = new RouteDef[(routeCount + desiredAdditionalSize) * 2];
            System.arraycopy(routeDefs, 0, newRoutes, 0, routeCount);
            routeDefs = newRoutes;
        }
    }

    private record NodeDef(int height, int x, int y, boolean communicated) {}
    private record LiftDef(int startNode, int endNode, int groupTimeSpread, int maxGroupSize, int liftDuration) {}
    private record RouteDef(int startNode, int endNode, int routeDifficulty, int routeDuration, double baseRouteAttractiveness, double routeResilience) {}
    private record AthleteDef(int skillLevel, double spontaneousness, boolean tracked, double levelMatch, double surfaceTolerance, int startNode, int startTime) {}
}
