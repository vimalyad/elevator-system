package ElevatorSystem.models.elevator;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.Door;
import ElevatorSystem.models.panels.ElevatorPanel;

public class Elevator implements IElevator {
    private final int elevatorId;
    private ElevatorStatus elevatorStatus;
    private final Door door;
    private final ElevatorPanel panel;
    private final int maxFloor;
    private final int minFloor;

    public Elevator(int elevatorId, int minFloor, int maxFloor, ElevatorPanel elevatorPanel, Door door) {
        this.elevatorId = elevatorId;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.panel = elevatorPanel;
        this.elevatorStatus = ElevatorStatus.IDLE;
        this.door = door;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentFloor() {
        return panel.getCurrentFloor();
    }

    public void setCurrentFloor(int currentFloor) {
        this.panel.setCurrentFloor(currentFloor);
    }

    public Direction getDirection() {
        return panel.getDirection();
    }

    public void setDirection(Direction direction) {
        this.panel.setDirection(direction);
    }

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public int getMinFloor() {
        return minFloor;
    }

    public Door getDoor() {
        return door;
    }
}
