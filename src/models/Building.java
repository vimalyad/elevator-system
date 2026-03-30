package ElevatorSystem.models;

import ElevatorSystem.models.elevator.Elevator;

import java.util.Collections;
import java.util.List;

public class Building {
    private List<Floor> floors;
    private List<Elevator> elevators;

    public Building(List<Floor> floors, List<Elevator> elevators) {
        this.floors = floors;
        this.elevators = elevators;
    }

    public List<Floor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public List<Elevator> getElevators() {
        return Collections.unmodifiableList(elevators);
    }
}
