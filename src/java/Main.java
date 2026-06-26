import inputParser.ResortInputParser;
import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import simulation.*;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.err.println("The first argument should be an output directory for maps");
                return;
            }

            SimulationConfig config = new SimulationConfig(
                    TimeHelper.expandAbsoluteTime(9, 0, 0),
                    TimeHelper.expandAbsoluteTime(15, 0, 0),
                    TimeHelper.expandAbsoluteTime(16, 0, 0)
            );

            GeneratorMapek mapGenerator = new GeneratorMapek(args[0]);
            Logger logger = new Logger(System.out, LogLevel.PRODUCTION, mapGenerator);
            SimulationEngine engine = new ResortInputParser(System.in).parse(logger, config);

            logger.snapshotMapParameters(engine, "topology.tex");
            engine.run();
            logger.snapshotMapState(engine, "final.tex");
            logger.snapshotLogState(engine);
            logger.snapshotMapAthletes(engine, "History");

        } catch (WyjatekSystemuPlikow e) {
            System.err.println("Filesystem error. Ensure that you have proper permissions");
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println("Unexpected error. Report it to the developers");
            e.printStackTrace(System.err);
        }
    }
}
