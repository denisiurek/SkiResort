package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;
import resort.topology.Route;

public interface AthleteDecisionPolicy {
    Connection chooseConnection(Athlete athlete, Node node);

    default void onRouteEntry(Athlete athlete, Route route) {}
}
