package building;

import elevator.ElevatorController;

import java.util.ArrayList;
import java.util.Arrays;

public class Logger {

    private long now = 0;
    private static Logger instance;

    public static Logger getInstance() {
        if(instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public long getNow() {
        return this.now;
    }

    public void updateNow(long time) {
        this.now = this.now+time;
    }

    private String getTimeStamp() {
        long second = (getNow() / 1000) % 60;
        long minute = (getNow() / (1000 * 60)) % 60;
        long hour = (getNow() / (1000 * 60 * 60)) % 24;

        String timestamp = String.format("%02d:%02d:%02d", hour, minute, second);

        return timestamp;
    }

    public void startupTime() {
        String output = String.format("%s Startup", getTimeStamp());
        System.out.println(output);
    }

    private String listRiders(String elevId) {
        ArrayList riders = ElevatorController.getInstance().getElevator(Integer.parseInt(elevId)-1).listRiders();
        String ridersList;

        if(riders.size()<1) {
            ridersList = "None";
        } else {
            ridersList = Arrays.toString(riders.toArray()).replace("[", "").replace("]", "");
        }


        String output = String.format("[Riders: %s]",ridersList);
        return output;
    }

    private String listRequests(String elevId) {
        ArrayList floors = ElevatorController.getInstance().getElevator(Integer.parseInt(elevId)-1).listFloorRequests();
        ArrayList riders = ElevatorController.getInstance().getElevator(Integer.parseInt(elevId)-1).listRiderRequests();
        String floorList;
        String riderList;

        if(floors.size()<1) {
            floorList = "None";
        } else {
            floorList = Arrays.toString(floors.toArray()).replace("[", "").replace("]", "");
        }

        if(riders.size()<1) {
            riderList = "None";
        } else {
            riderList = Arrays.toString(riders.toArray()).replace("[", "").replace("]", "");
        }

        String output = String.format("[Floor Requests: %s][Rider Requests: %s]",floorList,riderList);

        return output;
    }

    public void personCreated(String pid, String createdFloor, String direction, String destinationFloor) {
        String output = String.format("%s Person %s created on Floor %s, wants to go %s to Floor %s", getTimeStamp(), pid, createdFloor, direction, destinationFloor);
        System.out.println(output);
    }

    public void floorRequest(String pid, String direction, String createdFloor) {
        String output = String.format("%s Person %s presses %s button on Floor %s", getTimeStamp(), pid, direction, createdFloor);
        System.out.println(output);
    }

    public void floorRequestReceived(String elevId, String direction, String targetFloor) {
        String output = String.format("%s Elevator %s new floor request to go to floor %s, %s %s", getTimeStamp(), elevId, targetFloor, direction, listRequests(elevId));
        System.out.println(output);
    }

    public void riderEnteredRequest(String elevId, String targetFloor) {
        String output = String.format("%s Elevator %s rider request for Floor %s %s", getTimeStamp(), elevId, targetFloor, listRequests(elevId));
        System.out.println(output);
    }

    public void elevatorMovement(String elevId, String from, String to) {
        String output = String.format("%s Elevator %s moving from Floor %s to Floor %s", getTimeStamp(), elevId, from, to, listRequests(elevId));
        System.out.println(output);
    }

    public void elevatorArrival(String elevId, String floor) {
        String output = String.format("%s Elevator %s has stopped at Floor %s %s", getTimeStamp(), elevId, floor, listRequests(elevId));
        System.out.println(output);
    }


    public void elevatorDoorStatusChange(String elevId, String status) {
        String output = String.format("%s Elevator %s Doors %s", getTimeStamp(),elevId,status);
        System.out.println(output);
    }

    public void personEnteringElevator(String pid, String elevId) {
        String output = String.format("%s Person %s entered Elevator %s %s",getTimeStamp(), pid, elevId, listRiders(elevId));
        System.out.println(output);
    }

    public void personLeavingElevator(String pid, String elevId) {
        String output = String.format("%s Person %s has left Elevator %s %s",getTimeStamp(), pid, elevId, listRiders(elevId));
        System.out.println(output);
    }

    public void personEnteringFloor(String pid, String floorNumber) {
        String output = String.format("%s Person %s entered Floor %s", getTimeStamp(), pid, floorNumber);
        System.out.println(output);
    }

    public void personLeavingFloor(String pid, String floorNumber) {
        String output = String.format("%s Person %s has left Floor %s", getTimeStamp(), pid, floorNumber);
        System.out.println(output);
    }

}
