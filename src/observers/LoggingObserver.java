package ElevatorSystem.observers;

import ElevatorSystem.models.elevator.IElevator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingObserver implements ElevatorObserver {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void onFloorArrival(IElevator elevator, int floor) {
        System.out.println(
                "[LOG " + now() + "] " +
                        "Elevator " + elevator.getElevatorId() +
                        " passed floor " + floor +
                        " | direction=" + elevator.getDirection() +
                        " | status=" + elevator.getElevatorStatus()
        );
    }

    @Override
    public void onDoorStatusChange(IElevator elevator) {
        System.out.println(
                "[LOG " + now() + "] " +
                        "Elevator " + elevator.getElevatorId() +
                        " door=" + elevator.getDoor().getDoorStatus() +
                        " at floor " + elevator.getCurrentFloor()
        );
    }

    private String now() {
        return LocalDateTime.now().format(FMT);
    }
}
