package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.Scanner;

public class RouteSectionParser implements SectionParser{

    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        for (int i = 0; i < entries.length; i++) {
                Scanner lineScanner = new Scanner(entries[i]);

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing startNode for route", i);
                }
                int startNode = lineScanner.nextInt();

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing endNode for route", i);
                }
                int endNode = lineScanner.nextInt();

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing routeDifficulty for route", i);
                }
                int routeDifficulty = lineScanner.nextInt();

                if  (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing routeDuration for route", i);
                }
                int routeDuration = lineScanner.nextInt();

                if  (!lineScanner.hasNextDouble()) {
                    throw new IncorrectFormattingException("Error parsing baseRouteAttractiveness for route", i);
                }
                double baseRouteAttractiveness = lineScanner.nextDouble();

                if  (!lineScanner.hasNextDouble()) {
                    throw new IncorrectFormattingException("Error parsing routeResilience for route", i);
                }
                double routeResilience = lineScanner.nextDouble();

                if (lineScanner.hasNext()) {
                    throw new IncorrectFormattingException("Unexpected data at the end for lift", i);
                }

                builder.addRoute(startNode, endNode, routeDifficulty, routeDuration, baseRouteAttractiveness, routeResilience);
            }
    }
}
