package ElevatorSystem.controllers;

import ElevatorSystem.enums.RequestType;
import ElevatorSystem.models.Request;
import ElevatorSystem.models.elevator.IElevator;
import ElevatorSystem.movers.ElevatorMover;
import ElevatorSystem.observers.ElevatorObserver;
import ElevatorSystem.strategies.DispatchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ElevatorController {
    private final List<IElevator> elevators;
    private DispatchStrategy strategy;
    private final ElevatorMover mover;
    private final List<ElevatorObserver> observers = new ArrayList<>();

    private final Queue<Request> requestQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService dispatcher = Executors.newScheduledThreadPool(1);

    public ElevatorController(List<IElevator> elevators, DispatchStrategy strategy) {
        this.elevators = elevators;
        this.strategy = strategy;
        this.mover = new ElevatorMover(elevators, observers);

        startDispatcher();
    }

    private void startDispatcher() {
        dispatcher.scheduleAtFixedRate(this::processQueue, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void handleRequest(Request request) {
        requestQueue.offer(request);
        System.out.println("[CONTROLLER] Request added to Global Queue. Pending requests: " + requestQueue.size());
    }

    private synchronized void processQueue() {
        if (requestQueue.isEmpty()) return;

        Request nextRequest = requestQueue.peek();

        try {
            IElevator elevator = strategy.selectElevator(elevators, nextRequest);
            requestQueue.poll();
            dispatchElevator(elevator, nextRequest);
        } catch (IllegalStateException ignored) {

        }
    }

    private void dispatchElevator(IElevator elevator, Request request) {
        int targetFloor = (request.getRequestType() == RequestType.EXTERNAL)
                ? request.getSourceFloor()
                : request.getDestinationFloor();

        mover.addDestination(elevator, targetFloor);
    }

    public void setStrategy(DispatchStrategy strategy) {
        this.strategy = strategy;
    }

    public void registerObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public void handleAlarm() {
        elevators.forEach(mover::emergencyStop);
        observers.forEach(o -> o.onAlarmTriggered(elevators));
    }

    public void shutdown() {
        dispatcher.shutdown();
        mover.shutdown();
    }
}