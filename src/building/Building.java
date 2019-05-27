package building;

// The simulations performed with application represent one building
// The building owns a certain number of floors and a certain number of elevators
// A building owns an elevator controller that controls the actions all of the elevators

import java.util.HashMap;

public class Building {

    public static int NUM_FLOORS;
    public static Building instance;
    private static HashMap<Integer, Floor> floors = new HashMap<>();


    private Building() {
        for (int i = 1; i <= NUM_FLOORS; i++) {
            floors.put(i, new Floor(i));
        }
    }

    public static Building getInstance() {
        if(instance == null) {
            instance = new Building();
        }
        //System.out.println("Got building instance "+instance);
        return instance;
    }

    public void setNumFloor(int floors) {
        this.NUM_FLOORS = floors;
    }

    public void addPerson(Person p, int floorNum) {
        //System.out.println("x");

        Floor f = getFloor(floorNum);
        if (f == null) {
            System.out.println("FLOOR " + floorNum);
        }
        f.addPerson(p);
        floors.put(floorNum, getFloor(floorNum));
    }

    public Floor getFloor(int fn) {
        return floors.get(fn);
    }

}
//elevator : direction and floor (array of stops from int and ext)
