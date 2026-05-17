package collections.athleteQueue;

import resort.athletes.Athlete;

public interface AthleteQueue {
    void add (Athlete athlete);
    Athlete fetch() throws AthleteQueueEmptyException;
    boolean isEmpty();
    Athlete peek() throws AthleteQueueEmptyException;
}
