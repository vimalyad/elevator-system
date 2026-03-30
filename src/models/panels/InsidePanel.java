package ElevatorSystem.models.panels;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.models.buttons.Button;

import java.util.List;

public class InsidePanel implements ElevatorPanel {
    private final List<Button> buttons;
    private int currentFloor;
    private Direction direction;

    public InsidePanel(List<Button> buttons, int currentFloor) {
        this.buttons = buttons;
        this.currentFloor = currentFloor;
        this.direction = Direction.NOT_MOVING;
    }

    @Override
    public void pressButton(Button button) {
        if (!buttons.contains(button)) {
            throw new IllegalArgumentException("Button not registered on this panel");
        }
        button.press();
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
