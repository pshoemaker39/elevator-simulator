package elevator;

// The elevator controller receives calls (requests) from the persons on a floor
// The ec determines which elevator should respond to that request
// The ec will instruct individual elevators to go to certain floors and respond to requests (not in scope for phase I?)


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
