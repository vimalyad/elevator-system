package ElevatorSystem.movers;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.elevator.IElevator;
import ElevatorSystem.observers.ElevatorObserver;

import java.util.List;

public class ElevatorMover {
    private final List<ElevatorObserver> observers;

    public ElevatorMover(List<ElevatorObserver> observers) {
        this.observers = observers;
    }

    public void moveToFloor(IElevator elevator, int destination) {
        int current = elevator.getCurrentFloor();

        Direction dir = destination > current ? Direction.UP : Direction.DOWN;
        elevator.setDirection(dir);
        elevator.setElevatorStatus(
                dir == Direction.UP ? ElevatorStatus.UP : ElevatorStatus.DOWN
        );

        elevator.getDoor().setDoorStatus(DoorStatus.CLOSED);

        while (elevator.getCurrentFloor() != destination) {
            int next = dir == Direction.UP
                    ? elevator.getCurrentFloor() + 1
                    : elevator.getCurrentFloor() - 1;
            elevator.setCurrentFloor(next);

            observers.forEach(o -> o.onFloorArrival(elevator, elevator.getCurrentFloor()));
        }

        elevator.getDoor().setDoorStatus(DoorStatus.OPEN);
        elevator.setDirection(Direction.NOT_MOVING);
        elevator.setElevatorStatus(ElevatorStatus.IDLE);
        observers.forEach(o -> o.onDoorStatusChange(elevator));
    }
}
