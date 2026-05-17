package resort.topology;

public class Node {
    private final static int INITIAL_CONNECTION_CAPACITY = 4;
    private final int id, height, x, y;
    private final boolean communicated;
    private Lift[] outgoingLifts;
    private int outgoingLiftCount;
    private Route[] outgoingRoutes;
    private int outgoingRouteCount;
    private int visits;

    public Node(int id, int height, int x, int y, boolean communicated) {
        this.id = id;
        this.height = height;
        this.x = x;
        this.y = y;
        this.communicated = communicated;
        this.outgoingLifts = new Lift[INITIAL_CONNECTION_CAPACITY];
        this.outgoingLiftCount = 0;
        this.outgoingRoutes = new Route[INITIAL_CONNECTION_CAPACITY];
        this.outgoingRouteCount = 0;
        this.visits = 0;
    }

    public void addOutgoingLift(Lift lift) {
        ensureLiftSize(1);
        outgoingLifts[outgoingLiftCount++] = lift;
    }

    public void addOutgoingRoute(Route route) {
        ensureRouteSize(1);
        outgoingRoutes[outgoingRouteCount++] = route;
    }

    public Lift getOutgoingLift(int internalIndex) {
        return outgoingLifts[internalIndex];
    }

    public int getOutgoingLiftCount() {
        return outgoingLiftCount;
    }

    public Route getOutgoingRoute(int internalIndex) {
        return outgoingRoutes[internalIndex];
    }

    public int getOutgoingRouteCount() {
        return outgoingRouteCount;
    }

    public Lift[] getAllOutgoingLifts() {
        Lift[] lifts = new Lift[outgoingLiftCount];
        System.arraycopy(outgoingLifts, 0, lifts, 0, outgoingLiftCount);
        return lifts;
    }

    public Route[] getAllOutgoingRoutes() {
        Route[] routes = new Route[outgoingRouteCount];
        System.arraycopy(outgoingRoutes, 0, routes, 0, outgoingRouteCount);
        return routes;
    }

    public boolean isCommunicated() {
        return communicated;
    }

    public String toString() {
        return "Node " + (communicated ? "s" : "") + id + ", (x, y, height) = (" + x + ", " + y + ", " + height + ")" + ", totalVisits = " + visits;
    }

    private void ensureLiftSize(int desiredAdditionalSize) {
        if (outgoingLiftCount + desiredAdditionalSize >= outgoingLifts.length) {
            Lift[] newLifts = new Lift[(outgoingLiftCount + desiredAdditionalSize) * 2];
            System.arraycopy(outgoingLifts, 0, newLifts, 0, outgoingLiftCount);
            outgoingLifts = newLifts;
        }
    }

    private void ensureRouteSize(int desiredAdditionalSize) {
        if (outgoingRouteCount + desiredAdditionalSize >= outgoingRoutes.length) {
            Route[] newRoutes = new Route[(outgoingRouteCount + desiredAdditionalSize) * 2];
            System.arraycopy(outgoingRoutes, 0, newRoutes, 0, outgoingRouteCount);
            outgoingRoutes = newRoutes;
        }
    }

    public int getId() {return id;}

    public void registerVisit() {visits++;}

    public int getVisits() {return visits;}

}
