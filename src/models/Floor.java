package ElevatorSystem.models;

import ElevatorSystem.models.panels.ExternalPanel;

public class Floor {
    private final int floorNumber;
    private final ExternalPanel externalPanel;

    public Floor(int floorNumber, ExternalPanel externalPanel) {
        this.floorNumber = floorNumber;
        this.externalPanel = externalPanel;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public ExternalPanel getExternalPanel() {
        return externalPanel;
    }
}
