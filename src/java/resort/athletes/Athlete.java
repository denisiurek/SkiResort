package resort.athletes;

import resort.topology.Connection;
import resort.topology.Node;

public class Athlete {
    private final int id;
    private final int skill;
    private final double spontaneousness;
    private final double weightDifficulty;
    private final double weightWear;
    private final boolean tracked;
    private final AthleteDecisionPolicy decisionPolicy;

    public Athlete(int id, int skill, double spontaneousness, double weightDifficulty, double weightWear, boolean tracked, AthleteDecisionPolicy decisionPolicy) {
        this.id = id;
        this.skill = skill;
        this.spontaneousness = spontaneousness;
        this.weightDifficulty = weightDifficulty;
        this.weightWear = weightWear;
        this.tracked = tracked;
        this.decisionPolicy = decisionPolicy;
    }

    public int getId() {return id;}

    public int getSkill() {return skill;}

    public double getSpontaneousness() {return spontaneousness;}

    public double getWeightDifficulty() {return weightDifficulty;}

    public double getWeightWear() {return weightWear;}

    public boolean isTracked() {return tracked;}

    public Connection chooseNextConnection(Node node) {
        return decisionPolicy.chooseConnection(this, node);
    }

}
