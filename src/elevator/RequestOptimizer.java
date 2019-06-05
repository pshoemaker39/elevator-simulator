package elevator;

import java.util.HashMap;

public class RequestOptimizer implements PathAlgorithm {

    private Elevator mostEfficientElevator;

    private HashMap<Request, Integer> pendingRequests = new HashMap<>();

    boolean hasIdentifiedElevator = false;

    //testing purposes - remove
//    public void setTargetElevator(Elevator elevator) {
//        this.mostEfficientElevator = elevator;
//    }

    public HashMap<Request, Integer> getPendingRequests() {
        return this.pendingRequests;
    }

    public void removeRequest(Request request) {

        System.out.println("!!!!! remove request -- must be developed");

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

    private void setMostEfficientElevator(Elevator elevator) {
        this.mostEfficientElevator = elevator;
    }

////// end interface methods ///////

    public void evaluateRequest(Request request) {

    }

}
