package resort.topology;

import collections.athleteQueue.ArrayAthleteQueue;
import collections.athleteQueue.AthleteQueue;
import collections.athleteQueue.AthleteQueueEmptyException;
import resort.athletes.Athlete;

public class Lift extends Connection {
    private final int departureSpread;
    private final int capacity;
    private final AthleteQueue queue;

    public Lift(int id, Node source, Node destination, int travelTime, int departureSpread, int capacity) {
        super(id, source, destination, travelTime);
        this.capacity = capacity;
        this.departureSpread = departureSpread;
        this.queue = new ArrayAthleteQueue(capacity * 2);
    }

    public int getDepartureSpread() {return departureSpread;}

    public void enqueue(Athlete athlete) {
        queue.add(athlete);
    }

    public Athlete[] takePassengers() {
        Athlete[] passengers = new Athlete[capacity];
        int taken = 0;
        while (taken < capacity) {
            try {
                passengers[taken] = queue.fetch();
                taken++;
            } catch (AthleteQueueEmptyException e) {
                break; // Queue is empty.
            }
        }
        Athlete[] passengersTaken = new Athlete[taken];
        System.arraycopy(passengers, 0, passengersTaken, 0, taken);
        return passengersTaken;
    }

    @Override
    public String toString() {
        return "Lift " + getId() + " from " + getSource().getId() + " to " + getDestination().getId() + ", took: " + getUses() + " athletes";
    }
}
