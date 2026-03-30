package ElevatorSystem.strategies;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.Request;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

public class SCANStrategy implements DispatchStrategy {

    @Override
    public IElevator selectElevator(List<IElevator> elevators, Request request) {
        return elevators.stream()
                .filter(e -> e.getElevatorStatus() != ElevatorStatus.NOT_AVAILABLE)
                .min((a, b) -> Integer.compare(score(a, request), score(b, request)))
                .orElseThrow(() -> new IllegalStateException("No elevators available"));
    }

    private int score(IElevator elevator, Request request) {
        int currentFloor = elevator.getCurrentFloor();
        int requestedFloor = request.getSourceFloor();
        int distance = Math.abs(currentFloor - requestedFloor);
        Direction elevDir = elevator.getDirection();
        Direction reqDir = request.getDirection();
        // if moving in same direction of requested
        if (isMovingToward(elevator, requestedFloor) && elevDir == reqDir) {
            return distance;
        }
        // if moving in different direction but toward the request
        if (isMovingToward(elevator, requestedFloor)) {
            return distance + 1000;
        }
        return distance + 2000;
    }

    private boolean isMovingToward(IElevator elevator, int targetFloor) {
        int current = elevator.getCurrentFloor();
        Direction dir = elevator.getDirection();
        if (dir == Direction.UP && targetFloor > current) return true;
        if (dir == Direction.DOWN && targetFloor < current) return true;
        if (dir == Direction.NOT_MOVING) return true;
        return false;
    }
}