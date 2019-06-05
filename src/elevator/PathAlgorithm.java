package elevator;

import java.util.ArrayList;
import java.util.HashMap;

interface PathAlgorithm {

    Elevator mostEfficientElevator = null;

    //will track requests and number of cycles without a match
    HashMap<Request, Integer> pendingRequests = new HashMap<>();

    Elevator getMostEfficientElevator();

    HashMap<Request, Integer> getPendingRequests();

    boolean hasIdentifiedElevator = false;

    void removeRequest(Request request);

    void evaluateRequest(Request request, ArrayList<Elevator> elevators);


}
