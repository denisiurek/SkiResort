package simulation.events;

public class EventQueueEmptyException extends RuntimeException {
    public EventQueueEmptyException(String message) {
        super(message);
    }
}
