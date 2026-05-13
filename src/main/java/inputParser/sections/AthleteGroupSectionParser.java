package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.Scanner;

public class AthleteGroupSectionParser implements SectionParser{
    private record AthleteGroupGeneralCharacteristics(
            int groupSize,
            int skillLevel,
            double spontaneousness,
            boolean tracked

    ){}

    private record AthleteGroupWeights(
            double levelMatch,
            double surfaceTolerance
    ){}

    private record AthleteGroupStartParameters(
            int startNode,
            int startTime,
            int timeSpread
    ){}
    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        for (int i = 0; i < entries.length; i+=3){
            Scanner scanner = new Scanner(entries[i]);
            AthleteGroupGeneralCharacteristics characteristics = parseGeneralCharacteristics(entries[i], i);
            AthleteGroupWeights weights = parseWeights(entries[i+1], i+1);
            AthleteGroupStartParameters parameters = parseStartParameters(entries[i+2], i+2);
            expandGroup(characteristics, weights, parameters, builder);
        }
    }
    private AthleteGroupGeneralCharacteristics parseGeneralCharacteristics(String entry, int lineNumber) throws IncorrectFormattingException {
        Scanner scanner = new Scanner(entry);

        if (!scanner.hasNextInt()) {
            throw new IncorrectFormattingException("Error parsing groupSize for athlete group", lineNumber);
        }
        int groupSize = scanner.nextInt();

        if (!scanner.hasNextInt()) {
            throw new IncorrectFormattingException("Error parsing skillLevel for athlete group", lineNumber);
        }
        int skillLevel = scanner.nextInt();

        if (!scanner.hasNextDouble()) {
            throw new IncorrectFormattingException("Error parsing spontaneousness for athlete group", lineNumber);
        }
        double spontaneousness = scanner.nextDouble();

        boolean tracked;
        if (scanner.hasNext("s")){
            tracked = true;
        } else if (!scanner.hasNext()) {
            tracked = false;
        } else {
            throw new IncorrectFormattingException("Unexpected data at the end for athlete group", lineNumber);
        }

        return new AthleteGroupGeneralCharacteristics(groupSize, skillLevel, spontaneousness, tracked);
    }
    private AthleteGroupWeights parseWeights(String entry, int lineNumber) throws IncorrectFormattingException {
        Scanner scanner = new Scanner(entry);

        if (!scanner.hasNextDouble()) {
            throw new IncorrectFormattingException("Error parsing levelMatch weight for athlete group", lineNumber);
        }
        double levelMatch = scanner.nextDouble();

        if (!scanner.hasNextDouble()) {
            throw new IncorrectFormattingException("Error parsing surfaceTolerance weight for athlete group", lineNumber);
        }
        double surfaceTolerance = scanner.nextDouble();

        if (scanner.hasNext()) {
            throw new IncorrectFormattingException("Unexpected data at the end for athlete group weights", lineNumber);
        }

        return new AthleteGroupWeights(levelMatch, surfaceTolerance);
    }


    private AthleteGroupStartParameters parseStartParameters(String entry, int lineNumber) throws IncorrectFormattingException {
        Scanner scanner = new Scanner(entry);
        if  (!scanner.hasNextInt()) {
            throw new IncorrectFormattingException("Error parsing startNode for athlete group", lineNumber);
        }
        int startNode = scanner.nextInt();
        if  (!scanner.hasNextInt()) {
            throw new IncorrectFormattingException("Error parsing startTime for athlete group", lineNumber);
        }
        int hh = scanner.nextInt();
        scanner.next(":");
        int mm = scanner.nextInt();
        scanner.next(":");
        int ss = scanner.nextInt();
        int time = expandTime(hh, mm, ss);
        int spread = 0;
        if (scanner.hasNextInt()) {
            spread = scanner.nextInt();
        } else if (scanner.hasNext()) {
            throw new IncorrectFormattingException("Unexpected data at the end for athlete group", lineNumber);
        }
        return new AthleteGroupStartParameters(startNode, time, spread);
    }

    private int expandTime(int hh, int mm, int ss) {
        return ss + 60*mm + 3600*hh;
    }

    private void expandGroup(AthleteGroupGeneralCharacteristics characteristics, AthleteGroupWeights weights,
                             AthleteGroupStartParameters parameters, SimulationBuilder builder){
        int startTime = parameters.startTime;
        for (int i = 0; i < characteristics.groupSize; i++){
            builder.addAthlete(characteristics.skillLevel, characteristics.spontaneousness, characteristics.tracked, weights.levelMatch, weights.surfaceTolerance, parameters.startNode, parameters.startTime);
            startTime += parameters.timeSpread;
        }

    }

}
