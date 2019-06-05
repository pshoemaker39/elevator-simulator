import building.Building;
import building.Logger;
import building.Person;
import config.BadDataException;
import config.Config;
import config.Testing;
import elevator.Elevator;
import elevator.ElevatorController;
import gui.ElevatorDisplay;

import java.util.HashMap;

public class Main {

    private static void displayAggregateRiderStats() {
        int totalRiders = 0;
        int totalWaiters = 0;
        long waitSum = 0;
        long rideSum = 0;
        long waitMax = 0;
        long waitMin = 0;
        long rideMax = 0;
        long rideMin = 0;

        for(int i = 0; i < Testing.getPeople().size(); i++) {

            Person p = Testing.getPeople().get(i);

            if(p.getWaitTime()>-1) {
                waitSum = waitSum+p.getWaitTime();
                totalWaiters++;
            }

            if(p.getRideTime()>-1) {
                rideSum = rideSum+p.getRideTime();
                totalRiders++;
            }



            if(i == 0) {
                rideMin = p.getRideTime();
                waitMin = p.getWaitTime();
            } else {
                if((p.getWaitTime()<waitMin) && (p.getWaitTime()>0)) {
                    waitMin = p.getWaitTime();
                }
                if((p.getRideTime()<rideMin) && (p.getRideTime()>0)) {
                    rideMin = p.getRideTime();
                }
            }

            if(p.getWaitTime()>waitMax) {
                waitMax = p.getWaitTime();
            }

            if(p.getRideTime()>rideMax) {
                rideMax = p.getRideTime();
            }

        }

        System.out.println(" ");
        System.out.println("Avg Wait Time: "+(waitSum/totalWaiters)+" sec");
        System.out.println("Avg Ride Time: "+(rideSum/totalRiders)+" sec");
        System.out.println(" ");
        System.out.println("Min Wait Time: "+(waitMin)+" sec");
        System.out.println("Min Ride Time: "+(rideMin)+" sec");
        System.out.println(" ");
        System.out.println("Max Wait Time: "+(waitMax)+" sec");
        System.out.println("Max Ride Time: "+(rideMax)+" sec");
        System.out.println(" ");


    }

    private static String checkWaitTime(long time) {
        if(time<0){
            return "Not Picked Up";
        } else {
            return Long.toString(time);
        }
    }

    private static String checkRideTime(long time) {
        if(time<0){
            return "Not Dropped Off";
        } else {
            return Long.toString(time);
        }
    }

    private static String checkFinalTime(long time) {
        if(time<0){
            return "N/A";
        } else {
            return Long.toString(time);
        }
    }

    private static void displayDetailRiderStats() {
        String header = String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s","Person", "Start Floor", "End Floor", "Direction", "Wait Time", "Ride Time", "Total Time");
        System.out.println(header);
        Testing.getPeople().forEach((person)->{
            String output = String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s",person.getId(), person.getStartFloor(), person.getDesiredFloor(), person.getDirection(), checkWaitTime(person.getWaitTime()), checkRideTime(person.getRideTime()), checkFinalTime(person.getTotalTime()));
            System.out.println(output);
        });
    }

    private static void validateInput() throws BadDataException {
        try {


            HashMap<String, String> cfg = Config.getValues();
            Building.NUM_FLOORS = Integer.parseInt(cfg.get("NUM_FLOORS"));
            ElevatorDisplay.getInstance().initialize(Integer.parseInt(cfg.get("NUM_FLOORS")));
            Elevator.MAX_PERSONS_PER_ELEVATOR = Integer.parseInt(cfg.get("MAX_PERSONS_PER_ELEVATOR"));
            Elevator.MAX_IDLE_TIME = Integer.parseInt(cfg.get("MAX_IDLE_TIME"));
            Elevator.DOOR_TIME = Integer.parseInt(cfg.get("DOOR_TIME"));
            Elevator.ELEVATOR_SPEED = Integer.parseInt(cfg.get("ELEVATOR_SPEED"));
            ElevatorController.NUM_ELEVATORS = Integer.parseInt(cfg.get("NUM_ELEVATORS"));
            Testing.SIM_TIME = Integer.parseInt(cfg.get("SIM_TIME"));
            Testing.INTERVAL = Integer.parseInt(cfg.get("INTERVAL"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new BadDataException();
        }
    }

    public static void main(String[] args) {

        Logger.getInstance().startupTime();

        try {

            validateInput();

            Building.getInstance();

            for (int i = 1; i <= ElevatorController.NUM_ELEVATORS; i++) {

                ElevatorController.getInstance().createElevator(i);
                ElevatorDisplay.getInstance().addElevator(i, 1);
            }

//            Testing.test1();
            Testing.test2();
//            Testing.test3();
//            Testing.test4();


//            Testing.finalTest();

            displayAggregateRiderStats();
            displayDetailRiderStats();
            ElevatorDisplay.getInstance().shutdown();

        } catch (Exception InterruptedException) {
            System.out.println(InterruptedException);
        }



    }
}
