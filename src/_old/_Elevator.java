package _old;

// Elevators will travel up and down to the various floors of the building
// Elevators will hold Person objects. Person objects enter and exit the elevator.
// Elevators have a max person count
// Elevators have a direction
// Elevators have a time in ms per floor value (speed)
// When arriving at a floor the elevator doors should open then remain open for some const value in ms then close
// When an elevator stops at a floor any person on that floor who has requested an elevator in that direction should
// Elevators should maintain a list of the floors they need to visit, in order
// Idle elevators should return to floor 1 after a const time


import building.Building;
import building.Floor;
import building.Person;
import elevator.Direction;
import elevator.Request;
import gui.ElevatorDisplay;

import java.util.ArrayList;

//elevators changes its own state

public class _Elevator {

    public int id;
    private Direction direction;
    private int currentFloor;
    private int targetFloor;
    private final ArrayList<Request> floorRequests = new ArrayList<>();
    private final ArrayList<Person> riders = new ArrayList<>();
    private long idleTime;
    private boolean isExchangingPassengers;
    private long remainingDoorTime;
    private long totalTime;



    //1 sec per inital imp reqs
    //private static final int FLOOR_TIME = 1000;
    //2 sec per inital imp reqs
    public static Integer DOOR_TIME;
    //15 sec max idle
    public static Integer MAX_IDLE_TIME;
    public static Integer MAX_PERSONS_PER_ELEVATOR;


    public _Elevator(int idIn) {
        this.id = idIn;
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.totalTime = 0;
        this.idleTime = 0;
        this.isExchangingPassengers = false;
    }

    public void addFloorRequest(Request r) {
        System.out.println("+++++++++++++");
        this.floorRequests.add(r);
    }

    private int getDoorTime() {
        return this.DOOR_TIME;
    }


    private int getTargetFloor() {
        return this.targetFloor;
    }

    private int getCurrentFloor() {
        return this.currentFloor;
    }

    private void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    private Direction getDirection() {
        return this.direction;
    }

    public int getId() {
        return this.id;
    }

    private void setDirection(Direction d) {
        this.direction = d;
    }

    private int getMaxIdleTime() {
        return this.MAX_IDLE_TIME;
    }

    private void setIdleTime(long time) {
        this.idleTime = time;
    }

    private long getIdleTime() {
        return this.idleTime;
    }

    private int getRequestFromIdle() {
        return floorRequests.get(0).getRequestStart();
    }

    private void setTargetFloor(int target) {
        this.targetFloor = target;
        if(target >= getCurrentFloor()) {
            setDirection(Direction.UP);
        } else {
            setDirection(Direction.DOWN);
        }
    }

    private long getRemainingDoorTime(){
        return this.remainingDoorTime;
    }

    private void setRemainingDoorTime(long time) {
        this.remainingDoorTime = time;
    }

    private void setPassengerExchangeStatus(boolean status) {
        this.isExchangingPassengers = status;
    }

    private boolean getPassengerExchangeStatus(){
        return this.isExchangingPassengers;
    }

    private int getRidersSize() {
        return this.riders.size();
    }

    private int getFloorRequestsSize() {
        return this.floorRequests.size();
    }

    private Person getRiderById(int id) {
        return this.riders.get(id);
    }

    private Request getFloorRequestById(int id) {
        return this.floorRequests.get(id);
    }

    /////////// END GETTERS AND SETTERS /////////////////////////

    private void removeFloorRequestById(int id) {
        this.floorRequests.remove(id);
    }

    private void removeRiderById(int id) {
        this.riders.remove(id);
    }

    private void addRider(Person p) {
        this.riders.add(p);
    }

    private ElevatorDisplay.Direction matchDirection(Direction direction) {
        if(direction == Direction.UP) {
            return ElevatorDisplay.Direction.UP;
        } else if (direction == Direction.DOWN) {
            return ElevatorDisplay.Direction.DOWN;
        } else {
            return ElevatorDisplay.Direction.IDLE;
        }
    }

    private boolean hasPendingRequests() {
        if ((getRidersSize() > 0) || ((getFloorRequestsSize() > 0))) {
            return true;
        } else {
            return false;
        }
    }

    private void returnHome() {
        setTargetFloor(1);
        setDirection(Direction.DOWN);
    }

    private void incrementIdleTimer(long time) {
        if(getCurrentFloor() == 1) {
            setIdleTime(0);
        } else {
            if(getIdleTime() == getMaxIdleTime()) {
                returnHome();
            } else {
                setIdleTime(getIdleTime() + time);
            }
        }
    }

    private boolean isCurrentFloorTargetFloor() {
        if(getCurrentFloor() == getTargetFloor()) {
            return true;
        } else {
            return false;
        }
    }

    private void incrementCurrentFloor() {
        setCurrentFloor(getCurrentFloor() +1);
    }

    private void decrementCurrentFloor() {
        setCurrentFloor(getCurrentFloor() -1);
    }

    private void indexFloors() {
        if(getTargetFloor() > getCurrentFloor()) {
            incrementCurrentFloor();
        } else {
            decrementCurrentFloor();
        }
    }

    private void beginPassengerExchange() {
        ElevatorDisplay.getInstance().openDoors(getId());
        setPassengerExchangeStatus(true);
        setRemainingDoorTime(getDoorTime());
        movePeopleFromElevatorToFloor();
        movePeopleFromFloorToElevator(getCurrentFloor(),getDirection());


    }


    private void checkFloorForRiderRequests() {
        if(getRidersSize()>0) {
            for(int i = 0; i<getRidersSize(); i++) {
                if(getCurrentFloor() == getRiderById(i).getDesiredFloor()) {
                    beginPassengerExchange();
                }
            }
        }

    }

    private void movePeopleFromElevatorToFloor() {
        //add person to floor

        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(getCurrentFloor());
        int riding = getRidersSize();

        for (int i = 0; i < riding; i++) {
            if(getRiderById(i).getDesiredFloor() == flr.getFloorNum()) {
                flr.addArrivedPerson(getRiderById(i));
                removeRiderById(i);
            }
        }

        if((getRidersSize() == 0) && (getFloorRequestsSize() == 0)) {
            setDirection(Direction.IDLE);
        }

    }



    private void createRiderRequest(Person p) {
        Request r = new Request(getCurrentFloor(), getDirection());
        r.setRequestEnd(p.getDesiredFloor());

    }

    private void movePeopleFromFloorToElevator(int floor, Direction direction) {
        //add request end



        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(floor);
        int waiting = flr.getWaitingPeople(direction).size();

        if(waiting>0) {
            // loop add and remove

            for (int i = 0; i < waiting; i++) {
                //add to rider queue
                addRider(flr.getWaitingPeople(direction).get(i));

                //create rider request
                createRiderRequest(flr.getWaitingPeople(direction).get(i));

                //remove person from floor
                flr.getWaitingPeople(direction).remove(i);

                //remove riders floor request
                for(int r = 0; r < getFloorRequestsSize(); r++) {
                    if(getFloorRequestById(r).getRequestStart() == getCurrentFloor()) {
                        removeFloorRequestById(r);
                    }
                }

            }
        }

        if(getTargetFloor() == getCurrentFloor()) {
            if(getDirection() == Direction.DOWN) {
                setDirection(Direction.UP);
            } else {
                setDirection(Direction.DOWN);
            }

            setTargetFloor(getFloorRequestById(0).getRequestEnd()+1);
        }
    }

    private void endPassengerExchange() {
        ElevatorDisplay.getInstance().closeDoors(getId());
        setPassengerExchangeStatus(false);
        setRemainingDoorTime(0);
    }

    private void decrementDoorTime(long time) {
        setRemainingDoorTime(getRemainingDoorTime() - time);
    }

    private boolean isFloorRequested() {
        //get floor and check requests

        if(getCurrentFloor() == getTargetFloor()) {

            return true;
        } else {
            return false;
        }
    }

    private boolean floorHasRequestsInCurrentDirection() {
        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(getCurrentFloor());
        if(flr.getWaitingPeople(getDirection()).size()>0) {
            return true;
        } else {
            return false;
        }
    }

    public void move(long time) {
        this.totalTime = this.totalTime+time;

        ElevatorDisplay.getInstance().updateElevator(getId(), getCurrentFloor(), getRidersSize(), matchDirection(getDirection()));

        if(getId()==1) {

            System.out.println("-------------------------------------- ELEVATOR " + getId() + " Activity-------------------------------------");
//            System.out.println("Total: "+this.totalTime);
//            System.out.println("Door time remaining: "+this.remainingDoorTime);
//            System.out.println("Exchanging: "+this.isExchangingPassengers);
//            System.out.println("Current Floor: "+getCurrentFloor());
//            System.out.println("Riders: "+this.riders.size());
            System.out.println("Floor Requests: " + this.floorRequests.size());
            System.out.println("Direction: " + getDirection());
            System.out.println("Target: " + this.targetFloor);
//            System.out.println("Idle time: "+this.idleTime);
//            System.out.println("\n");
            System.out.println("*****************************************************************************************************************");
        }

        if((getIdleTime() == 10000) && (getCurrentFloor()==1)) {
            setDirection(Direction.IDLE);
            ElevatorDisplay.getInstance().shutdown();
        }



        /////////////////// BEGIN EVALUATING PASSENGER EXCHANGES //////////////////////////////

        //check passenger exchange in progress
        if ((getRemainingDoorTime() > 0) && (getPassengerExchangeStatus() == true)) {
            //reduce remaining door time by 1 second
            decrementDoorTime(time);
            if(getRemainingDoorTime() == 0) {
                endPassengerExchange();
            }
        }

        /////////////////// END EVALUATING PASSENGER EXCHANGES //////////////////////////////



        //check idle
        else if(getDirection() == Direction.IDLE) {
            //is idle

            System.out.println("Elevator "+getId()+" is idle");

            //check for pending requests
            if(hasPendingRequests()) {
                //has pending requests

                //set target floor
                setTargetFloor(getRequestFromIdle());

                //evaluate current floor
                //check floor has requests waiting

                if(isCurrentFloorTargetFloor()) {

                    //current floor is target floor
                    beginPassengerExchange();

                    decrementDoorTime(time);

                } else {
                    //current floor is not target floor
                    indexFloors();
                }


            } else {
                //no pending requests, increment idle timer
                incrementIdleTimer(time);
            }
        }

        else if(getDirection() != Direction.IDLE) {
            //not idle

            checkFloorForRiderRequests();

            if(floorHasRequestsInCurrentDirection()) {
                beginPassengerExchange();
            }


            if(isFloorRequested()) {
                //floor is requested
                beginPassengerExchange();

            } else if(getPassengerExchangeStatus() && getRemainingDoorTime()>0) {
              decrementDoorTime(time);

            } else{
                //floor is not requested
                indexFloors();
            }
        }
    }
}
