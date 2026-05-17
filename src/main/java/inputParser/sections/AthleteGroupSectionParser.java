package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static timeHelper.TimeHelper.expandAbsoluteTime;

public class AthleteGroupSectionParser implements SectionParser {
    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        int i = 0;
        while (i < entries.length) {
            try {
                AthleteGroupLine1 characteristics = parseGeneralCharacteristics(entries[i++]);
                AthleteGroupLine2 weights = parseWeights(entries[i++]);
                AthleteGroupLine3 parameters = parseStartParameters(entries[i++]);

                int startTime = parameters.startTime;
                for (int j = 0; j < characteristics.groupSize; j++) {
                    addAthlete(builder, characteristics, weights, parameters, startTime);
                    startTime += parameters.timeSpread;
                }
            } catch (NoSuchElementException e) {
                throw new IncorrectFormattingException("Incorrect formatting in athlete group section", i);
            }

        }
    }

    private void addAthlete(
            SimulationBuilder builder,
            AthleteGroupLine1 characteristics,
            AthleteGroupLine2 weights,
            AthleteGroupLine3 parameters,
            int startTime
    ) {
        builder.addAthlete(
                characteristics.skillLevel,
                characteristics.spontaneousness,
                characteristics.tracked,
                weights.levelMatch,
                weights.surfaceTolerance,
                parameters.startNode,
                startTime
        );
    }

    private AthleteGroupLine1 parseGeneralCharacteristics(String entry) {
        Scanner scanner = new Scanner(entry);

        int groupSize = scanner.nextInt();

        int skillLevel = scanner.nextInt();

        double spontaneousness = scanner.nextDouble();

        boolean tracked = scanner.hasNext("s");

        return new AthleteGroupLine1(groupSize, skillLevel, spontaneousness, tracked);

    }

    private AthleteGroupLine2 parseWeights(String entry) {
        Scanner scanner = new Scanner(entry);

        double levelMatch = scanner.nextDouble();

        double surfaceTolerance = scanner.nextDouble();

        return new AthleteGroupLine2(levelMatch, surfaceTolerance);
    }

    private AthleteGroupLine3 parseStartParameters(String entry) {
        Scanner scanner = new Scanner(entry);
        scanner.useDelimiter("[:\\s]+");
        int startNode = scanner.nextInt();
        int hh = scanner.nextInt();

        scanner.skip(":");

        int mm = scanner.nextInt();

        scanner.skip(":");

        int ss = scanner.nextInt();

        int time = expandAbsoluteTime(hh, mm, ss);
        scanner.useDelimiter(" ");
        int spread = 0;
        if (scanner.hasNextInt()) spread = scanner.nextInt();

        return new AthleteGroupLine3(startNode, time, spread);
    }

    private record AthleteGroupLine1(
            int groupSize,
            int skillLevel,
            double spontaneousness,
            boolean tracked

    ) {
    }

    private record AthleteGroupLine2(
            double levelMatch,
            double surfaceTolerance
    ) {
    }

    private record AthleteGroupLine3(
            int startNode,
            int startTime,
            int timeSpread
    ) {
    }

}
