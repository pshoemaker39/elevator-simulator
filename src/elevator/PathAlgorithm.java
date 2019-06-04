package elevator;

import java.util.HashMap;

interface PathAlgorithm {

    Elevator mostEfficientElevator = null;

    //will track requests and number of cycles without a match
    HashMap<Request, Integer> pendingRequests = new HashMap<>();

    Elevator getMostEfficientElevator();

    HashMap<Request, Integer> getPendingRequests();

    void removeRequest(Request request);

    void evaluateRequest(Request request);

    boolean hasIdentifiedElevator();


}
