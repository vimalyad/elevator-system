package ElevatorSystem.observers;

import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.models.elevator.IElevator;

public class SoundObserver implements ElevatorObserver {
    @Override
    public void onFloorArrival(IElevator elevator, int floor) {

    }

    @Override
    public void onDoorStatusChange(IElevator elevator) {
        if (elevator.getDoor().getDoorStatus() == DoorStatus.OPEN) {
            System.out.println(
                    "[SOUND] *ding* — Elevator " + elevator.getElevatorId() +
                            " arrived at floor " + elevator.getCurrentFloor()
            );
        }
    }
}
