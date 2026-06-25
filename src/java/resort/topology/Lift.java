package resort.topology;




import resort.athletes.Athlete;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Lift extends Connection {
    private final int departureSpread;
    private final int capacity;
    private final Queue<Athlete> queue;

    public Lift(int id, Node source, Node destination, int travelTime, int departureSpread, int capacity) {
        super(id, source, destination, travelTime);
        this.capacity = capacity;
        this.departureSpread = departureSpread;
        this.queue = new LinkedList<Athlete>();
    }

    public int getDepartureSpread() {return departureSpread;}

    public void enqueue(Athlete athlete) {
        queue.add(athlete);
    }

    public List<Athlete> takePassengers() {
        ArrayList<Athlete> passengers = new ArrayList<>();
        int taken = 0;
        while (taken < capacity && !queue.isEmpty()) {
                passengers.add(queue.poll());
                taken++;
            }
        return passengers;
    }

    @Override
    public String toString() {
        return "Lift " + getId() + " from " + getSource().getId() + " to " + getDestination().getId() + ", took: " + getUses() + " athletes";
    }
}
