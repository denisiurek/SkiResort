package simulation.events.eventQueue;

import simulation.events.Event;
import simulation.events.EventQueueEmptyException;

public class ArrayEventQueue implements EventQueue {
    Event[] events;
    int eventCount;
    private static final int DEFAULT_SIZE = 100;

    public ArrayEventQueue() {
        this(DEFAULT_SIZE);
    }

    public ArrayEventQueue(int size) {
        events = new Event[size];
    }

    @Override
    public void add(Event newEvent) {
        ensureEventsSize(1);
        int i = 0;
        while (i != eventCount && events[i].getTime() > newEvent.getTime()) i++;
        shiftEventsArray(i, 1);
        events[i] = newEvent;
    }
    private void ensureEventsSize(int desiredAdditionalSize) {
        if (eventCount + desiredAdditionalSize >=events.length) {
            Event[] newEvents =
                    new Event[(eventCount + desiredAdditionalSize) * 2];
            System.arraycopy(events, 0, newEvents, 0, eventCount);
            events = newEvents;
        }
    }
    private void shiftEventsArray(int startPos, int shiftSize) {
        if (startPos == eventCount) return;
        System.arraycopy(events, startPos, events, startPos+shiftSize,
                eventCount-startPos);

    }
    @Override
    public Event fetch() throws EventQueueEmptyException {
        if (eventCount == 0) throw new EventQueueEmptyException("q emty");
        Event event = events[--eventCount];
        events[eventCount] = null;
        return event;
    }

    @Override
    public boolean isEmpty() {
        return eventCount == 0;
    }
}
