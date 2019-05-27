package building;

// Each floor of the building holds a list of persons that are waiting for an elevator
// Each floor of the building holds a list of persons that have completed their trip (done)

import elevator.Direction;

import java.util.ArrayList;

public class Floor {

    private int floorNum;
    private ArrayList<Person> waitingForDescendingElevator = new ArrayList<>();
    private ArrayList<Person> waitingForAscendingElevator = new ArrayList<>();
    private ArrayList<Person> arrivedPerson = new ArrayList<>();

    public Floor (int fn) {
        floorNum = fn;
    }

    public void addArrivedPerson(Person p) {
        p.setRideEnd(System.currentTimeMillis());
        arrivedPerson.add(p);
        System.out.println("A person just arrived on floor: "+getFloorNum());
    }

    public void addPerson(Person p) {
        if (p == null) {
            System.out.println("Null person");
        }
        //System.out.println("Added person to queue. "+p.getId());

        //check direction person is waiting for
        if(p.getDesiredFloor()>getFloorNum()) {
            //person is going up
            this.waitingForAscendingElevator.add(p);
        } else {
            //person is going down
            this.waitingForDescendingElevator.add(p);
        }

    }

    public int getFloorNum() {
        return floorNum;
    }

    public ArrayList<Person> getWaitingPeople(Direction d) {
        //when passed a direction on the floor, filter the waiting people for that direction

        if(d == Direction.UP) {
            //return people waiting to go up
            return this.waitingForAscendingElevator;
        } else {
            //return people waiting to go down
            return this.waitingForDescendingElevator;
        }


    }


}
