package simulation;


import resort.athletes.Athlete;
import resort.SkiResort;
import simulation.events.Event;
import simulation.events.NonSchedulableEvent;
import collections.eventQueue.ArrayEventQueue;
import collections.eventQueue.EventQueue;
import collections.eventQueue.EventQueueEmptyException;
import timeUtils.TimeOperators;

public class SimulationEngine implements Scheduler {
    private Logger logger;
    private EventQueue eventQueue;
    private int time;
    private int softStopTime;
    private int hardStopTime;
    private SkiResort resort;
    private Athlete[] athletes;

    public SimulationEngine(Logger logger, SimulationConfig config) {
        this.eventQueue = new ArrayEventQueue();
        this.time = config.getStartTime();
        this.softStopTime = config.getSoftStopTime();
        this.hardStopTime = config.getHardStopTime();
        this.logger = logger;
        this.state = EngineState.READY;
    }

    public void setResort(SkiResort resort) {
        this.resort = resort;
    }
    public void setAthletes(Athlete[] athletes) {
        this.athletes = athletes;
    }

    public enum EngineState {
        NOT_INITIALIZED,
        READY,
        RUNNING,
        SOFT_STOPPED,
        HARD_STOPPED
    }
    EngineState state;

    public void scheduleEvent(Event event) { //rework
        if (event instanceof NonSchedulableEvent) {
            throw new IllegalStateException("Attempted to schedule a non-schedulable event");
        }
        EngineState stateAtEventTime = getStateInTime(event.getTime());
        switch (stateAtEventTime) {
            case HARD_STOPPED:
                break;
            case SOFT_STOPPED:
                if (event.isFinishable()) {
                    eventQueue.add(event);
                }
                break;
            case RUNNING:
                eventQueue.add(event);
                break;
            default:
                throw new IllegalStateException("Engine is not in a valid state to schedule events");
        }
    }

    @Override
    public int getCurrentTime() {
        return time;
    }

    @Override
    public void log(Event event) {
        logger.log(event);
    }

    private EngineState getStateInTime(int time) {
        if (time < softStopTime) {
            return EngineState.RUNNING;
        } else if (time < hardStopTime) {
            return EngineState.SOFT_STOPPED;
        } else {
            return EngineState.HARD_STOPPED;
        }
    }
    public void run() {
        state = EngineState.RUNNING;
        try {
            while (state != EngineState.HARD_STOPPED) {
                Event event = eventQueue.fetch();
                if (event.getTime() > time) {
                    time = event.getTime();
                    state = getStateInTime(time);
                } else {
                    event.execute(this);
                }

            }
        } catch (EventQueueEmptyException e) {
            System.out.println("Event queue is empty. Simulation ended at time " + TimeOperators.formatTime(time));
        }
    }

}