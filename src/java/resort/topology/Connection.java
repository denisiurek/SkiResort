package resort.topology;

public abstract class Connection {
    private final int id, travelTime;
    private final Node source;
    private final Node destination;
    private int uses;
    private int currentUsers; // for later statistics I want to play around with.

    protected Connection(int id, Node source, Node destination, int travelTime) {
        this.id = id;
        this.travelTime = travelTime;
        this.source = source;
        this.destination = destination;
        this.uses = 0;
        this.currentUsers = 0;
    }

    public Node getSource() {return source;}

    public Node getDestination() {return destination;}

    public int getTravelTime() {return travelTime;}

    public void registerEntry() {
        uses++;
        currentUsers++;
    }

    public void registerExit() {currentUsers--;}

    public int getUses() {return uses;}

    public int getId() {return id;}

    abstract public String toString();
}
