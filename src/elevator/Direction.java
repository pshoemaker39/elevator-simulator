package elevator;

public enum Direction {

    UP, DOWN, IDLE;

    public static Direction determineDirection(int start, int end) {
        if (start > end) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

}
