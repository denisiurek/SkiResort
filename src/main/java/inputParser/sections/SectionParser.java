package inputParser.sections;

import inputParser.IncorrectFormattingException;
import inputParser.SimulationBuilder;

public interface SectionParser {
    void parseSection(String[] entries, SimulationBuilder builder) throws IncorrectFormattingException;
}
