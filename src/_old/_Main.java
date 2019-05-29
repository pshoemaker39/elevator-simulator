//import building.Building;
//import building.Person;
//import elevator.Direction;
//import elevator.Elevator;
//import elevator.ElevatorController;
//import elevator.Request;
//import gui.ElevatorDisplay;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//public class _Main {
//
//
//
//    public static final long initTime = System.currentTimeMillis();
//    private static ArrayList<Person> people = new ArrayList<>();
//    private static int personCounter = 1;
//    private static ElevatorController instance;
//
//    private static void test1() throws InterruptedException {
//
//        for (int i = 0; i < 60; i++) {
//            if (i == 0) {
//
//                addPerson(1, 10, 1);
//            }
//
//            ElevatorController.getInstance().moveElevators(1000);
//            Thread.sleep(1000);
//
//        }
//    }
//
//    private static void test2() throws InterruptedException {
//
//        for (int i = 0; i < 120; i++) {
//
//            switch(i) {
//                case 0:
//                    addPerson(20, 5, 2);
//                    break;
//                case 1:
//                    addPerson(15, 19, 2);
//                    break;
//            }
//
//            ElevatorController.getInstance().moveElevators(1000);
//            Thread.sleep(1000);
//        }
//    }
//
//    private static void test3() throws InterruptedException {
//        for (int i = 0; i < 45; i++) {
//
//            switch(i) {
//                case 0:
//                    addPerson(1, 10, 3-2);
//                    break;
//                case 25:
//                    addPerson(10, 1, 3-2);
//                    break;
//            }
//            ElevatorController.getInstance().moveElevators(1000);
//            Thread.sleep(1000);
//        }
//    }
//
//    private static void test4() throws InterruptedException {
//        for (int i = 0; i < 65; i++) {
//
//            switch(i) {
//                case 0:
//                    addPerson(1, 10, 1);
//                    break;
//                case 5:
//                    addPerson(8, 17, 1);
//                    break;
//                case 6:
//                    addPerson(1, 9, 4);
//                    break;
//                case 32:
//                    addPerson(3, 1, 4);
//                    break;
//            }
//
//            ElevatorController.getInstance().moveElevators(1000);
//            Thread.sleep(1000);
//        }
//
//    }
//
//    public static String getTimeStamp() {
//        long now = System.currentTimeMillis() - initTime;
//        long hours = now/3600000;
//        long minutes = hours/60;
//        long seconds = minutes/60;
//        double millis = Math.floor(seconds/1000)/1000;
//        return hours+":"+minutes+":"+seconds+"."+millis;
//    }
//
//    private static void addPerson(int start, int end, int elevId) {
//        Direction d = Direction.determineDirection(start, end);
//        Person p = new Person("P "+personCounter++, end);
//        people.add(p);
//        Building.getInstance().addPerson(p, start);
//        p.setWaitStart(System.currentTimeMillis());
//        ElevatorController.getInstance().getElevator(elevId).addFloorRequest(new Request(start, d));
//    }
//
//    public static void main(String[] args) {
//
//
//        FileReader reader;
//        try {
//            // Create a FileReader object using your filename
//            reader = new FileReader("input.json");
//        } catch (
//                FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        }
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jObj;
//
//        try {
//            // Create a JSONParser using the FileReader
//            jObj = (JSONObject) jsonParser.parse(reader);
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//            return;
//        }
//
//
//        System.out.println("Startup: " + getTimeStamp());
//        ElevatorDisplay.getInstance().initialize((Integer) jObj.get("NUM_FLOORS"));
//        Building.getInstance();
//        for (int i = 0; i < (Integer) jObj.get("NUM_ELEVATORS"); i++) {
//
//            Elevator.MAX_PERSONS_PER_ELEVATOR = (Integer) jObj.get("MAX_PERSONS_PER_ELEVATOR");
//            Elevator.MAX_IDLE_TIME = (Integer) jObj.get("MAX_IDLE_TIME");
//            Elevator.DOOR_TIME = (Integer) jObj.get("DOOR_TIME");
//
//            ElevatorController.getInstance().createElevator(i);
//            ElevatorDisplay.getInstance().addElevator(i+1, 1);
//        }
//        try {
//            test2();
//        } catch (Exception InterruptedException) {
//            System.out.println(InterruptedException);
//        }
//    }
//}
