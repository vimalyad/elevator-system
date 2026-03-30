package ElevatorSystem.sensors;


import ElevatorSystem.models.elevator.IElevator;

import java.util.function.Consumer;

public class WeightSensor {
    private final int maxCapacity;
    private int currentWeight;
    private IElevator elevator;
    private final Consumer<IElevator> onOverload;

    public WeightSensor(int maxCapacity, Consumer<IElevator> onOverload) {
        this.maxCapacity = maxCapacity;
        this.currentWeight = 0;
        this.onOverload = onOverload;
    }

    public void attachElevator(IElevator elevator) {
        this.elevator = elevator;
    }

    public void addWeight(int weight) {
        if (this.currentWeight + weight > maxCapacity) {
            if (onOverload != null && elevator != null) {
                onOverload.accept(elevator);
            }
        } else {
            this.currentWeight += weight;
        }
    }

    public void removeWeight(int weight) {
        this.currentWeight = Math.max(0, this.currentWeight - weight);
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean canAccept(int estimatedWeight) {
        return (this.currentWeight + estimatedWeight) <= maxCapacity;
    }
}