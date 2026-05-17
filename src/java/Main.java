import inputParser.ResortInputParser;
import simulation.*;

public class Main {
    public static void main(String[] args) {
        SimulationConfig config = new SimulationConfig(
                TimeHelper.expandAbsoluteTime(9, 0, 0),
                TimeHelper.expandAbsoluteTime(15, 0, 0),
                TimeHelper.expandAbsoluteTime(16, 0, 0)
        );
        SimulationEngine engine = new ResortInputParser(System.in)
                .parse(new Logger(System.out, LogLevel.INFO), config);
        engine.run();
    }
}

