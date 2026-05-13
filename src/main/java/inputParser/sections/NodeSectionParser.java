package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.Scanner;

public class NodeSectionParser implements SectionParser{
    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
            for (int i = 0; i < entries.length; i++) {
                Scanner lineScanner = new Scanner(entries[i]);

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing height for node", i);
                }
                int height = lineScanner.nextInt();

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing x for node", i);
                }
                int x = lineScanner.nextInt();

                if (!lineScanner.hasNextInt()) {
                    throw new IncorrectFormattingException("Error parsing y for node", i);
                }
                int y = lineScanner.nextInt();

                boolean communicated;
                if (lineScanner.hasNext("s")){
                    communicated = true;
                } else if (!lineScanner.hasNext()) {
                    communicated = false;
                } else {
                    throw new IncorrectFormattingException("Unexpected data at the end for node", i);
                }
                builder.addNode(height, x, y, communicated);
            }
    }
}
