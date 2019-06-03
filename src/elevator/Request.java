package elevator;

public class Request {

    private int start;
    private int end;
    private Direction d;

    public Request(int start, Direction d) {
        //System.out.println("New request... start: "+start+" direction: "+d);
        this.start = start;
        this.d = d;
    }

    public void setRequestEnd(int floor) {
        this.end = floor;
    }

    public Direction getRequestDirection() {
        return this.d;
    }

    public int getRequestEnd() {
        return this.end;
    }

    public int getRequestStart() {
        return this.start;
    }



}
