package resort.topology;

public class SkiResort {
    private final Node[] nodes;
    private final Connection[] connections;

    public SkiResort(Node[] nodes, Connection[] connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

    public Connection[] getConnections() {
        return connections;
    }
    public Node[] getNodes() {
        return nodes;
    }

}
