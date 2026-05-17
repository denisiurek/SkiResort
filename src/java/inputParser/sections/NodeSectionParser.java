package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class NodeSectionParser implements SectionParser {
    @Override
    public void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException {
        for (int i = 0; i < entries.length; i++) {
            try {
                Scanner lineScanner = new Scanner(entries[i]);

                int height = lineScanner.nextInt();

                int x = lineScanner.nextInt();

                int y = lineScanner.nextInt();

                boolean communicated = lineScanner.hasNext("s");

                builder.addNode(height, x, y, communicated);
            } catch (NoSuchElementException e) {
                throw new IncorrectFormattingException("Incorrect formatting " +
                        "in node section", i);
            }
        }
    }
}
