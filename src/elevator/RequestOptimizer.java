package elevator;

import java.util.HashMap;

public class RequestOptimizer implements PathAlgorithm {

    private Elevator mostEfficientElevator;

    private HashMap<Request, Integer> pendingRequests = new HashMap<>();

    //testing purposes - remove
    public void setTargetElevator(Elevator elevator) {
        this.mostEfficientElevator = elevator;
    }

    public Elevator getMostEfficientElevator() {
        return this.mostEfficientElevator;
    }

    public HashMap<Request, Integer> getPendingRequests() {
        return this.pendingRequests;
    }

    public void removeRequest(Request request) {

        System.out.println("!!!!! remove request -- must be developed");

    }

    public void addRequestToQueue(Request request) {

    }

    public void evaluateRequest(Request request) {

    }

    public boolean hasIdentifiedElevator() {

        return true;
    }






}
