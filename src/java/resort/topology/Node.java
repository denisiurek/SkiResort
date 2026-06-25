package resort.topology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    private final int id, height, x, y;
    private final boolean communicated;
    private final List<Lift> outgoingLifts;
    private final List<Route> outgoingRoutes;
    private int visits;

    public Node(int id, int height, int x, int y, boolean communicated) {
        this.id = id;
        this.height = height;
        this.x = x;
        this.y = y;
        this.communicated = communicated;
        this.outgoingLifts = new ArrayList<>();
        this.outgoingRoutes = new ArrayList<>();
        this.visits = 0;
    }

    public void addOutgoingLift(Lift lift) {
        outgoingLifts.add(lift);
    }

    public void addOutgoingRoute(Route route) {
        outgoingRoutes.add(route);
    }

    public List<Lift> getAllOutgoingLifts() {
        return Collections.unmodifiableList(outgoingLifts);
    }

    public List<Route> getAllOutgoingRoutes() {
        return Collections.unmodifiableList(outgoingRoutes);
    }

    public boolean isCommunicated() {
        return communicated;
    }

    public String toString() {
        return "Node " + (communicated ? "s" : "") + id + ", (x, y, height) = (" + x + ", " + y + ", " + height + ")" + ", totalVisits = " + visits;
    }


    public int getId() {return id;}

    public void registerVisit() {visits++;}

    public int getVisits() {return visits;}

}
