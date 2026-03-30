package ElevatorSystem.observers;

import ElevatorSystem.models.elevator.IElevator;

public interface ElevatorObserver {
    void onFloorArrival(IElevator elevator, int floor);

    void onDoorStatusChange(IElevator elevator);
}
