package building;

// The simulations performed with application represent one building
// The building owns a certain number of floors and a certain number of elevators
// A building owns an elevator controller that controls the actions all of the elevators

import java.util.HashMap;

public class Building {

    public static int NUM_FLOORS;
    private static Building instance;
    private HashMap<Integer, Floor> floors = new HashMap<>();


    private Building() {
        for (int i = 1; i <= NUM_FLOORS; i++) {
            Floor floor = new Floor(i);
            this.floors.put(i, floor);
        }
    }

    public static Building getInstance() {
        if(instance == null) {
            instance = new Building();
        }
        return instance;
    }

    public void setNumFloor(int floors) {
        this.NUM_FLOORS = floors;
    }



    public void addPerson(Person person, int floorNum) {
        Floor floorToAddPerson = floors.get(floorNum);
        floorToAddPerson.addPerson(person);
        floors.put(floorNum, getFloor(floorNum));
    }

    public Floor getFloor(int fn) {
        return floors.get(fn);
    }

}
//elevator : direction and floor (array of stops from int and ext)
