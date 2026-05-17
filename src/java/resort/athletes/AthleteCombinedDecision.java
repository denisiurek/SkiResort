package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

import java.util.Random;

public class AthleteCombinedDecision implements AthleteDecisionPolicy {
    private final Random random;

    public AthleteCombinedDecision(Random random) {
        this.random = random;
    }

    public Connection chooseConnection(Athlete athlete, Node node) {
        double spontaneousness = athlete.getSpontaneousness();
        if (random.nextDouble() <= spontaneousness) {
            return new AthleteRandomDecision(random).chooseConnection(athlete, node);
        } else {
            return new AthletePreferenceDecision().chooseConnection(athlete, node);
        }
    }

}