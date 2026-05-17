package inputParser;

import simulation.Logger;
import simulation.SimulationConfig;
import simulation.SimulationEngine;

public interface InputParser {
    SimulationEngine parse(Logger logger, SimulationConfig config) throws IncorrectFormattingException;
}
