package simulation;

public class SimulationConfig {
    private final int startTime;
    private final int softStopTime;
    private final int hardStopTime;

    public SimulationConfig(int startTime, int softStopTime, int hardStopTime) {
        this.startTime = startTime;
        this.softStopTime = softStopTime;
        this.hardStopTime = hardStopTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getSoftStopTime() {
        return softStopTime;
    }

    public int getHardStopTime() {
        return hardStopTime;
    }
}

