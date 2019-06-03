import building.Building;
import building.Logger;
import building.Person;
import config.Config;
import elevator.Direction;
import elevator.Elevator;
import elevator.ElevatorController;
import elevator.Request;
import gui.ElevatorDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    private static final long INIT_TIME = System.currentTimeMillis();
    private static ArrayList<Person> people = new ArrayList<>();
    private static int personCounter = 1;

    private static void addPerson(int start, int end, int elevId) {

        //set direction
        Direction direction = Direction.determineDirection(start, end);

        //create person
        Person person = new Person("P"+personCounter++, end);
        people.add(person);
        Building.getInstance().addPerson(person, start);
        person.setWaitStart(System.currentTimeMillis());

        Request request = new Request(start, direction);
        Logger.getInstance().personCreated(person.getId(), Integer.toString(start), direction.toString(), Integer.toString(end));

        Logger.getInstance().floorRequest(person.getId(), direction.toString(), Integer.toString(start));
        ElevatorController.getInstance().addRequestFromFloor(elevId, request);



        //ElevatorController.getInstance().getElevator(elevId).addFloorRequest(request);
    }

    private static void test1() throws InterruptedException {

        for (int i = 0; i < 60; i++) {
            if (i == 0) {
                addPerson(1, 10, 0);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);

        }
        System.out.println("Done");
    }

    private static void test2() throws InterruptedException {



        for (int i = 0; i < 120; i++) {
            //System.out.println("Now:" +Logger.getInstance().getNow());
            switch(i) {
                case 0:
                    addPerson(20, 5, 1);
                    break;
                case 1:
                    addPerson(15, 19, 1);
                    break;
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
            Logger.getInstance().updateNow(1000);
        }
    }

        private static void test3() throws InterruptedException {
        //error caused by how I am handling shutdown
        for (int i = 0; i < 45; i++) {

            switch(i) {
                case 0:
                    addPerson(1, 10, 2);
                    break;
                case 25:
                    addPerson(10, 1, 2);
                    break;
            }
            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
    }

        private static void test4() throws InterruptedException {
        //errors caused because return home pushes one onto stack

        for (int i = 0; i < 65; i++) {

            switch(i) {
                case 0:
                    addPerson(1, 10, 0);
                    break;
                case 5:
                    addPerson(8, 17, 0);
                    break;
                case 6:
                    addPerson(1, 9, 3);
                    break;
                case 32:
                    addPerson(3, 1, 3);
                    break;
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }

    }

    private static void finalTest() throws InterruptedException {
        Random rand = new Random();
        for(int i = 0; i < 100; i++) {
            if(i%3 == 0) {
                int end = rand.nextInt(20 - 2 + 1) + 2;
                int start = rand.nextInt(end - 1 + 1) + 1;
                int elevId = rand.nextInt(3 - 0 + 1) + 0;
                addPerson(start, end, elevId);
            }
            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
    }

    //TODO pull simulation inputs from file
    // Expand final test to include shutdown
    // Normalize output
    // Generate rider statistics
    // Implement elevator controller algorithm
    // Investigate why directions not updating at end (sim may have just run out of time)



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

            test2();
            //finalTest();

        } catch (Exception InterruptedException) {
            System.out.println(InterruptedException);
        }



    }
}
