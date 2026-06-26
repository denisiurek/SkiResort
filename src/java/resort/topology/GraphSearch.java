package resort.topology;

import java.util.*;
import java.util.stream.Stream;

public final class GraphSearch {
    private GraphSearch() {}

    public static List<Connection> bfsPath(Node from, Node to) throws RuntimeException {
        if (from == to) return List.of();

        ArrayDeque<Node> queue = new ArrayDeque<>();
        HashMap<Node, Connection> parent = new HashMap<>();
        queue.add(from);
        parent.put(from, null);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Connection c : outgoing(current)) {
                Node next = c.getDestination();
                if (!parent.containsKey(next)) {
                    parent.put(next, c);
                    if (next == to) return reconstructPath(parent, to);
                    queue.add(next);
                }
            }
        }
        throw new RuntimeException("Node is unreachable");
    }

    public static Map<Node, Integer> bfsDistances(Node from) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        HashMap<Node, Integer> distances = new HashMap<>();
        queue.add(from);
        distances.put(from, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int dist = distances.get(current);
            for (Connection c : outgoing(current)) {
                Node next = c.getDestination();
                if (!distances.containsKey(next)) {
                    distances.put(next, dist + 1);
                    queue.add(next);
                }
            }
        }
        return distances;
    }

    private static List<Connection> outgoing(Node node) {
        return Stream.concat(
            node.getAllOutgoingLifts().stream(),
            node.getAllOutgoingRoutes().stream()
        ).toList();
    }

    private static List<Connection> reconstructPath(Map<Node, Connection> parent, Node to) {
        LinkedList<Connection> path = new LinkedList<>();
        Node current = to;
        while (parent.get(current) != null) {
            Connection edge = parent.get(current);
            path.addFirst(edge);
            current = edge.getSource();
        }
        return path;
    }
}
