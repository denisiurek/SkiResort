package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;
import resort.topology.Route;

public class Athlete {
    private final int id;
    private final int skill;
    private final double spontaneousness;
    private final double weightDifficulty;
    private final double weightWear;
    private final double beta;
    private final double alphaZ;
    private final boolean tracked;
    private final AthleteDecisionPolicy decisionPolicy;

    private final double[] boredom;
    private final int[] lastBoredomTick;
    private int rideCount;

    public Athlete(int id, int skill, double spontaneousness, double weightDifficulty, double weightWear,
                   double beta, double alphaZ, int routeCount, boolean tracked, AthleteDecisionPolicy decisionPolicy) {
        this.id = id;
        this.skill = skill;
        this.spontaneousness = spontaneousness;
        this.weightDifficulty = weightDifficulty;
        this.weightWear = weightWear;
        this.beta = beta;
        this.alphaZ = alphaZ;
        this.tracked = tracked;
        this.decisionPolicy = decisionPolicy;
        this.boredom = new double[routeCount];
        this.lastBoredomTick = new int[routeCount];
        this.rideCount = 0;
    }

    public void onRouteEntry(Route route) {
        rideCount++;
        int rid = route.getId();
        int decayTicks = rideCount - 1 - lastBoredomTick[rid];
        if (decayTicks > 0) boredom[rid] *= Math.pow(1 - beta, decayTicks);
        boredom[rid] = beta + (1 - beta) * boredom[rid];
        lastBoredomTick[rid] = rideCount;
        decisionPolicy.onRouteEntry(this, route);
    }

    public double getBoredom(Route route) {
        int rid = route.getId();
        int decayTicks = rideCount - lastBoredomTick[rid];
        if (decayTicks > 0) {
            return boredom[rid] * Math.pow(1 - beta, decayTicks);
        }
        return boredom[rid];
    }

    public int getId() {return id;}

    public int getSkill() {return skill;}

    public double getSpontaneousness() {return spontaneousness;}

    public double getWeightDifficulty() {return weightDifficulty;}

    public double getWeightWear() {return weightWear;}

    public double getAlphaZ() {return alphaZ;}

    public boolean isTracked() {return tracked;}

    public Connection chooseNextConnection(Node node) {
        return decisionPolicy.chooseConnection(this, node);
    }
}
