package ElevatorSystem.strategies;

import ElevatorSystem.models.Request;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

public interface DispatchStrategy {
    IElevator selectElevator(List<IElevator> elevators, Request request);
}
