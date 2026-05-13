package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.Scanner;

public class LiftSectionParser implements SectionParser{
    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        for (int i = 0; i < entries.length; i++) {
                Scanner lineScanner = new Scanner(entries[i]);

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing startNode for lift", i);
                }
                int startNode = lineScanner.nextInt();

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing endNode for lift", i);
                }
                int endNode = lineScanner.nextInt();

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing groupTimeSpread for lift", i);
                }
                int groupTimeSpread = lineScanner.nextInt();

                if  (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing maxGroupSize for lift", i);
                }
                int maxGroupSize = lineScanner.nextInt();

                if  (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing liftDuration for lift", i);
                }
                int liftDuration = lineScanner.nextInt();

                if (lineScanner.hasNext()) {
                    throw new IncorrectFormattingException("Unexpected data at the end for lift", i);
                }

                builder.addLift(startNode, endNode, groupTimeSpread, maxGroupSize, liftDuration);
            }
    }
}
