package ElevatorSystem.models;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.enums.RequestType;

public class Request {
    private final int sourceFloor;
    private final int destinationFloor;
    private final Direction direction;
    private final RequestType requestType;

    public Request(int sourceFloor, int destinationFloor, Direction direction, RequestType requestType) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
        this.requestType = requestType;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
