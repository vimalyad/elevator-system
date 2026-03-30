package ElevatorSystem.models.panels;

import ElevatorSystem.enums.Direction;

public interface ElevatorPanel extends Panel {
    int getCurrentFloor();

    void setCurrentFloor(int floor);

    Direction getDirection();

    void setDirection(Direction direction);
}
