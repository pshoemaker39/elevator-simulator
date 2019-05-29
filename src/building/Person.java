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

public class Person {

    private String id;
    private int desiredFloor;
    private Direction direction;
    private long waitStart;
    private long rideEnd;


    public Person(String idIn, int floorNum) {
        id = idIn;
        desiredFloor = floorNum;
    }

    public String getId() {
        return id;
    }



    public int getDesiredFloor() {
        return desiredFloor;
    }


    public void setRideEnd(long rideEnd) {
        this.rideEnd = rideEnd;
    }

    public void setWaitStart(long waitStart) {
        this.waitStart = waitStart;
    }
}
