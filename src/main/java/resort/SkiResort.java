package resort;

import resort.topology.Connection;
import resort.topology.Node;

public class SkiResort {
    private final Node[] nodes;
    private final Connection[] connections;

    public SkiResort(Node[] nodes, Connection[] connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

}
