package elevator;

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
import gui.ElevatorDisplay;

import java.util.*;

//elevators changes its own state

public class Elevator {

    public int id;
    private Direction direction;
    private int currentFloor;
    private int targetFloor;
    private final ArrayList<Request> floorRequests = new ArrayList<>();
    private final ArrayList<Person> riders = new ArrayList<>();
    private final ArrayList<Integer> stops = new ArrayList<>();
    private final Set<Integer> stopQueue = new HashSet<>();
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


    public Elevator(int idIn) {
        this.id = idIn;
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.totalTime = 0;
        this.idleTime = 0;
        this.isExchangingPassengers = false;
    }

    public void addFloorRequest(Request r) {

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
        if(getCurrentFloor() != 1) {
            stops.add(1);
            setDirection(Direction.DOWN);
        }
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


        //updateRiderRequests();


    }

    private void beginPassengerExchange() {
        ElevatorDisplay.getInstance().openDoors(getId());
        setPassengerExchangeStatus(true);
        setRemainingDoorTime(getDoorTime());
        floorToElevator();
        elevatorToFloor();
        stops.remove(stops.indexOf(getCurrentFloor()));

        riders.forEach((rider)->{
            if(!stops.contains(rider.getDesiredFloor())) {
                stops.add(rider.getDesiredFloor());
                System.out.println("Elevator "+getId()+" had a stop added to its queue");
            }
        });

        Collections.sort(stops);
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

    private void getNextFloorRequest() {
        Request next = floorRequests.get(0);
        setTargetFloor(next.getRequestStart());
        setDirection(next.getRequestDirection());
        //FIXME removing requests too early?
        //floorRequests.remove(0);
    }



    private void createRiderRequest(Person p) {
        Request r = new Request(getCurrentFloor(), getDirection());
        r.setRequestEnd(p.getDesiredFloor());

    }


    private void floorToElevator() {
        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(getCurrentFloor());
        int waiting = flr.getWaitingPeople(getDirection()).size();

        System.out.println(waiting+" people on floor "+flr.getFloorNum());

        if(waiting > 0) {

            for(int i = 0; i < waiting; i++) {
                Person waitingPerson = flr.getWaitingPeople(getDirection()).get(i);
                riders.add(waitingPerson);
                flr.removePersonFromFloor(waitingPerson);
                System.out.println("Rider add to elevator, remove from floor");
            }

            flr.getWaitingPeople(getDirection()).forEach((person) -> {
                //move to elevator
                riders.add(person);
                //remove from floor
                flr.removePersonFromFloor(person);

            });
        }
    }

    private void updateRiderRequests() {

        riders.forEach((rider)->{
            if(!stops.contains(rider.getDesiredFloor())) {
                stops.add(rider.getDesiredFloor());
            }
        });
        Collections.sort(stops);
        System.out.println("Sorted stops list: ");
        stops.forEach((stop)->{
            System.out.println("Floor: "+stop);
        });

        //TODO remove, testing value here
        setTargetFloor(stops.get(0));
        stops.remove(0);

        //TODO do nothing? loop through array list at each floor?
//        if(getDirection() == Direction.DOWN) {
//            setTargetFloor(stops.get(0));
//        }
    }

    private void elevatorToFloor() {
        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(getCurrentFloor());

        if(getRidersSize()>0) {
            for(int i = 0; i < getRidersSize(); i++) {
                Person rider = this.riders.get(i);
                if(rider.getDesiredFloor() == getCurrentFloor()){
                    flr.addArrivedPerson(rider);
                    this.riders.remove(i);
                    System.out.println("Rider add to floor, remove from elevator");
                }
            }
        }

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

    private void idleLogic(long time) {

        //should I still be idle
        if(getStopQueueSize() > 0) {
            System.out.println("Elevator "+getId()+" has a floor request");
            getNextFloorRequest();
        } else if (getRidersSize() > 0) {
            System.out.println("Elevator "+getId()+" is idle but has riders, something is wrong");
        }

        //how long have I been idle
        else if(getIdleTime() == 10000) {
            System.out.println("Elevator "+getId()+" called home");
            returnHome();
        } else {
            System.out.println("Elevator "+getId()+" idle time "+getIdleTime());
            incrementIdleTimer(time);
        }
    }

    private void exchangeLogic(long time) {
        //reduce remaining door time by 1 second
        decrementDoorTime(time);
        if(getRemainingDoorTime() == 0) {
            endPassengerExchange();
        }
    }

    private void travelLogic(long time) {

        //stops will not contain current floor after passenger exchange

        if(stops.contains(getCurrentFloor())) {
            beginPassengerExchange();
        } else {
            indexFloors();
        }

    }

    public void addStopToQueue(Request request) {
        if(getStopQueueSize() == 0) {
            System.out.println("Direction of elevator "+getId()+" was update to "+request.getRequestDirection()+" by a floor request");
            setDirection(request.getRequestDirection());
        }

        if(!stops.contains(request.getRequestStart())) {
            stops.add(request.getRequestStart());
            System.out.println("Elevator "+getId()+" had a stop added to its queue");
        }


    }

    private void removeStopFromQueue(int floor) {
        stops.remove(floor);
    }

    private int getStopQueueSize() {
        return stops.size();
    }

    private boolean floorRequiresExchange() {
        if(stops.contains(getCurrentFloor())) {
            beginPassengerExchange();
            return true;
        } else {
            return false;
        }
    }

    public void move(long time) {

        this.totalTime = this.totalTime+time;

        //check for stops
        //exchange passengers
        //index floors
        //increment idle time




        ElevatorDisplay.getInstance().updateElevator(getId(), getCurrentFloor(), getRidersSize(), matchDirection(getDirection()));

        System.out.println("Begin move loop, Elevator "+getId()+" stops "+ Arrays.toString(stops.toArray())+" ");


        if((getIdleTime() > MAX_IDLE_TIME) && (getCurrentFloor() == 1)){
            setDirection(Direction.IDLE);
        }

        if(stops.contains(getCurrentFloor())) {
            if(getIdleTime() > 9000) {
               setDirection(Direction.IDLE);
                stops.remove(stops.indexOf(getCurrentFloor()));
            } else {
                System.out.println("Exchange required on this floor");
                beginPassengerExchange();
            }
        }

        else if(isExchangingPassengers) {
            //is exchanging
            System.out.println("Elevator "+getId()+" is exchanging passengers");
            exchangeLogic(time);
        }


        else if(getStopQueueSize() == 0) {
            //is idle
            System.out.println("Elevator "+getId()+" is idle");
            //updateRequestQueue
            idleLogic(time);
        }

        else if (getStopQueueSize() > 0){
            //System.out.println("Elevator "+ getId()+" is not idle");
            System.out.println("Elevator "+getId()+" target floors: "+ Arrays.toString(stops.toArray())+" direction: "+getDirection());

            //updateRequestQueue
            //eval rider requests
            //eval floor requests
//
//            Building bld = Building.getInstance();
//            Floor flr = bld.getFloor(getCurrentFloor());
//
//            if(flr.getWaitingPeople(getDirection()).size() > 0) {
//                System.out.println("This floor ("+getCurrentFloor()+") has people waiting in my direction");
//                beginPassengerExchange();
//            } else {
//                System.out.println("This floor ("+getCurrentFloor()+") does not have people waiting in my direction");
//            }

            if(getRidersSize() == 0) {
                if(stops.get(0)>getCurrentFloor()){
                    incrementCurrentFloor();
                } else {
                    decrementCurrentFloor();
                }
            }

            else if(getDirection() == Direction.UP) {
                incrementCurrentFloor();
            } else {
                decrementCurrentFloor();
            }

            //update floor
            //do I have a rider request for this floor?
                //exchange passengers
            //do I have a floor request for this floor & direction?
                //exchnage passengers



        }
    }
}
