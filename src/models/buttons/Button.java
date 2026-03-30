package ElevatorSystem.models.buttons;

import ElevatorSystem.enums.ButtonType;

public interface Button {
    boolean isPressed();

    void press();

    String getDisplayName();

    ButtonType getType();

    void execute();
}
