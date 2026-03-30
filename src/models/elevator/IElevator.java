package ElevatorSystem.models.elevator;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.ElevatorStatus;
import ElevatorSystem.models.Door;
import ElevatorSystem.sensors.WeightSensor;

public interface IElevator {
    int getElevatorId();

    int getCurrentFloor();

    void setCurrentFloor(int floor);

    Direction getDirection();

    void setDirection(Direction direction);

    ElevatorStatus getElevatorStatus();

    void setElevatorStatus(ElevatorStatus status);

    int getMinFloor();

    int getMaxFloor();

    Door getDoor();

    WeightSensor getWeightSensor();
}
