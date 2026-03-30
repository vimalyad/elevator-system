package ElevatorSystem.models.buttons;

import ElevatorSystem.enums.ButtonType;

public class SimpleButton implements Button {

    private final ButtonType type;
    private final String displayName;
    private boolean isPressed;
    private final Runnable action;

    public SimpleButton(ButtonType type, String displayName, Runnable action) {
        this.type = type;
        this.displayName = displayName;
        this.action = action;
        this.isPressed = false;
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }

    @Override
    public void press() {
        this.isPressed = true;
        execute();
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ButtonType getType() {
        return type;
    }

    @Override
    public void execute() {
        action.run();
    }
}
