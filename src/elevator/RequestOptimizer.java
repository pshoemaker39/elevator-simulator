package elevator;

import java.util.ArrayList;

public class RequestOptimizer implements PathAlgorithm {

    private Elevator mostEfficientElevator;

    private ArrayList<Request> pendingRequests = new ArrayList<>();

    public Elevator getMostEfficientElevator() {
        return this.mostEfficientElevator;
    }

    public ArrayList<Request> getPendingRequests() {
        return this.pendingRequests;
    }

    public void removeRequest(Request request) {


    }

    public void addRequestToQueue(Request request) {

    }

    public void evaluateRequest(Request request) {

    }

    public boolean hasIdentifiedElevator() {

        return true;
    }

    //testing purposes
    public void setTargetElevator(Elevator elevator) {
        this.mostEfficientElevator = elevator;
    }

    public Elevator determingMostEfficientElevator(Request request) {

        return getMostEfficientElevator();
    }



}
