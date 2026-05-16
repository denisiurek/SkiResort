package collections.athleteQueue;

import resort.athletes.Athlete;

/*
 * TODO: MAKE A CYCLIC BUFFER INSTEAD TO REDUCE THE MEM COPY
 */
public class ArrayAthleteQueue implements AthleteQueue {
    private Athlete[] athletes;
    private int head;
    private int athleteCount;
    private static final int DEFAULT_SIZE = 20;

    public ArrayAthleteQueue(int athleteCount) {
        this.athleteCount = 0;
        this.head = 0;
        athletes = new Athlete[athleteCount];
    }

    public ArrayAthleteQueue() {
        this(DEFAULT_SIZE);
    }

    @Override
    public void add(Athlete athlete) {
        ensureCapacity(1);
        int index = (head + athleteCount) % athletes.length;
        athletes[index] = athlete;
        athleteCount++;
    }

    @Override
    public Athlete fetch() throws AthleteQueueEmptyException {
        if (isEmpty()) throw new AthleteQueueEmptyException("Athlete q is empty");
        Athlete athlete = athletes[head];
        athletes[head] = null;
        head = (head + 1) % athletes.length;
        athleteCount--;
        return athlete;
    }

    @Override
    public boolean isEmpty() {
        return athleteCount == 0;
    }

    @Override
    public Athlete peek() throws AthleteQueueEmptyException {
        if (isEmpty()) throw new AthleteQueueEmptyException("Athlete q is empty");
        return athletes[head];
    }

    private void ensureCapacity(int desiredAdditionalSize) {
        if (athleteCount + desiredAdditionalSize > athletes.length) {
            Athlete[] newBuffer = new Athlete[(athleteCount + desiredAdditionalSize) * 2];
            for (int i = 0; i < athleteCount; i++) {
                newBuffer[i] = athletes[(head + i) % athletes.length];
            }
            athletes = newBuffer;
            head = 0;
        }
    }
}
