package ElevatorSystem.strategies;

import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.Request;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

public class NearestCarStrategy implements DispatchStrategy {

    @Override
    public IElevator selectElevator(List<IElevator> elevators, Request request) {
        return elevators.stream()
                .filter(e -> e.getElevatorStatus() != ElevatorStatus.NOT_AVAILABLE)
                .min((a, b) -> {
                    int distA = Math.abs(a.getCurrentFloor() - request.getSourceFloor());
                    int distB = Math.abs(b.getCurrentFloor() - request.getSourceFloor());
                    return Integer.compare(distA, distB);
                })
                .orElseThrow(() -> new IllegalStateException("No elevators available"));
    }
}


