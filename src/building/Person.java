package building;

// Persons are created and placed on a floor of the building (do not necessarily start on 1st floor)
// Persons are created with a specific desired destination floor
// Persons call the elevator by sending a message to the elevator controller
// When an elevator arrives on the persons floor that is going in their desired direction,
//  the person should be moved from the floor to the elevator, where they become riders
// Once in the elevator a person will select a destination floor
// They will ride the elevator to their destination, then exit on their desired floor
// Each person will need to record their wait-time duration in seconds (req - enter)
// Each person will need to record their ride-time duration in seconds (enter - exit)


import elevator.Direction;

//TODO increase precision to a single decimal point
// issue with test2 results

public class Person {

    private String id;
    private int desiredFloor;
    private int startFloor;

    private long waitStart;
    private long waitEnd;
    private long rideStart;
    private long rideEnd;


    public Person(String idIn, int floorNum) {
        id = idIn;
        desiredFloor = floorNum;
    }

    public String getId() {
        return id;
    }

    public void setStartFloor(int floor) {
        this.startFloor = floor;
    }

    public int getDesiredFloor() {
        return desiredFloor;
    }

    public int getStartFloor() {
        return this.startFloor;
    }

    public void setWaitStart() {

        this.waitStart = System.currentTimeMillis();
        System.out.println(String.format("Wait time start set for %s at %s",getId(), waitStart));
    }

    public void setWaitEnd() {

        this.waitEnd = System.currentTimeMillis();
        System.out.println(String.format("Wait time end set for %s at %s",getId(), waitEnd));
    }

    public void setRideStart() {
        this.rideStart = System.currentTimeMillis();
    }

    public void setRideEnd() {
        this.rideEnd = System.currentTimeMillis();
    }

    private long getWaitStart() {
        return this.waitStart;
    }

    private long getWaitEnd() {
        return this.waitEnd;
    }

    private long getRideStart() {
        return this.rideStart;
    }

    private long getRideEnd() {
        return this.rideEnd;
    }

    public long getWaitTime() {
        return (getWaitEnd() - getWaitStart())/1000;
    }

    public long getRideTime() {
        return (getRideEnd() - getRideStart())/1000;
    }

    public Direction getDirection() {
        if(getRideStart()>getRideEnd()) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

    public long getTotalTime() {
        return getWaitTime()+getRideTime();
    }
}
