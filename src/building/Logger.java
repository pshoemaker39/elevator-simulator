package building;

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

        //long millis = getNow() % 1000;
        long second = (getNow() / 1000) % 60;
        long minute = (getNow() / (1000 * 60)) % 60;
        long hour = (getNow() / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d", hour, minute, second);

        return time;

//
//        long hours = getNow()/3600000;
//        long minutes = hours/60;
//        long seconds = minutes/60;
//        double millis = Math.floor(seconds/1000)/1000;
//        return hours+":"+minutes+":"+seconds+"."+millis;
    }

    public void startupTime() {
        String output = String.format("%s Startup", getTimeStamp());
        System.out.println(output);
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
        String output = String.format("%s Elevator %s new floor request to go to floor %s, %s", getTimeStamp(), elevId, targetFloor, direction);
        System.out.println(output);
    }
}
