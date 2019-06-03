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
    }

    public void addPerson(Person p) {
        //System.out.println("Floor "+getFloorNum()+" has person added");

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
        return this.floorNum;
    }

    public void removePersonFromFloor(Person removePerson) {

        for(int i = 0; i < this.waitingForDescendingElevator.size(); i++) {
            if(this.waitingForDescendingElevator.get(i).equals(removePerson)) {
                this.waitingForDescendingElevator.remove(i);
            }
        }

        for(int i = 0; i < this.waitingForAscendingElevator.size(); i++) {
            if(this.waitingForAscendingElevator.get(i).equals(removePerson)) {
                this.waitingForAscendingElevator.remove(i);
            }
        }
    }

    public ArrayList<Person> getWaitingPeople(Direction d) {
        //when passed a direction on the floor, filter the waiting people for that direction

        //System.out.println("Direction: "+d);

        if(d == Direction.UP) {
            //return people waiting to go up
            return this.waitingForAscendingElevator;
        } else {
            //return people waiting to go down
            return this.waitingForDescendingElevator;
        }


    }


}
