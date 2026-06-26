import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resort.topology.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphSearchTest {

    private Node[] nodes;

    @BeforeEach
    void buildGraph() {
        nodes = new Node[6];
        for (int i = 0; i < 6; i++)
            nodes[i] = new Node(i, 1000 + i * 100, i, i, false);

        Lift lift0 = new Lift(0, nodes[0], nodes[1], 120, 10, 4);
        nodes[0].addOutgoingLift(lift0);

        Route route0 = new Route(0, nodes[1], nodes[2], 60, 3, 0.999, 0.3);
        nodes[1].addOutgoingRoute(route0);

        Lift lift1 = new Lift(1, nodes[2], nodes[4], 120, 10, 4);
        nodes[2].addOutgoingLift(lift1);

        Route route1 = new Route(1, nodes[3], nodes[1], 60, 3, 0.999, 0.3);
        nodes[3].addOutgoingRoute(route1);

        Route route2 = new Route(2, nodes[4], nodes[5], 60, 3, 0.999, 0.3);
        nodes[4].addOutgoingRoute(route2);

        Lift lift2 = new Lift(2, nodes[5], nodes[3], 120, 10, 4);
        nodes[5].addOutgoingLift(lift2);

        Route route3 = new Route(3, nodes[4], nodes[0], 60, 3, 0.999, 0.3);
        nodes[4].addOutgoingRoute(route3);

        Lift lift3 = new Lift(3, nodes[0], nodes[3], 120, 10, 4);
        nodes[0].addOutgoingLift(lift3);
    }

    @Test
    void pathFromZeroToFour_distanceThree() {
        List<Connection> path = GraphSearch.bfsPath(nodes[0], nodes[4]);
        assertEquals(3, path.size());

        assertEquals(nodes[0], path.get(0).getSource());
        assertEquals(nodes[1], path.get(0).getDestination());
        assertEquals(nodes[1], path.get(1).getSource());
        assertEquals(nodes[2], path.get(1).getDestination());
        assertEquals(nodes[2], path.get(2).getSource());
        assertEquals(nodes[4], path.get(2).getDestination());

        Map<Node, Integer> distances = GraphSearch.bfsDistances(nodes[0]);
        assertEquals(3, distances.get(nodes[4]));
    }

    @Test
    void pathFromThreeToOne_direct() {
        List<Connection> path = GraphSearch.bfsPath(nodes[3], nodes[1]);
        assertEquals(1, path.size());
        assertEquals(nodes[3], path.getFirst().getSource());
        assertEquals(nodes[1], path.getFirst().getDestination());

        Map<Node, Integer> distances = GraphSearch.bfsDistances(nodes[3]);
        assertEquals(1, distances.get(nodes[1]));
    }

    @Test
    void pathFromTwoToSelf_empty() {
        List<Connection> path = GraphSearch.bfsPath(nodes[2], nodes[2]);
        assertTrue(path.isEmpty());

        Map<Node, Integer> distances = GraphSearch.bfsDistances(nodes[2]);
        assertEquals(0, distances.get(nodes[2]));
    }

    @Test
    void pathFromFourToThree_distanceTwo() {
        List<Connection> path = GraphSearch.bfsPath(nodes[4], nodes[3]);
        assertEquals(2, path.size());

        assertEquals(nodes[4], path.getFirst().getSource());
        Node mid = path.get(0).getDestination();
        assertTrue(mid == nodes[5] || mid == nodes[0],
            "Path should go through node 5 or node 0, got " + mid.getId());
        assertEquals(mid, path.get(1).getSource());
        assertEquals(nodes[3], path.get(1).getDestination());

        Map<Node, Integer> distances = GraphSearch.bfsDistances(nodes[4]);
        assertEquals(2, distances.get(nodes[3]));
    }
}
