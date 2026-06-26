package resort.topology;

public abstract sealed class Connection permits Lift, Route {
    private final int id, travelTime;
    private final Node source;
    private final Node destination;
    private int uses;

    protected Connection(int id, Node source, Node destination, int travelTime) {
        this.id = id;
        this.travelTime = travelTime;
        this.source = source;
        this.destination = destination;
        this.uses = 0;
    }

    public Node getSource() {return source;}

    public Node getDestination() {return destination;}

    public int getTravelTime() {return travelTime;}

    public void registerEntry() {
        uses++;
    }

    public void registerExit() {}

    public int getUses() {return uses;}

    public int getId() {return id;}

}
