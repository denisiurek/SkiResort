package simulation;


import collections.eventQueue.ArrayEventQueue;
import collections.eventQueue.EventQueue;
import collections.eventQueue.EventQueueEmptyException;
import resort.athletes.Athlete;
import resort.topology.Connection;
import resort.topology.Node;
import resort.topology.SkiResort;
import simulation.events.Event;
import simulation.events.NonSchedulableEvent;

import java.util.Random;

public class SimulationEngine implements Scheduler {
    private final Logger logger;
    private final EventQueue eventQueue;
    private final Random random;
    private final int softStopTime;
    private final int hardStopTime;
    private EngineState state;
    private int time;
    private SkiResort resort;
    private Athlete[] athletes; // retained for possible further neeeds of access

    public SimulationEngine(Logger logger, SimulationConfig config) {
        this.eventQueue = new ArrayEventQueue();
        this.time = config.startTime();
        this.softStopTime = config.softStopTime();
        this.hardStopTime = config.hardStopTime();
        this.logger = logger;
        this.state = EngineState.READY;
        this.random = new Random();
    }

    public void setResort(SkiResort resort) {
        this.resort = resort;
    }

    public void setAthletes(Athlete[] athletes) {
        this.athletes = athletes;
    }

    public void executeEvent(Event event) {
        if (event.getTime() > time) {
            time = event.getTime();
            state = getStateInTime(time);
        }
        if (state == EngineState.SOFT_STOPPED && !event.isFinishable() || state == EngineState.HARD_STOPPED) {
            return;
        }
        event.execute(this);
    }

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

    @Override
    public Random getRandomGenerator() {
        return random;
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
                executeEvent(eventQueue.fetch());
            }
        } catch (EventQueueEmptyException e) {
            logger.log("Event queue is empty", LogLevel.DEBUG);
        } finally {
            logger.log("Summary Report", LogLevel.INFO);
            for (Connection connection : resort.connections()) {
                logger.log(connection.toString(), LogLevel.INFO);
            }
            for (Node node : resort.nodes()) {
                logger.log(node.toString(), LogLevel.DEBUG);
            }
        }
    }

    public enum EngineState {
        NOT_INITIALIZED,
        READY,
        RUNNING,
        SOFT_STOPPED,
        HARD_STOPPED
    }

}