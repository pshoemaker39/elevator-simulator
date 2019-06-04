import building.Building;
import building.Logger;
import building.Person;
import config.Config;
import config.Testing;
import elevator.Elevator;
import elevator.ElevatorController;
import gui.ElevatorDisplay;

import java.util.HashMap;

public class Main {

    //TODO pull simulation inputs from file
    // Expand final test to include shutdown
    // Normalize output
    // Generate rider statistics
    // Implement elevator controller algorithm
    // Investigate why directions not updating at end (sim may have just run out of time)
    // Test 2 has issue where doors open at 14 seconds but no passengers are reported exchanging

    private static void displayAggregateRiderStats() {
        int totalRiders = Testing.getPeople().size();
        long waitSum = 0;
        long rideSum = 0;
        long waitMax = 0;
        long waitMin = 0;
        long rideMax = 0;
        long rideMin = 0;

        for(int i = 0; i < totalRiders; i++) {

            Person p = Testing.getPeople().get(i);

            waitSum = waitSum+p.getWaitTime();
            rideSum = rideSum+p.getRideTime();

            if(p.getWaitTime()>waitMax) {
                waitMax = p.getWaitTime();
            }

            if(p.getWaitTime()<waitMin) {
                waitMin = p.getWaitTime();
            }

            if(p.getRideTime()>rideMax) {
                rideMax = p.getRideTime();
            }

            if(p.getRideTime()<rideMin) {
                rideMin = p.getRideTime();
            }
        }

        System.out.println(" ");
        System.out.println("Avg Wait Time: "+(waitSum/totalRiders)+" sec");
        System.out.println("Avg Ride Time: "+(rideSum/totalRiders)+" sec");
        System.out.println(" ");
        System.out.println("Min Wait Time: "+(waitMin)+" sec");
        System.out.println("Min Ride Time: "+(rideMin)+" sec");
        System.out.println(" ");
        System.out.println("Max Wait Time: "+(waitMax)+" sec");
        System.out.println("Max Ride Time: "+(rideMax)+" sec");
        System.out.println(" ");


    }

    private static void displayDetailRiderStats() {
        String header = String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s","Person", "Start Floor", "End Floor", "Direction", "Wait Time", "Ride Time", "Total Time");
        System.out.println(header);
        Testing.getPeople().forEach((person)->{
            String output = String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s",person.getId(), person.getStartFloor(), person.getDesiredFloor(), person.getDirection(), person.getWaitTime(), person.getRideTime(), person.getTotalTime());
            System.out.println(output);
        });
    }

    public static void main(String[] args) {

        Logger.getInstance().startupTime();

        try {
            HashMap<String, String> cfg = Config.getValues();

            Building.NUM_FLOORS = Integer.parseInt(cfg.get("NUM_FLOORS"));
            ElevatorDisplay.getInstance().initialize(Integer.parseInt(cfg.get("NUM_FLOORS")));
            Elevator.MAX_PERSONS_PER_ELEVATOR = Integer.parseInt(cfg.get("MAX_PERSONS_PER_ELEVATOR"));
            Elevator.MAX_IDLE_TIME = Integer.parseInt(cfg.get("MAX_IDLE_TIME"));
            Elevator.DOOR_TIME = Integer.parseInt(cfg.get("DOOR_TIME"));

            Building.getInstance();

            for (int i = 1; i <= Integer.parseInt(cfg.get("NUM_ELEVATORS")); i++) {

                ElevatorController.getInstance().createElevator(i);
                ElevatorDisplay.getInstance().addElevator(i, 1);
            }

            Testing.test2();
            displayAggregateRiderStats();
            displayDetailRiderStats();

        } catch (Exception InterruptedException) {
            System.out.println(InterruptedException);
        }



    }
}
