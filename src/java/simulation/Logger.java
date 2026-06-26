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

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    final PrintStream printStream;
    final LogLevel setVerbosity;
    private final GeneratorMapek mapGenerator;

    public Logger(OutputStream stream, LogLevel verbosity, GeneratorMapek mapGenerator) {
        this.printStream = new PrintStream(stream);
        this.setVerbosity = verbosity;
        this.mapGenerator = mapGenerator;
    }

    public Logger(OutputStream stream, LogLevel verbosity) {
        this(stream, verbosity, null);
    }

    private void outputLog(String entry) {
        printStream.println(entry);
    }

    public void runtimeLog(Event event) {
        if (setVerbosity.allows(event.getLogLevel())) {
            outputLog(event.toString());
        }
    }

    private void addNodes(List<Node> nodes) {
        nodes.forEach(node -> mapGenerator.dodajWezel(node.getId(), node.getX(), node.getY(), node.isCommunicated() ?
                new StylWezla(GruboscKonturu.POGRUBIONY) : new StylWezla(GruboscKonturu.ZWYKLY)));
    }
    public boolean hasMapGenerator() {
        return mapGenerator != null;
    }

    public void snapshotMapParameters(SimulationEngine engine, String filename) throws WyjatekSystemuPlikow {
        if (mapGenerator == null) return;
        SkiResort resort = engine.resort;
        mapGenerator.zeruj();
        addNodes(resort.nodes());

        resort.connections().forEach(connection -> {
            List<String> lines = new ArrayList<>(2);
            boolean isLift = connection instanceof Lift;
            if (isLift) {
                Lift lift = (Lift) connection;
                lines.add(
                       "l" + lift.getId() + ": " +
                        "cap: " + lift.getCapacity() + " / " + lift.getDepartureSpread()+ "s"
                );
                lines.add(
                        "t = " + lift.getTravelTime() + "s"
                );
            } else {
                Route route = (Route) connection;
                lines.add(
                        "r" + route.getId() + ": " +
                                "diff: " + route.getDifficulty() + ", t: " +
                                route.getTravelTime() + "s"
                );
                lines.add(
                        "attr: " + route.getBaseRouteAttractiveness() + ", " + route.getResilience()

                );
            }

            mapGenerator.dodajKrawedz(connection.getSource().getId(), connection.getDestination().getId(), isLift ?
                    new StylKrawedzi(StylLinii.PRZERYWANA) : new StylKrawedzi(StylLinii.CIAGLA), lines);

        });

        mapGenerator.tworzMapke(filename);
    }

    public void snapshotMapState(SimulationEngine engine, String filename) throws WyjatekSystemuPlikow {
        if (mapGenerator == null) return;
        SkiResort resort = engine.resort;
        mapGenerator.zeruj();
        addNodes(resort.nodes());

        resort.connections().forEach(connection -> {
            List<String> lines = new ArrayList<>(2);
            boolean isLift = connection instanceof Lift;
            if (isLift) {
                Lift lift = (Lift) connection;
                lift.pushAvgUpdate(engine.getCurrentTime());
                lines.add(
                       "l" + lift.getId() + ": " +
                        "q: " + String.format("%.2f", lift.getAvgQSize()) + "(avg), " + lift.getMaxQ()+ "(max)"
                );
                lines.add(
                        "rides: " + lift.getUses() + " / " + (lift.getCapacity() * lift.getRideCount()) + " ("+(int)( (double) lift.getUses() * 100.0 / ((double)lift.getCapacity() * (double)lift.getRideCount()))+ "%)"
                );
            } else {
                Route route = (Route) connection;
                lines.add(
                        "r" + route.getId() + ": " +
                                "snow: " + String.format("%.2f", route.getWear())
                );
                lines.add(
                        "rides: " + route.getUses()

                );
            }

            mapGenerator.dodajKrawedz(connection.getSource().getId(), connection.getDestination().getId(), isLift ?
                    new StylKrawedzi(StylLinii.PRZERYWANA) : new StylKrawedzi(StylLinii.CIAGLA), lines);

        });



        mapGenerator.tworzMapke(filename);
    }





    public void snapshotLogState(SimulationEngine engine)
    {
        StringBuilder report = new StringBuilder();
        report.append("Engine state: ").append(engine.state.toString()).append(", at t = ").append(TimeHelper
            .formatTime(engine.getCurrentTime())).append("\n");
        String connectionsState = connectionsState(engine.resort.connections(), engine.getCurrentTime());
         if (!connectionsState.isEmpty()) {
        report.append("Connections: ").append("\n");
            report.append(connectionsState);

        }
        String athleteState = athleteState(engine.athletes, engine.getCurrentTime());
        if (!athleteState.isEmpty()) {
            report.append("Athletes: ").append("\n");
            report.append(athleteState);

        }
        outputLog(report.toString());
    }

    private String connectionsState(List<Connection> connections, int time) {
        StringBuilder sb = new StringBuilder();
        switch (setVerbosity) {
            case NONE:
                break;
            case INFO:
                connections.forEach(connection -> {
                    boolean isLift = connection instanceof Lift;
                    sb.append(isLift ? "Lift " : "Route ");
                    sb.append(connection.getId());
                    sb.append(", used").append(connection.getUses()).append("\n");
                });
                break;
            case DEBUG:
            case PRODUCTION:
                connections.forEach(connection -> {
                    boolean isLift = connection instanceof Lift;
                    sb.append(isLift ? "Lift " : "Route ");
                    sb.append(connection.getId());
                    sb.append(", uses = ").append(connection.getUses());
                    if (isLift) {
                        Lift lift = (Lift) connection;
                        lift.pushAvgUpdate(time);
                        sb.append(", maxQ = ").append(lift.getMaxQ()).append(String.format(", avgQ = %.2f, %%occup. =" +
                                " %.2f", lift.getAvgQSize(),
                                (double) lift.getUses() * 100.0 / ((double) lift.getRideCount() * (double) lift.getCapacity())));
                        sb.append("\n");
                    } else {
                        Route route = (Route) connection;
                        sb.append(String.format(", wear = %.4f", route.getWear()));
                        sb.append("\n");
                    }
                });
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
