package ElevatorSystem.models;

import ElevatorSystem.enums.DoorStatus;

public class Door {
    private final int doorId;

    private DoorStatus doorStatus;

    public Door(int doorId) {
        this.doorId = doorId;
        this.doorStatus = DoorStatus.CLOSED;
    }

    public int getDoorId() {
        return doorId;
    }

    public DoorStatus getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(DoorStatus doorStatus) {
        this.doorStatus = doorStatus;
    }
}
