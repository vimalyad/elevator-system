package ElevatorSystem.movers;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.DoorStatus;
import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.elevator.IElevator;
import ElevatorSystem.observers.ElevatorObserver;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorMover {
    private final List<ElevatorObserver> observers;

    private final Map<IElevator, Set<Integer>> inboxes = new ConcurrentHashMap<>();

    private final ExecutorService motorPool;
    private volatile boolean isRunning = true;

    public ElevatorMover(List<IElevator> elevators, List<ElevatorObserver> observers) {
        this.observers = observers;
        this.motorPool = Executors.newFixedThreadPool(elevators.size());

        for (IElevator elevator : elevators) {
            inboxes.put(elevator, ConcurrentHashMap.newKeySet());
            motorPool.submit(() -> runMotorLoop(elevator));
        }
    }

    public void addDestination(IElevator elevator, int destination) {
        inboxes.get(elevator).add(destination);
    }

    private void runMotorLoop(IElevator elevator) {
        Set<Integer> targetFloors = inboxes.get(elevator);

        while (isRunning) {
            try {
                if (elevator.getElevatorStatus() == ElevatorStatus.NOT_AVAILABLE) {
                    Thread.sleep(500);
                    continue;
                }

                if (targetFloors.isEmpty()) {
                    if (elevator.getElevatorStatus() != ElevatorStatus.IDLE) {
                        elevator.setElevatorStatus(ElevatorStatus.IDLE);
                        elevator.setDirection(Direction.NOT_MOVING);
                    }
                    Thread.sleep(200);
                    continue;
                }

                int currentFloor = elevator.getCurrentFloor();
                int nextStop = calculateNextStop(currentFloor, elevator.getDirection(), targetFloors);

                if (currentFloor < nextStop) {
                    moveOneFloor(elevator, Direction.UP, currentFloor + 1);
                } else if (currentFloor > nextStop) {
                    moveOneFloor(elevator, Direction.DOWN, currentFloor - 1);
                }

                if (targetFloors.contains(elevator.getCurrentFloor())) {
                    targetFloors.remove(elevator.getCurrentFloor());
                    openDoors(elevator);
                    Thread.sleep(2000);
                    closeDoors(elevator);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private int calculateNextStop(int currentFloor, Direction currentDir, Set<Integer> targetFloors) {
        if (currentDir == Direction.UP || currentDir == Direction.NOT_MOVING) {
            Integer nextUp = targetFloors.stream().filter(f -> f > currentFloor).sorted().findFirst().orElse(null);
            if (nextUp != null) return nextUp;
        }

        Integer nextDown = targetFloors.stream().filter(f -> f < currentFloor).sorted(Comparator.reverseOrder()).findFirst().orElse(null);
        if (nextDown != null) return nextDown;

        return currentFloor;
    }

    private void moveOneFloor(IElevator elevator, Direction dir, int nextFloor) throws InterruptedException {
        elevator.setDirection(dir);
        elevator.setElevatorStatus(dir == Direction.UP ? ElevatorStatus.UP : ElevatorStatus.DOWN);

        Thread.sleep(1000);

        if (elevator.getElevatorStatus() != ElevatorStatus.NOT_AVAILABLE) {
            elevator.setCurrentFloor(nextFloor);
            observers.forEach(o -> o.onFloorArrival(elevator, nextFloor));
        }
    }

    private void openDoors(IElevator elevator) {
        elevator.getDoor().setDoorStatus(DoorStatus.OPEN);
        elevator.setDirection(Direction.NOT_MOVING);
        observers.forEach(o -> o.onDoorStatusChange(elevator));
    }

    private void closeDoors(IElevator elevator) {
        elevator.getDoor().setDoorStatus(DoorStatus.CLOSED);
        observers.forEach(o -> o.onDoorStatusChange(elevator));
    }

    public void emergencyStop(IElevator elevator) {
        elevator.setElevatorStatus(ElevatorStatus.NOT_AVAILABLE);
        elevator.setDirection(Direction.NOT_MOVING);
        elevator.getDoor().setDoorStatus(DoorStatus.OPEN);
    }

    public void shutdown() {
        isRunning = false;
        motorPool.shutdownNow();
    }
}