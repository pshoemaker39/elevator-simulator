package config;

import building.Building;
import building.Logger;
import building.Person;
import elevator.Direction;
import elevator.ElevatorController;
import elevator.Request;

import java.util.ArrayList;
import java.util.Random;

public class Testing {

    private static ArrayList<Person> people = new ArrayList<>();
    private static int personCounter = 1;
    public static int SIM_TIME;
    public static int INTERVAL;

    public static ArrayList<Person> getPeople() {
        return people;
    }

    private static void addPerson(int start, int end, int elevId) {

        //set direction
        Direction direction = Direction.determineDirection(start, end);

        //create person
        Person person = new Person("P"+personCounter++, end);
        people.add(person);
        Building.getInstance().addPerson(person, start);

        person.setWaitStart();
        person.setStartFloor(start);

        Request request = new Request(start, direction);
        Logger.getInstance().personCreated(person.getId(), Integer.toString(start), direction.toString(), Integer.toString(end));

        Logger.getInstance().floorRequest(person.getId(), direction.toString(), Integer.toString(start));
        ElevatorController.getInstance().addRequestFromFloor(elevId, request);
    }

    public static void test1() throws InterruptedException {
        for (int i = 0; i < 60; i++) {
            if (i == 0) {
                addPerson(1, 10, 0);
            }
            ElevatorController.getInstance().moveElevators(1000);
            Logger.getInstance().updateNow(1000);
            Thread.sleep(1000);
        }
    }

    public static void test2() throws InterruptedException {
        for (int i = 0; i < 70; i++) {
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

    public static void test3() throws InterruptedException {
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
            Logger.getInstance().updateNow(1000);
        }
    }

    public static void test4() throws InterruptedException {
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
            Logger.getInstance().updateNow(1000);
        }

    }

    public static void finalTest() throws InterruptedException {
        Random rand = new Random();
        int floorMin = 1;
        int floorMax = Building.NUM_FLOORS;
        int elMin = 0;
        int elMax = ElevatorController.NUM_ELEVATORS;

        for(int i = 0; i < SIM_TIME; i++) {
            if((i%INTERVAL == 0)) {


                int a = rand.nextInt(floorMax - floorMin + 1) + floorMin;
                int b = rand.nextInt(floorMax - floorMin + 1) + floorMin;
                while(a==b){
                    b = rand.nextInt(floorMax - floorMin + 1) + floorMin;
                }
                int elevId = rand.nextInt(elMax - elMin + 1) + elMin;
                //System.out.println("Start: "+a+" End: "+b+" Elevator: "+elevId);
                addPerson(a, b, elevId);
            }
            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
            Logger.getInstance().updateNow(1000);
        }
    }
}
