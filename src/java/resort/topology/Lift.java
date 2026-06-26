package resort.topology;

import resort.athletes.Athlete;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class Lift extends Connection {
    private final int departureSpread;
    private final int capacity;
    private final Queue<Athlete> queue;
    private int rideCount;
    private int maxQ;
    private int latestUpdateTime;
    private double updateVal;
    private double avgQSize;
    private int firstRunTime = 0;
    private final int softStopTime;
    public Lift(int id, Node source, Node destination, int travelTime, int departureSpread, int capacity, int softStopTime) {
        super(id, source, destination, travelTime);
        this.capacity = capacity;
        this.departureSpread = departureSpread;
        this.queue = new ArrayDeque<>();
        this.rideCount = 0;
        this.maxQ = 0;
        this.updateVal = 0.0;
        this.avgQSize = 0.0;
        this.softStopTime = softStopTime;
    }

        public Lift(int id, Node source, Node destination, int travelTime, int departureSpread, int capacity) {
        super(id, source, destination, travelTime);
        this.capacity = capacity;
        this.departureSpread = departureSpread;
        this.queue = new ArrayDeque<>();
        this.rideCount = 0;
        this.maxQ = 0;
        this.updateVal = 0.0;
        this.avgQSize = 0.0;
        this.softStopTime = Integer.MAX_VALUE;
    }

    public int getDepartureSpread() {return departureSpread;}

    public void enqueue(Athlete athlete, int time) {
        pushAvgUpdate(time);
        queue.add(athlete);
        maxQ = Integer.max(queue.size(), maxQ);
        pushAvgUpdate(time);
    }

    public List<Athlete> takePassengers(int time) {
        pushAvgUpdate(time); // to process before (possible cutoff)
        List<Athlete> passengers = new ArrayList<>();
        while (passengers.size() < capacity && !queue.isEmpty())
            passengers.add(queue.poll());
        pushAvgUpdate(time);
        return passengers;
    }

    public void registerArrival() {
        rideCount++;
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
    public double getAvgQSize(int currentTime) {
        pushAvgUpdate(currentTime);
        return avgQSize;
    }

    private void pushAvgUpdate(int currTime) {
        if (currTime > softStopTime) currTime = softStopTime;
        if (firstRunTime == 0) firstRunTime = currTime;
        currTime -= firstRunTime;
        if (currTime > latestUpdateTime) {
            int deltat = currTime - latestUpdateTime;
            avgQSize *= ((double) latestUpdateTime / (double) currTime);
            avgQSize += (updateVal * (double) deltat) / (double) currTime;
            latestUpdateTime = currTime;
        }
        updateVal = queue.size();
    }

}
