package config;

import elevator.ElevatorController;

public class Testing {

    private static void test1() throws InterruptedException {

        for (int i = 0; i < 60; i++) {
            if (i == 0) {

                //addPerson(1, 10, 1);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);

        }
    }
}
