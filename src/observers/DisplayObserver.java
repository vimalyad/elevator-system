package ElevatorSystem.observers;

import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.models.elevator.IElevator;

public class DisplayObserver implements ElevatorObserver {
    @Override
    public void onFloorArrival(IElevator elevator, int floor) {
        System.out.println(
                "[DISPLAY] Elevator " + elevator.getElevatorId() +
                        " → Floor " + floor +
                        " (" + elevator.getDirection() + ")"
        );
    }

    @Override
    public void onDoorStatusChange(IElevator elevator) {
        String status = elevator.getDoor().getDoorStatus() == DoorStatus.OPEN
                ? "OPEN" : "CLOSED";
        System.out.println(
                "[DISPLAY] Elevator " + elevator.getElevatorId() +
                        " doors " + status + " at floor " + elevator.getCurrentFloor()
        );
    }
}
