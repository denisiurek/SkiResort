import inputParser.ResortInputParser;
import simulation.*;

public class Main {
    public static void main(String[] args) {
        // Initial config for scheduling events checker while building simulation
        SimulationConfig config = new SimulationConfig(
                TimeHelper.expandAbsoluteTime(9, 0, 0),
                TimeHelper.expandAbsoluteTime(15, 0, 0),
                TimeHelper.expandAbsoluteTime(16, 0, 0)
        );
        SimulationEngine engine = new ResortInputParser(System.in)
                .parse(new Logger(System.out, LogLevel.PRODUCTION), config);
        // LogLevel.NONE -- no logs at all
        // LogLevel.INFO -- only summary
        // LogLevel.PRODUCTION -- expected amount of logs
        // Loglevel.DEBUG -- more detailed logs for debugging purposes
        // Made both the input and output stream configurable to allow for easier testing

        engine.run();
    }
    // Design decisions -- commentary:
    // Simulation builder is the interface between the parser and the simulation engine.
    // It looks nonsensical but to enable the parsing to be done in a different way - lifts first etc.
    // It needs to store those data and use them while building.
    // Parser itself just calls the builder as it reads sections.
    // Simulation engine implements scheduler, whose methods are called by events to schedule
    // new events. Therefore new events are only created by other events or during building.
    // This leaves lifts athletes and routes intentionally simple.
    // And events follow a nice flow.
    // There are certain events that are instanceof NonSchedulableEvent.
    // This is used to signify internal event which are used for logging purposes --> events that
    // happen at the same time instant. They are separated to make it logically clear
    // in terms of merging events that happen at the same time instant.

}

