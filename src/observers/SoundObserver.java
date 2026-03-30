package ElevatorSystem.observers;

import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

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

    @Override
    public void onAlarmTriggered(List<IElevator> elevators) {
        System.out.println("[SOUND] *** ALARM SIREN ACTIVATED ***");
    }

    @Override
    public void onOverloadWarning(IElevator elevator) {
        System.out.println("[SOUND] *BUZZZZZZ* — Elevator " + elevator.getElevatorId() + " weight alarm sounding!");
    }
}
