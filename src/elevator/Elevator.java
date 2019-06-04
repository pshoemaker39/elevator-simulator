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
import building.Logger;
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
    private Request requestFromIdle;
    private long idleTime;
    private boolean isExchangingPassengers;
    private long remainingDoorTime;
    private long totalTime;

    public static Integer DOOR_TIME;
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

    private void returnHome() {
        if(getCurrentFloor() != 1) {
            decrementCurrentFloor();
        }
    }

    private void incrementIdleTimer(long time) {
        if(getCurrentFloor() == 1) {
            setIdleTime(0);
        } else {
            if(getIdleTime() == getMaxIdleTime()) {
                //returnHome();
            } else {
                setIdleTime(getIdleTime() + time);
            }
        }
    }

    private void incrementCurrentFloor() {
        Logger.getInstance().elevatorMovement(Integer.toString(getId()), Integer.toString(getCurrentFloor()), Integer.toString(getCurrentFloor()+1));
        setCurrentFloor(getCurrentFloor() +1);
    }

    private void decrementCurrentFloor() {
        Logger.getInstance().elevatorMovement(Integer.toString(getId()), Integer.toString(getCurrentFloor()), Integer.toString(getCurrentFloor()-1));
        setCurrentFloor(getCurrentFloor() -1);
    }

    private void beginPassengerExchange() {
        Logger.getInstance().elevatorArrival(Integer.toString(getId()), Integer.toString(getCurrentFloor()));
        ElevatorDisplay.getInstance().openDoors(getId());
        Logger.getInstance().elevatorDoorStatusChange(Integer.toString(getId()), "Open");
        setPassengerExchangeStatus(true);
        setRemainingDoorTime(getDoorTime());
        floorToElevator();
        elevatorToFloor();
        stops.remove(stops.indexOf(getCurrentFloor()));

        riders.forEach((rider)->{
            Logger.getInstance().riderEnteredRequest(Integer.toString(getId()),Integer.toString(rider.getDesiredFloor()));
            if(!stops.contains(rider.getDesiredFloor())) {
                stops.add(rider.getDesiredFloor());
            }
        });

        Collections.sort(stops);
    }

    public ArrayList listFloorRequests() {
        ArrayList<Integer> requestedFloors = new ArrayList<>();
        floorRequests.forEach((requestor)->{
            Integer req = requestor.getRequestStart();
            if(!requestedFloors.contains(req)) {
                requestedFloors.add(req);
            }
        });

        return requestedFloors;
    }

    public ArrayList listRiderRequests() {
        ArrayList<Integer> riderRequestedFloors = new ArrayList<>();
        riders.forEach((rider)->{
            Integer req = rider.getDesiredFloor();
            if(!riderRequestedFloors.contains(req)) {
                riderRequestedFloors.add(req);
            }
        });

        return riderRequestedFloors;
    }

    private void getNextFloorRequest() {
        Request next = floorRequests.get(0);
        setTargetFloor(next.getRequestStart());
        setDirection(next.getRequestDirection());
    }

    public ArrayList listRiders() {
        ArrayList<String> ridersList = new ArrayList<>();
        riders.forEach((rider)->{
            ridersList.add(rider.getId());
        });
        return ridersList;
    }


    private void floorToElevator() {
        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(getCurrentFloor());
        int waiting = flr.getWaitingPeople(getDirection()).size();

        if(waiting > 0) {


            for(int i = 0; i < waiting; i++) {
                Person waitingPerson = flr.getWaitingPeople(getDirection()).get(i);

                waitingPerson.setWaitEnd();
                waitingPerson.setRideStart();

                riders.add(waitingPerson);
                flr.removePersonFromFloor(waitingPerson);

                for(int f = 0; f <floorRequests.size(); f++) {
                    Request r = floorRequests.get(f);
                    if(r.getRequestStart() == getCurrentFloor()) {
                        floorRequests.remove(r);
                    }

                }

                Logger.getInstance().personLeavingFloor(waitingPerson.getId(), Integer.toString(getCurrentFloor()));
                Logger.getInstance().personEnteringElevator(waitingPerson.getId(), Integer.toString(getId()));

            }

            //TODO remove floor request here: see test 1, floor request should disappear here

//            for (int i = 0; i < flr.getWaitingPeople(getDirection()).size(); i++) {
//                Person person = flr.getWaitingPeople(getDirection()).get(i);
//                riders.add(person);
//                System.out.println("**********************");
//            }


//            flr.getWaitingPeople(getDirection()).forEach((person) -> {
//                //move to elevator
//                riders.add(person);
//                //remove from floor
//
//
//
//                flr.removePersonFromFloor(person);
//
//            });
        }


        //remove from floor request queue
//        floorRequests.forEach((waiter)->{
//            if(waiter.getRequestStart() == getCurrentFloor()) {

//                floorRequests.remove(floorRequests.indexOf(waiter));
//            }
//        });
    }

    private void elevatorToFloor() {
        Building bld = Building.getInstance();
        Floor flr = bld.getFloor(getCurrentFloor());

        if(getRidersSize()>0) {
            for(int i = 0; i < getRidersSize(); i++) {
                Person rider = this.riders.get(i);
                if(rider.getDesiredFloor() == getCurrentFloor()){

                    rider.setRideEnd();

                    Logger.getInstance().personLeavingElevator(rider.getId(), Integer.toString(getId()));
                    Logger.getInstance().personEnteringFloor(rider.getId(), Integer.toString(getCurrentFloor()));


                    flr.addArrivedPerson(rider);
                    this.riders.remove(i);

                }
            }
        }

    }


    private void endPassengerExchange() {
        Logger.getInstance().elevatorDoorStatusChange(Integer.toString(getId()), "Close");
        ElevatorDisplay.getInstance().closeDoors(getId());
        setPassengerExchangeStatus(false);
        setRemainingDoorTime(0);
    }

    private void decrementDoorTime(long time) {
        setRemainingDoorTime(getRemainingDoorTime() - time);
    }



    private void idleLogic(long time) {

        //should I still be idle
        if(getStopQueueSize() > 0) {
            getNextFloorRequest();
        } else if (getRidersSize() > 0) {
            System.out.println("Elevator "+getId()+" is idle but has riders, something is wrong");
        }

        //how long have I been idle
        else if(getIdleTime() == 10000) {
            returnHome();
        } else {
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


    public void addStopToQueue(Request request) {
        if(floorRequests.size() == 0) {
            requestFromIdle = request;
            floorRequests.add(request);
        } else {
            floorRequests.add(request);
        }


        if(getStopQueueSize() == 0) {
            setDirection(request.getRequestDirection());
        }
        if(!stops.contains(request.getRequestStart())) {
            stops.add(request.getRequestStart());
        }
        setIdleTime(0);
    }

    private void removeStopFromQueue(int floor) {
        stops.remove(floor);
    }

    private int getStopQueueSize() {
        return stops.size();
    }


    public void move(long time) {

        this.totalTime = this.totalTime+time;

        //check for stops
        //exchange passengers
        //index floors
        //increment idle time

        ElevatorDisplay.getInstance().updateElevator(getId(), getCurrentFloor(), getRidersSize(), matchDirection(getDirection()));



        if((getIdleTime() > MAX_IDLE_TIME)) {
            if((getCurrentFloor() == 1)) {
                setDirection(Direction.IDLE);
            } else {
                decrementCurrentFloor();
            }
        }

        else if(stops.contains(getCurrentFloor())) {
            if(getIdleTime() > 9000) {
               setDirection(Direction.IDLE);
                stops.remove(stops.indexOf(getCurrentFloor()));
            } else {
                if(getCurrentFloor() == requestFromIdle.getRequestStart()) {
                    setDirection(requestFromIdle.getRequestDirection());
                }
                beginPassengerExchange();
            }
        }

        else if(isExchangingPassengers) {
            //is exchanging
            exchangeLogic(time);
        }


        else if(getStopQueueSize() == 0) {
            //is idle
            //updateRequestQueue
            idleLogic(time);
        }

        else if (getStopQueueSize() > 0){

            if(getRidersSize() == 0) {
                //System.out.println("Has queue, no but riders");

                if(stops.get(0) == getCurrentFloor()) {
                    System.out.println("At current floor, begin exchange");
                    beginPassengerExchange();
                }
                if(stops.get(0)>getCurrentFloor()){
                    setDirection(Direction.UP);
                    incrementCurrentFloor();
                } else {
                    setDirection(Direction.DOWN);
                    decrementCurrentFloor();
                }
            }

            else if(getDirection() == Direction.UP) {
                incrementCurrentFloor();
            } else {
                decrementCurrentFloor();
            }

        }
    }
}
