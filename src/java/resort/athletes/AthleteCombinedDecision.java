package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

import java.util.Random;

public class AthleteCombinedDecision implements AthleteDecisionPolicy {
    private final Random random;
    private final AthleteDecisionPolicy randomPolicy;
    private final AthleteDecisionPolicy preferencePolicy;

    public AthleteCombinedDecision(Random random) {
        this.random = random;
        this.randomPolicy = new AthleteRandomDecision(random);
        this.preferencePolicy = new AthletePreferenceDecision();
    }

    @Override
    public Connection chooseConnection(Athlete athlete, Node node) {
        if (random.nextDouble() <= athlete.getSpontaneousness()) {
            return randomPolicy.chooseConnection(athlete, node);
        }
        return preferencePolicy.chooseConnection(athlete, node);
    }
}