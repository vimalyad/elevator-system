package ElevatorSystem.observers;

import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

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

    @Override
    public void onAlarmTriggered(List<IElevator> elevators) {
        elevators.forEach(e ->
                System.out.println("[DISPLAY] *** ALARM *** Elevator "
                        + e.getElevatorId() + " stopped at floor " + e.getCurrentFloor())
        );
    }

    @Override
    public void onOverloadWarning(IElevator elevator) {
        System.out.println("[DISPLAY] ⚠️ MAXIMUM CAPACITY REACHED IN ELEVATOR "
                + elevator.getElevatorId() + " ⚠️ — Please step out.");
    }
}
