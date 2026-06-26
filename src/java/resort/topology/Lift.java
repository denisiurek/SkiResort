package resort.topology;

import resort.athletes.Athlete;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Lift extends Connection {
    private final int departureSpread;
    private final int capacity;
    private final Queue<Athlete> queue;
    private int rideCount;
    private int maxQ;
    int latestUpdateTime;
    double updateVal;
    double avgQSize;
    int firstRunTime = 0;
    public Lift(int id, Node source, Node destination, int travelTime, int departureSpread, int capacity) {
        super(id, source, destination, travelTime);
        this.capacity = capacity;
        this.departureSpread = departureSpread;
        this.queue = new ArrayDeque<>();
        this.rideCount = 0;
        this.maxQ = 0;
        this.updateVal = 0.0;
        this.avgQSize = 0.0;
    }

    public int getDepartureSpread() {return departureSpread;}

    public void enqueue(Athlete athlete, int time) {
        pushAvgUpdate(time);
        queue.add(athlete);
        maxQ = Integer.max(queue.size(), maxQ);
    }

    public List<Athlete> takePassengers(int time) {
        ArrayList<Athlete> passengers = new ArrayList<>();
        int taken = 0;
        while (taken < capacity && !queue.isEmpty()) {
                passengers.add(queue.poll());
                taken++;
            }
        pushAvgUpdate(time);

        rideCount++;
        return passengers;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRideCount() {
        return rideCount;
    }
    public int getMaxQ() {
        return maxQ;
    }
    public int getQsize() {
        return queue.size();
    }
    public double getAvgQSize() {
        return avgQSize;
    }

    public void pushAvgUpdate(int currTime) {
        if (firstRunTime == 0) firstRunTime = currTime;
        currTime -= firstRunTime;
        if (currTime > latestUpdateTime) {
            int deltat = currTime - latestUpdateTime;
            avgQSize *= ((double) latestUpdateTime / (double) currTime);
            avgQSize += ((double) updateVal * (double) deltat) / (double) currTime;
            latestUpdateTime = currTime;
        }
        updateVal = (double) queue.size();
    }

}
