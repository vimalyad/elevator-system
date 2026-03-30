package ElevatorSystem.system;

import ElevatorSystem.controllers.ElevatorController;
import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.RequestType;
import ElevatorSystem.models.Building;
import ElevatorSystem.models.Request;
import ElevatorSystem.observers.ElevatorObserver;
import ElevatorSystem.strategies.DispatchStrategy;

public class ElevatorSystem {
    private final Building building;
    private final ElevatorController controller;

    public ElevatorSystem(Building building, ElevatorController controller) {
        this.building = building;
        this.controller = controller;
    }

    public void requestElevator(int sourceFloor, Direction direction) {
        Request request = new Request(
                sourceFloor,
                sourceFloor,
                direction,
                RequestType.EXTERNAL
        );
        controller.handleRequest(request);
    }

    public void requestFloor(int sourceFloor, int destinationFloor, Direction direction) {
        Request request = new Request(
                sourceFloor,
                destinationFloor,
                direction,
                RequestType.INTERNAL
        );
        controller.handleRequest(request);
    }

    public void registerObserver(ElevatorObserver observer) {
        controller.registerObserver(observer);
    }

    public void triggerAlarm() {
        controller.handleAlarm();
    }

    public void shutdown() {
        controller.shutdown();
    }

    public void setStrategy(DispatchStrategy strategy) {
        this.controller.setStrategy(strategy);
    }
}
