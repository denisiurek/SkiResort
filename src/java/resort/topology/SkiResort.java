package resort.topology;

import java.util.List;

public record SkiResort(List<Node> nodes, List<Connection> connections, List<Route> routes) {
}
