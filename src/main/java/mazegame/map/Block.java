package mazegame.map;

public record Block(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft,
                    boolean start, boolean end, boolean monster) {


    public static Block of() {
        return new Block(false, false, false, false, false, false, false);
    }

    public static Block of(boolean wallTop) {
        return new Block(wallTop, false, false, false, false, false, false);
    }

    public static Block of(boolean wallTop, boolean wallRight) {
        return new Block(wallTop, wallRight, false, false, false, false, false);
    }

    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom) {
        return new Block(wallTop, wallRight, wallBottom, false, false, false, false);
    }

    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft) {
        return new Block(wallTop, wallRight, wallBottom, wallLeft, false, false, false);
    }

    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft, boolean start) {
        return new Block(wallTop, wallRight, wallBottom, wallLeft, start, false, false);
    }

    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft,
                    boolean start, boolean end) {
        if (start && end) {
            throw new IllegalArgumentException("A block cannot be both a start and an end");
        }

        return new Block(wallTop, wallRight, wallBottom, wallLeft, start, end, false);
    }

    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft,
                    boolean start, boolean end, boolean monster) {
        if (start && end || start && monster || end && monster) {
            throw new IllegalArgumentException("A block cannot be both a start and and end or a monster");
        }

        return new Block(wallTop, wallRight, wallBottom, wallLeft, start, end, monster);
    }

    @Override
    public String toString() {
        return String.format("Block{wallTop=%s, wallRight=%s, wallBottom=%s, wallLeft=%s," +
                             "start=%s, end=%s, monster=%s}",
                wallTop, wallRight, wallBottom, wallLeft, start, end, monster);
    }

}
