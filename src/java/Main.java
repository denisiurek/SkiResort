import inputParser.ResortInputParser;
import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import simulation.*;

import java.io.File;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) throw new InputMismatchException("Invalid number of arguments: " + args.length +
                " (1)");

        SimulationConfig config = new SimulationConfig(
                TimeHelper.expandAbsoluteTime(9, 0, 0),
                TimeHelper.expandAbsoluteTime(15, 0, 0),
                TimeHelper.expandAbsoluteTime(16, 0, 0)
        );
        GeneratorMapek mapGenerator;
        File dir = new File(args[0]);
        try {
            mapGenerator = new GeneratorMapek(dir.getAbsolutePath());
        } catch (WyjatekSystemuPlikow e) {
            if (!dir.exists())
                throw new RuntimeException("No map directory: " + dir.getAbsolutePath() + e.getMessage());
            throw new RuntimeException(e);
        }
        Logger logger = new Logger(System.out, LogLevel.PRODUCTION, mapGenerator);
        SimulationEngine engine = new ResortInputParser(System.in).parse(logger, config);

        try {
            logger.snapshotMapParameters(engine, "topology.tex");
        } catch (WyjatekSystemuPlikow e) {
            System.err.println("Failed to generate topology map: " + e.getMessage());
        }

        engine.run();
        logger.snapshotLogState(engine);
        try {
        logger.snapshotMapState(engine, "final.tex");
                } catch (WyjatekSystemuPlikow e) {
            System.err.println("Failed to generate topology map: " + e.getMessage());
        }
    }
}


