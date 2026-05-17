package resort.topology;

public class Route extends Connection {
    private final int difficulty;
    private final double resilience;
    private final double baseRouteAttractiveness;

    public Route(int id, Node source, Node destination, int travelTime, int difficulty, double resilience,
                 double baseRouteAttractiveness) {
        super(id, source, destination, travelTime);
        this.difficulty = difficulty;
        this.resilience = resilience;
        this.baseRouteAttractiveness = baseRouteAttractiveness;
    }

    public int getDifficulty() {return difficulty;}

    public double getWear() {
        return baseRouteAttractiveness + (1 - baseRouteAttractiveness) * Math.pow(resilience, getUses());
    }

    @Override
    public String toString() {
        return "Route " + getId() + " from " + getSource().getId() + " to " + getDestination().getId() + ", taken by:" +
                " " + getUses() + " athletes";
    }
}
