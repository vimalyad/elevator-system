package ElevatorSystem.strategies;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.Request;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

public class SCANStrategy implements DispatchStrategy {

    private static final int AVG_PERSON_WEIGHT = 75;

    @Override
    public IElevator selectElevator(List<IElevator> elevators, Request request) {
        return elevators.stream()
                .filter(e -> e.getElevatorStatus() != ElevatorStatus.NOT_AVAILABLE)
                .filter(e -> e.getElevatorStatus() != ElevatorStatus.MAINTENANCE)
                .filter(e -> e.getWeightSensor().canAccept(AVG_PERSON_WEIGHT))
                .min((a, b) -> Integer.compare(score(a, request), score(b, request)))
                .orElseThrow(() -> new IllegalStateException("No elevators available"));
    }

    private int score(IElevator elevator, Request request) {
        int currentFloor = elevator.getCurrentFloor();
        int requestedFloor = request.getSourceFloor();
        int distance = Math.abs(currentFloor - requestedFloor);
        Direction elevDir = elevator.getDirection();
        Direction reqDir = request.getDirection();

        if (isMovingToward(elevator, requestedFloor) && elevDir == reqDir) {
            return distance;
        }
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
        return dir == Direction.NOT_MOVING;
    }
}