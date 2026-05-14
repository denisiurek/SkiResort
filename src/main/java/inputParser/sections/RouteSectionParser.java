package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class RouteSectionParser implements SectionParser {

    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        for (int i = 0; i < entries.length; i++) {
            try {
                Scanner lineScanner = new Scanner(entries[i]);

                int startNode = lineScanner.nextInt();

                int endNode = lineScanner.nextInt();

                int routeDifficulty = lineScanner.nextInt();

                int routeDuration = lineScanner.nextInt();

                double baseRouteAttractiveness = lineScanner.nextDouble();

                double routeResilience = lineScanner.nextDouble();

                builder.addRoute(startNode, endNode, routeDifficulty, routeDuration, baseRouteAttractiveness, routeResilience);
            } catch (NoSuchElementException e) {
                throw new IncorrectFormattingException("Incorrect formatting in route section", i);
            }
        }
    }
}
