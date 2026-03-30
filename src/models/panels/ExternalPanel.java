package ElevatorSystem.models.panels;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.models.buttons.Button;
import ElevatorSystem.models.elevator.IElevator;

import java.util.List;

public class ExternalPanel implements Panel {
    private final int panelId;
    private final Direction direction;
    private final List<IElevator> elevators;
    private final List<Button> buttons;

    public ExternalPanel(int panelId, Direction direction, List<IElevator> elevators, List<Button> buttons) {
        this.panelId = panelId;
        this.direction = direction;
        this.elevators = elevators;
        this.buttons = buttons;
    }

    public int getPanelId() {
        return panelId;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<IElevator> getElevators() {
        return elevators;
    }

    public void pressButton(Button button) {
        if (!buttons.contains(button)) {
            throw new IllegalArgumentException("Button not registered on this panel");
        }
        button.press();
    }
}
