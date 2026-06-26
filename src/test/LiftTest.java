import org.junit.jupiter.api.Test;
import resort.athletes.Athlete;
import resort.topology.Lift;
import resort.topology.Node;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LiftTest {

    private static Node dummyNode() {
        return new Node(0, 0, 0, 0, false);
    }

    private static Athlete dummyAthlete(int id) {
        return new Athlete(id, 5, 0.0, 0.5, 0.5, 0.0, 0.0, 0, false, (a, n) -> null);
    }

    private static Lift createLift(int capacity) {
        Node src = dummyNode();
        Node dst = dummyNode();
        return new Lift(0, src, dst, 120, 10, capacity);
    }

    @Test
    void fourAthletesCapacityThree_onlyThreeDepart() {
        Lift lift = createLift(3);
        int time = 100;

        for (int i = 0; i < 4; i++) {
            lift.enqueue(dummyAthlete(i), time);
        }

        List<Athlete> passengers = lift.takePassengers(time + 10);

        assertEquals(3, passengers.size());
        assertEquals(1, lift.getQsize());
    }

    @Test
    void twoAthletesCapacityThree_bothDepart() {
        Lift lift = createLift(3);
        int time = 100;

        lift.enqueue(dummyAthlete(0), time);
        lift.enqueue(dummyAthlete(1), time);

        List<Athlete> passengers = lift.takePassengers(time + 10);

        assertEquals(2, passengers.size());
        assertEquals(0, lift.getQsize());
    }

    @Test
    void maxQueueLength_trackedAcrossDeparture() {
        Lift lift = createLift(3);
        int time = 100;

        for (int i = 0; i < 4; i++) {
            lift.enqueue(dummyAthlete(i), time + i);
        }

        assertEquals(4, lift.getMaxQ());

        lift.takePassengers(time + 10);

        lift.enqueue(dummyAthlete(4), time + 11);

        assertEquals(4, lift.getMaxQ());
    }
}
