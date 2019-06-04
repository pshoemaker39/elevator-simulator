package elevator;

// The elevator controller receives calls (requests) from the persons on a floor
// The ec determines which elevator should respond to that request
// The ec will instruct individual elevators to go to certain floors and respond to requests (not in scope for phase I?)


import building.Logger;

import java.util.ArrayList;

public class ElevatorController {

    private ArrayList<Elevator> elevators = new ArrayList<>();
    private static ElevatorController instance;

    public static ElevatorController getInstance() {
        if(instance == null) {
            instance = new ElevatorController();
        }
        return instance;
    }

    public ArrayList<Elevator>  getElevators() {
        return this.elevators;
    }

    public Elevator getElevator(int elevId) {
        return getElevators().get(elevId);
    }

    public void addRequestFromFloor(int elevId, Request request) {

        getElevator(elevId).addStopToQueue(request);

//        RequestOptimizer ro = new RequestOptimizer();
//
//        ro.evaluateRequest(request);
//
//       // ro.setTargetElevator(getElevators().get(0));
//
//        if(ro.hasIdentifiedElevator()) {
//            //if you can identify an elevator, add the request
//            Elevator elevator = ro.getMostEfficientElevator();
//            elevator.addStopToQueue(request);
//            ro.removeRequest(request);
//        }

        //if there are requests which have not been served, revisit them
//        if(ro.getPendingRequests().size()>0) {
//
//            ro.getPendingRequests().forEach((revisitedRequest, iterations)->{
//                ro.evaluateRequest(revisitedRequest);
//
//                if(ro.hasIdentifiedElevator()) {
//                    //if you can identify an elevator, add the request
//                    Elevator elevator = ro.getMostEfficientElevator();
//                    elevator.addStopToQueue(revisitedRequest);
//                    ro.removeRequest(request);
//                }
//            });
//        }
//
//        if(!ro.hasIdentifiedElevator()){
//            //if the most recent request was not served, add it to the queue
//            ro.addRequestToQueue(request);
//        }

        Logger.getInstance().floorRequestReceived(Integer.toString(elevId+1), request.getRequestDirection().toString(), Integer.toString(request.getRequestStart()));
    }

    public void moveElevators(long time) {

        for (int i = 0; i < getElevators().size(); i++) {
            getElevator(i).move(time);
        }
    }

    public void createElevator(int id) {
        Elevator elevator = new Elevator(id);
        elevators.add(elevator);
    }

}
