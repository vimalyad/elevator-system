package ElevatorSystem.factories;

import ElevatorSystem.controllers.ElevatorController;
import ElevatorSystem.enums.ButtonType;
import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.models.Building;
import ElevatorSystem.models.Door;
import ElevatorSystem.models.Floor;
import ElevatorSystem.models.buttons.Button;
import ElevatorSystem.models.buttons.SimpleButton;
import ElevatorSystem.models.elevator.Elevator;
import ElevatorSystem.models.elevator.IElevator;
import ElevatorSystem.models.panels.ExternalPanel;
import ElevatorSystem.models.panels.InsidePanel;
import ElevatorSystem.observers.DisplayObserver;
import ElevatorSystem.observers.ElevatorObserver;
import ElevatorSystem.observers.LoggingObserver;
import ElevatorSystem.observers.SoundObserver;
import ElevatorSystem.sensors.WeightSensor;
import ElevatorSystem.strategies.DispatchStrategy;
import ElevatorSystem.strategies.NearestCarStrategy;
import ElevatorSystem.strategies.SCANStrategy;
import ElevatorSystem.system.ElevatorSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElevatorSystemFactory {

    public static final String STRATEGY_NEAREST_CAR = "NEAREST_CAR";
    public static final String STRATEGY_SCAN = "SCAN";

    public static ElevatorSystem create(int numberOfElevators,
                                        int minFloor,
                                        int maxFloor,
                                        String strategyName) {
        DispatchStrategy strategy = resolveStrategy(strategyName);
        List<ElevatorObserver> observers = buildObservers();
        return assemble(numberOfElevators, minFloor, maxFloor, strategy, observers);
    }

    public static ElevatorSystem create(int numberOfElevators,
                                        int minFloor,
                                        int maxFloor,
                                        DispatchStrategy strategy,
                                        List<ElevatorObserver> observers) {
        return assemble(numberOfElevators, minFloor, maxFloor, strategy, observers);
    }

    static DispatchStrategy resolveStrategy(String strategyName) {
        if (strategyName == null) return buildNearestCarStrategy();

        return switch (strategyName.trim().toUpperCase()) {
            case STRATEGY_SCAN -> buildScanStrategy();
            case STRATEGY_NEAREST_CAR -> buildNearestCarStrategy();
            default -> buildNearestCarStrategy();
        };
    }

    static DispatchStrategy buildNearestCarStrategy() {
        return new NearestCarStrategy();
    }

    static DispatchStrategy buildScanStrategy() {
        return new SCANStrategy();
    }

    static List<ElevatorObserver> buildObservers() {
        List<ElevatorObserver> observers = new ArrayList<>();
        observers.add(buildDisplayObserver());
        observers.add(buildLoggingObserver());
        observers.add(buildSoundObserver());
        return observers;
    }

    static ElevatorObserver buildDisplayObserver() {
        return new DisplayObserver();
    }

    static ElevatorObserver buildLoggingObserver() {
        return new LoggingObserver();
    }

    static ElevatorObserver buildSoundObserver() {
        return new SoundObserver();
    }

    static Elevator buildElevator(int id, int minFloor, int maxFloor, Runnable onAlarm, List<ElevatorObserver> observers) {
        Door door = buildDoor(id);
        InsidePanel panel = buildInsidePanel(minFloor, maxFloor, door, onAlarm);

        Consumer<IElevator> onOverload = (elevator) -> {
            observers.forEach(o -> o.onOverloadWarning(elevator));
        };

        WeightSensor sensor = new WeightSensor(800, onOverload);

        return new Elevator(id, minFloor, maxFloor, panel, door, sensor);
    }

    static Door buildDoor(int doorId) {
        return new Door(doorId);
    }

    static InsidePanel buildInsidePanel(int minFloor, int maxFloor, Door door, Runnable onAlarm) {
        List<Button> buttons = new ArrayList<>(buildFloorButtons(minFloor, maxFloor));
        buttons.add(buildOpenDoorButton(door));
        buttons.add(buildCloseDoorButton(door));
        buttons.add(buildAlarmButton(onAlarm));
        return new InsidePanel(buttons, minFloor);
    }

    static List<Button> buildFloorButtons(int minFloor, int maxFloor) {
        List<Button> buttons = new ArrayList<>();
        for (int floor = minFloor; floor <= maxFloor; floor++) {
            final int target = floor;
            buttons.add(new SimpleButton(
                    ButtonType.FLOOR,
                    "Floor " + target,
                    () -> System.out.println("[PANEL] Floor " + target + " pressed")
            ));
        }
        return buttons;
    }

    static Button buildOpenDoorButton(Door door) {
        return new SimpleButton(
                ButtonType.OPEN_DOOR,
                "Open",
                () -> door.setDoorStatus(DoorStatus.OPEN)
        );
    }

    static Button buildCloseDoorButton(Door door) {
        return new SimpleButton(
                ButtonType.CLOSE_DOOR,
                "Close",
                () -> door.setDoorStatus(DoorStatus.CLOSED)
        );
    }

    static Button buildAlarmButton(Runnable onAlarm) {
        return new SimpleButton(
                ButtonType.ALARM,
                "Alarm",
                onAlarm
        );
    }

    static List<Floor> buildFloors(int minFloor, int maxFloor,
                                   List<IElevator> elevators) {
        List<Floor> floors = new ArrayList<>();
        for (int f = minFloor; f <= maxFloor; f++) {
            floors.add(buildFloor(f, elevators));
        }
        return floors;
    }

    static Floor buildFloor(int floorNumber, List<IElevator> elevators) {
        ExternalPanel panel = buildExternalPanel(floorNumber, elevators);
        return new Floor(floorNumber, panel);
    }

    static ExternalPanel buildExternalPanel(int floorNumber,
                                            List<IElevator> elevators) {
        List<Button> buttons = new ArrayList<>();
        buttons.add(buildUpButton(floorNumber));
        buttons.add(buildDownButton(floorNumber));
        return new ExternalPanel(floorNumber, Direction.UP, elevators, buttons);
    }

    static Button buildUpButton(int floorNumber) {
        return new SimpleButton(
                ButtonType.UP,
                "▲",
                () -> System.out.println("[EXTERNAL] UP pressed at floor " + floorNumber)
        );
    }

    static Button buildDownButton(int floorNumber) {
        return new SimpleButton(
                ButtonType.DOWN,
                "▼",
                () -> System.out.println("[EXTERNAL] DOWN pressed at floor " + floorNumber)
        );
    }

    static ElevatorController buildController(List<IElevator> elevators,
                                              DispatchStrategy strategy,
                                              List<ElevatorObserver> observers) {
        ElevatorController controller = new ElevatorController(elevators, strategy);
        observers.forEach(controller::registerObserver);
        return controller;
    }

    static Building buildBuilding(List<Floor> floors, List<Elevator> elevators) {
        return new Building(floors, elevators);
    }

    private static ElevatorSystem assemble(int numberOfElevators,
                                           int minFloor,
                                           int maxFloor,
                                           DispatchStrategy strategy,
                                           List<ElevatorObserver> observers) {

        ElevatorController[] controllerRef = new ElevatorController[1];

        Runnable onAlarm = () -> {
            if (controllerRef[0] != null) {
                controllerRef[0].handleAlarm();
            }
        };

        List<Elevator> elevators = new ArrayList<>();
        for (int id = 1; id <= numberOfElevators; id++) {
            elevators.add(buildElevator(id, minFloor, maxFloor, onAlarm, observers));
        }

        List<IElevator> iElevators = new ArrayList<>(elevators);
        List<Floor> floors = buildFloors(minFloor, maxFloor, iElevators);
        Building building = buildBuilding(floors, elevators);

        ElevatorController controller = buildController(iElevators, strategy, observers);
        controllerRef[0] = controller;

        return new ElevatorSystem(building, controller);
    }

    private ElevatorSystemFactory() {
    }
}