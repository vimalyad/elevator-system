package ElevatorSystem.observers;

import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

public interface ElevatorObserver {
    void onFloorArrival(IElevator elevator, int floor);

    void onDoorStatusChange(IElevator elevator);

    void onAlarmTriggered(List<IElevator> elevators);

    void onOverloadWarning(IElevator elevator);
}
