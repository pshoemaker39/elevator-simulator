package elevator;

import building.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestOptimizer implements PathAlgorithm {

    private Elevator mostEfficientElevator;

    private HashMap<Request, Integer> pendingRequests = new HashMap<>();

    boolean hasIdentifiedElevator = false;

    private ArrayList<Elevator> potentialElevators = new ArrayList<>();


    //testing purposes - remove
//    public void setTargetElevator(Elevator elevator) {
//        this.mostEfficientElevator = elevator;
//    }

    public HashMap<Request, Integer> getPendingRequests() {
        return this.pendingRequests;
    }

    public void removeRequest(Request request) {
    }

    public void addRequestToQueue(Request request) {
        this.pendingRequests.put(request, 0);
    }

    private void setHasIdentifiedElevator(boolean state) {
        this.hasIdentifiedElevator = state;
    }

    public Elevator getMostEfficientElevator() {

        return this.mostEfficientElevator;
    }

    private boolean getHasIdentifiedElevator() {
        return this.hasIdentifiedElevator;
    }

    private void setMostEfficientElevator(Elevator elevator, String phase) {
       // System.out.println("MEE: "+elevator.getId()+ " Phase: "+phase);
        setHasIdentifiedElevator(true);
        this.mostEfficientElevator = elevator;
    }

    private void removeElevatorFromPotential(Elevator elevator) {
        this.potentialElevators.remove(this.potentialElevators.indexOf(elevator));
    }

    private void addElevatorToPotential(Elevator elevator) {
        this.potentialElevators.add(elevator);
    }

////// end supporting interface methods ///////

    private void selectLeastBusyElevator() {
        Integer minReqs = null;
        Elevator targetElevator = null;

        if(!getHasIdentifiedElevator()) {
            for (Elevator elevator : potentialElevators) {
                Integer reqSum;
                reqSum = elevator.floorRequests.size();
                reqSum = reqSum + elevator.riders.size();

                if(minReqs == null) {
                    targetElevator = elevator;
                   //System.out.println("El: "+elevator.getId()+" R: "+reqSum);
                } else {
                    if(reqSum <= minReqs) {
                        //System.out.println("El: "+elevator.getId()+" R: "+reqSum);
                        targetElevator = elevator;
                    }
                }
            }
            setMostEfficientElevator(targetElevator, "Select Least Busy");
        }

    }

    private void checkFloor(int target, Direction direction) {
        for (Elevator elevator : potentialElevators) {
            if((elevator.getCurrentFloor() == target) && (elevator.getDirection() == direction) && (!getHasIdentifiedElevator())) {
                setMostEfficientElevator(elevator, "Check floor");
            }
        }
    }

    private void checkExistingDestinations(int target, Direction direction) {

        for (Elevator elevator : potentialElevators) {
            if(!getHasIdentifiedElevator()) {

                for(Request request : elevator.floorRequests) {
                    if((request.getRequestEnd() == target) && (elevator.getDirection() == direction)) {
                        setMostEfficientElevator(elevator, "Check Existing Destinations");
                    } else {
                        for(Person rider : elevator.riders) {
                            if((rider.getDesiredFloor() == target) && (elevator.getDirection() == direction)) {
                                setMostEfficientElevator(elevator, "Check Existing Destinations");
                            }
                        }
                    }
                }
            }
        }

    }

    private void checkPassiveDestinations(int target, Direction direction) {
        for(Elevator elevator : potentialElevators) {
            Integer pathMin = null;
            Integer pathMax = null;

            if((elevator.floorRequests.size()>0) && (!getHasIdentifiedElevator())) {
                for(Request request : elevator.floorRequests) {
                    if(pathMin == null) {
                        pathMin = request.getRequestStart();
                    } else {
                        if(request.getRequestStart()<pathMin) {
                            pathMin = request.getRequestStart();
                        }
                    }

                    if(pathMax == null) {
                        pathMax = request.getRequestStart();
                    } else {
                        if(request.getRequestStart()>pathMax) {
                            pathMax = request.getRequestStart();
                        }
                    }
                }
            }


            if((elevator.riders.size()>0) && (!getHasIdentifiedElevator())) {
                for(Person rider : elevator.riders) {
                    if(pathMin == null) {
                        pathMin = rider.getDesiredFloor();
                    } else {
                        if(rider.getDesiredFloor()<pathMin) {
                            pathMin = rider.getDesiredFloor();
                        }
                    }

                    if(pathMax == null) {
                        pathMax = rider.getDesiredFloor();
                    } else {
                        if(rider.getDesiredFloor()>pathMax) {
                            pathMax = rider.getDesiredFloor();
                        }
                    }
                }
            }



            if((pathMin != null)&&(pathMax != null)) {
                if(
                        (target >= pathMin) && (target <= pathMax) && (elevator.getDirection() == direction)
                ) {
                    setMostEfficientElevator(elevator, "Check Passive Destinations");
                }
            }





        }
    }

    private void checkIdles() {

        for(Elevator elevator : potentialElevators) {
            if((elevator.getIdleTime()>0) && (!getHasIdentifiedElevator())) {
                setMostEfficientElevator(elevator, "Check Idles");
            }
        }

    }

    public void evaluateRequest(Request request, ArrayList<Elevator> elevators) {
        for (Elevator elevator : elevators) {
            addElevatorToPotential(elevator);
        }

        checkFloor(request.getRequestStart(),request.getRequestDirection());
        checkExistingDestinations(request.getRequestStart(),request.getRequestDirection());
        checkPassiveDestinations(request.getRequestStart(),request.getRequestDirection());
        checkIdles();
        selectLeastBusyElevator();

    }
}
