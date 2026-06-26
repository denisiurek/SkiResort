package simulation;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import resort.athletes.Athlete;
import resort.topology.*;
import simulation.events.Event;
import simulation.events.TravelEvent;
import simulation.events.athleteEvent.AthleteEvent;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Logger {
    private final PrintStream printStream;
    private final LogLevel setVerbosity;
    private final GeneratorMapek mapGenerator;

    private final Map<Athlete, List<Connection>> skiLogs;

    public Logger(OutputStream stream, LogLevel verbosity, GeneratorMapek mapGenerator) {
        this.printStream = new PrintStream(stream);
        this.setVerbosity = verbosity;
        this.mapGenerator = mapGenerator;
        this.skiLogs = new HashMap<>();
    }


    private void outputLog(String entry) {
        printStream.println(entry);
    }

    public void runtimeLog(Event event) {
        if (setVerbosity.allows(event.getLogLevel())) {
            outputLog(event.toString());
        }
        if (event instanceof AthleteEvent ae
          && event instanceof TravelEvent te
          && ae.getAthlete().isTracked()) {
            skiLogs.computeIfAbsent(ae.getAthlete(), athlete -> new ArrayList<>());
            skiLogs.get(ae.getAthlete()).add(te.getConnection());
        }
    }

    private void addNodes(List<Node> nodes) {
        nodes.forEach(node -> mapGenerator.dodajWezel(node.getId(), node.getX(), node.getY(), node.isCommunicated() ?
                new StylWezla(GruboscKonturu.POGRUBIONY) : new StylWezla(GruboscKonturu.ZWYKLY)));
    }

    /**
     * Generates a map of the parsed input parameters of the map
     * @param engine Simulation Engine
     * @param filename output file
     * @throws WyjatekSystemuPlikow .
     */
    public void snapshotMapParameters(SimulationEngine engine, String filename) throws WyjatekSystemuPlikow {
        snapshotMap(engine, filename,
            lift -> List.of(
                "l" + lift.getId() + ": cap: " + lift.getCapacity() + " / " + lift.getDepartureSpread() + "s",
                "t = " + lift.getTravelTime() + "s"),
            route -> List.of(
                "r" + route.getId() + ": diff: " + route.getDifficulty() + ", t: " + route.getTravelTime() + "s",
                "attr: " + String.format("%.2f", route.getBaseRouteAttractiveness()) + ", " + String.format("%.5f", route.getResilience())));
    }

    /**
     * Generates a map of the current state of the engine at the current time
     * @param engine Simulation Engine
     * @param filename output file
     * @throws WyjatekSystemuPlikow .
     */
    public void snapshotMapState(SimulationEngine engine, String filename) throws WyjatekSystemuPlikow {
        snapshotMap(engine, filename,
            lift -> {
                double avgQ = lift.getAvgQSize(engine.getCurrentTime());
                int maxQ = lift.getMaxQ();
                int uses = lift.getUses();
                int rideCount = lift.getRideCount();
                int capacity = lift.getCapacity();
                double occupancy = 0.0;
                if (rideCount > 0 && capacity > 0) {
                    occupancy = (double) uses * 100.0 / ((double) rideCount * (double) capacity);
                }
                return List.of(
                    "l" + lift.getId() + ": q: " + String.format("%.2f", avgQ) + "(avg), " + maxQ + "(max)",
                    "rides: " + uses + " / " + (capacity * rideCount)
                        + " (" + String.format("%.1f%%", occupancy) + ")");
            },
            route -> List.of(
                "r" + route.getId() + ": snow: " + String.format("%.2f", route.getWear()),
                "rides: " + route.getUses()));
    }

    /**
     * Generates a map of the current state of the athletes (what they rode through since start to this point)
     * @param engine Simulation Engine
     * @param baseFilename output file
     * @throws WyjatekSystemuPlikow
     */
    public void snapshotMapAthletes(SimulationEngine engine, String baseFilename) throws WyjatekSystemuPlikow {
        if (mapGenerator == null) return;

        for (var entry : skiLogs.entrySet()) {
            Athlete athlete = entry.getKey();
            List<Connection> rides = entry.getValue();

            Map<Connection, List<Integer>> rideMap = IntStream.range(0, rides.size())
                .boxed()
                .collect(Collectors.groupingBy(rides::get, LinkedHashMap::new,
                    Collectors.mapping(i -> i + 1, Collectors.toList())));

            mapGenerator.zeruj();
            addNodes(engine.getResort().nodes());

            for (Connection connection : engine.getResort().connections()) {
                boolean isLift = connection instanceof Lift;
                StylKrawedzi styl = new StylKrawedzi(isLift ? StylLinii.PRZERYWANA : StylLinii.CIAGLA);
                int src = connection.getSource().getId();
                int dst = connection.getDestination().getId();

                List<Integer> nums = rideMap.get(connection);
                if (nums != null) {
                    String prefix = (isLift ? "l" : "r") + connection.getId();
                    String label = prefix + "(" + nums.size() + "): "
                        + nums.stream().map(Object::toString).collect(Collectors.joining(","));
                    mapGenerator.dodajKrawedz(src, dst, styl, label);
                } else {
                    mapGenerator.dodajKrawedz(src, dst, styl, List.of());
                }
            }

            mapGenerator.tworzMapke(baseFilename + "-athlete-" + athlete.getId() + ".tex");
        }
    }

    /**
     * Internal function to remove repeated code
     */
    private void snapshotMap(SimulationEngine engine, String filename,
                             Function<Lift, List<String>> liftLabels,
                             Function<Route, List<String>> routeLabels) throws WyjatekSystemuPlikow {
        if (mapGenerator == null) return;
        mapGenerator.zeruj();
        addNodes(engine.getResort().nodes());

        engine.getResort().connections().forEach(connection -> {
            boolean isLift = connection instanceof Lift;
            List<String> lines = isLift
                ? liftLabels.apply((Lift) connection)
                : routeLabels.apply((Route) connection);
            mapGenerator.dodajKrawedz(connection.getSource().getId(), connection.getDestination().getId(),
                new StylKrawedzi(isLift ? StylLinii.PRZERYWANA : StylLinii.CIAGLA), lines);
        });

        mapGenerator.tworzMapke(filename);
    }


    /**
     * Logs to defined stream the current statistics of the engine.
     * @param engine Simulation Engine
     */
    public void snapshotLogState(SimulationEngine engine)
    {
        StringBuilder report = new StringBuilder();
        report.append("Engine state: ").append(engine.getState().toString()).append(", at t = ").append(TimeHelper
            .formatTime(engine.getCurrentTime())).append("\n");
        String connectionsState = connectionsState(engine.getResort().connections(), engine.getCurrentTime());
         if (!connectionsState.isEmpty()) {
        report.append("Connections: ").append("\n");
            report.append(connectionsState);

        }
        String athleteState = athleteState(engine.getAthletes(), engine.getCurrentTime());
        if (!athleteState.isEmpty()) {
            report.append("Athletes: ").append("\n");
            report.append(athleteState);

        }
        outputLog(report.toString());
    }

    private String connectionsState(List<Connection> connections, int time) {
        StringBuilder sb = new StringBuilder();
        switch (setVerbosity) {
            // Here lived my other output formats which were incredibly messy so i cleaned them.
            case NONE:
                break;
            case REDUCED:
                break;
            case PRODUCTION:
                connections.forEach(connection -> {
                    boolean isLift = connection instanceof Lift;
                    sb.append(isLift ? "Lift " : "Route ");
                    sb.append(connection.getId());
                    sb.append(": uses: ").append(connection.getUses());
                    if (isLift) {
                        Lift lift = (Lift) connection;
                        int maxQ = lift.getMaxQ();
                        double avgQ = lift.getAvgQSize(time);
                        int uses = lift.getUses();
                        int rideCount = lift.getRideCount();
                        int capacity = lift.getCapacity();
                        double occupancy = 0.0;
                        if (rideCount > 0 && capacity > 0) {
                            occupancy = (double) uses * 100.0 / ((double) rideCount * (double) capacity);
                        }
                        sb.append(String.format(", Q %d(max), %.2f(avg), %d / %d (%s)\n", maxQ, avgQ, uses,
                                rideCount * capacity,
                                String.format("%.1f%%", occupancy)));
                    } else {
                        Route route = (Route) connection;
                        sb.append(String.format(", snow = %.2f", route.getWear()));
                        sb.append("\n");
                    }
                });
                case DEBUG:
                    break;
        }
            return sb.toString();
        }
        private String athleteState(List<Athlete> athletes, int time) {
            return "";
        }


    public void runtimeLog(String entry, LogLevel logLevel) {
        if (setVerbosity.allows(logLevel)) {
            outputLog(entry);
        }
    }

}
