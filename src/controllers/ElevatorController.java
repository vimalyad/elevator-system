package ElevatorSystem.controllers;

import ElevatorSystem.enums.RequestType;
import ElevatorSystem.models.Request;
import ElevatorSystem.models.elevator.IElevator;
import ElevatorSystem.movers.ElevatorMover;
import ElevatorSystem.observers.ElevatorObserver;
import ElevatorSystem.strategies.DispatchStrategy;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private final List<IElevator> elevators;
    private DispatchStrategy strategy;
    private final ElevatorMover mover;
    private final List<ElevatorObserver> observers = new ArrayList<>();

    public ElevatorController(List<IElevator> elevators, DispatchStrategy strategy) {
        this.elevators = elevators;
        this.strategy = strategy;
        this.mover = new ElevatorMover(observers);
    }

    public void handleRequest(Request request) {
        IElevator elevator = strategy.selectElevator(elevators, request);
        if (request.getRequestType() == RequestType.INTERNAL) {
            mover.moveToFloor(elevator, request.getSourceFloor());
        } else {
            mover.moveToFloor(elevator, request.getDestinationFloor());
        }
    }

    public void setStrategy(DispatchStrategy strategy) {
        this.strategy = strategy;
    }

    public void registerObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    private void assignElevator(IElevator elevator, Request request) {
        elevator.setCurrentFloor(request.getDestinationFloor());
        notifyFloorArrival(elevator, request.getDestinationFloor());
    }

    private void notifyFloorArrival(IElevator elevator, int floor) {
        observers.forEach(o -> o.onFloorArrival(elevator, floor));
    }
}