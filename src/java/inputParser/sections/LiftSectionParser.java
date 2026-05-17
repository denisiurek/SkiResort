package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LiftSectionParser implements SectionParser {
    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        for (int i = 0; i < entries.length; i++) {
            try {
                Scanner lineScanner = new Scanner(entries[i]);

                int startNode = lineScanner.nextInt();

                int endNode = lineScanner.nextInt();

                int groupTimeSpread = lineScanner.nextInt();

                int maxGroupSize = lineScanner.nextInt();

                int liftDuration = lineScanner.nextInt();

                builder.addLift(startNode, endNode, groupTimeSpread, maxGroupSize, liftDuration);
            } catch (NoSuchElementException e) {
                throw new IncorrectFormattingException("Incorrect formatting in lift section", i);
            }
        }
    }
}
