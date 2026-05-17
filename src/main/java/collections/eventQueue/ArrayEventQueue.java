package collections.eventQueue;

import simulation.events.Event;

public class ArrayEventQueue implements EventQueue {
    private static final int DEFAULT_SIZE = 100;
    Event[] events;
    int eventCount;

    public ArrayEventQueue() {
        this(DEFAULT_SIZE);
    }

    public ArrayEventQueue(int size) {
        events = new Event[size];
        eventCount = 0;
    }

    @Override
    public void add(Event newEvent) {
        ensureEventsSize(1);
        int i = 0;
        while (i < eventCount && events[i].getTime() > newEvent.getTime()) {
            i++;
        }
        shiftEventsArray(i, 1);
        events[i] = newEvent;
        eventCount++;
    }

    private void ensureEventsSize(int desiredAdditionalSize) {
        if (eventCount + desiredAdditionalSize >= events.length) {
            Event[] newEvents =
                    new Event[(eventCount + desiredAdditionalSize) * 2];
            System.arraycopy(events, 0, newEvents, 0, eventCount);
            events = newEvents;
        }
    }

    private void shiftEventsArray(int startPos, int shiftSize) {
        if (startPos == eventCount) return;
        System.arraycopy(events, startPos, events, startPos + shiftSize,
                eventCount - startPos);

    }

    @Override
    public Event fetch() throws EventQueueEmptyException {
        if (isEmpty()) throw new EventQueueEmptyException("Event queue is empty");
        Event event = events[--eventCount];
        events[eventCount] = null;
        return event;
    }

    public Event peek() throws EventQueueEmptyException {
        if (isEmpty()) throw new EventQueueEmptyException("Event queue is empty");
        return events[eventCount - 1];
    }
    @Override
    public boolean isEmpty() {
        return eventCount == 0;
    }
}
