import inputParser.ResortInputParser;
import simulation.LogLevel;
import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;
import timeUtils.TimeOperators;

public class Main {
    public static void main(String[] args) {
        SimulationConfig config = new SimulationConfig(
                TimeOperators.expandAbsoluteTime(9, 0, 0),
                TimeOperators.expandAbsoluteTime(15, 0, 0),
                TimeOperators.expandAbsoluteTime(16, 0, 0)
        );
        SimulationEngine engine = new ResortInputParser(System.in)
                .parse(new Logger(System.out, LogLevel.PRODUCTION), config);
        engine.run();
    }
}

