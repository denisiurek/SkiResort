package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

public interface AthleteDecisionPolicy {
    Connection chooseConnection(Athlete athlete, Node node);
}
