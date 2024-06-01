package mazegame.map;

/**
 * Represents a block in the map of the game.
 *
 * @param wallTop whether there is a wall at the top of the block
 * @param wallRight whether there is a wall at the right of the block
 * @param wallBottom whether there is a wall at the bottom of the block
 * @param wallLeft whether there is a wall at the left of the block
 * @param start whether the block is the start of the map
 * @param end whether the block is the end of the map
 * @param monster whether the block contains a monster
 */
public record Block(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft,
                    boolean start, boolean end, boolean monster) {

    /**
     * Default Block object creator.
     *
     * @return a new Block object with all fields set to false
     */
    public static Block of() {
        return new Block(false, false, false, false, false, false, false);
    }

    /**
     * Block object creator with a wall at the top.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @return a new Block object with the given value
     */
    public static Block of(boolean wallTop) {
        return new Block(wallTop, false, false, false, false, false, false);
    }

    /**
     * Block object creator with a wall at the top and right.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @param wallRight whether there is a wall at the right of the block
     * @return a new Block object with the given values
     */
    public static Block of(boolean wallTop, boolean wallRight) {
        return new Block(wallTop, wallRight, false, false, false, false, false);
    }

    /**
     * Block object creator with a wall at the top, right and bottom.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @param wallRight whether there is a wall at the right of the block
     * @param wallBottom whether there is a wall at the bottom of the block
     * @return a new Block object with the given values
     */
    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom) {
        return new Block(wallTop, wallRight, wallBottom, false, false, false, false);
    }

    /**
     * Block object creator with a wall at the top, right, bottom and left.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @param wallRight whether there is a wall at the right of the block
     * @param wallBottom whether there is a wall at the bottom of the block
     * @param wallLeft whether there is a wall at the left of the block
     * @return a new Block object with the given values
     */
    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft) {
        return new Block(wallTop, wallRight, wallBottom, wallLeft, false, false, false);
    }

    /**
     * Block object creator with a wall at the top, right, bottom, left and start.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @param wallRight whether there is a wall at the right of the block
     * @param wallBottom whether there is a wall at the bottom of the block
     * @param wallLeft whether there is a wall at the left of the block
     * @param start whether the block is the start of the map
     * @return a new Block object with the given values
     */
    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft, boolean start) {
        return new Block(wallTop, wallRight, wallBottom, wallLeft, start, false, false);
    }

    /**
     * Block object creator with a wall at the top, right, bottom, left and end.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @param wallRight whether there is a wall at the right of the block
     * @param wallBottom whether there is a wall at the bottom of the block
     * @param wallLeft whether there is a wall at the left of the block
     * @param start whether the block is the start of the map
     * @param end whether the block is the end of the map
     * @return a new Block object with the given values
     * @throws IllegalArgumentException if the block is both a start and an end
     */
    public static Block of(boolean wallTop, boolean wallRight, boolean wallBottom, boolean wallLeft,
                    boolean start, boolean end) {
        if (start && end) {
            throw new IllegalArgumentException("A block cannot be both a start and an end");
        }

        return new Block(wallTop, wallRight, wallBottom, wallLeft, start, end, false);
    }

    /**
     * Block object creator with a wall at the top, right, bottom, left and monster.
     *
     * @param wallTop whether there is a wall at the top of the block
     * @param wallRight whether there is a wall at the right of the block
     * @param wallBottom whether there is a wall at the bottom of the block
     * @param wallLeft whether there is a wall at the left of the block
     * @param start whether the block is the start of the map
     * @param end whether the block is the end of the map
     * @param monster whether the block contains a monster
     * @return a new Block object with the given values
     * @throws IllegalArgumentException if the block is both a start and an end or a monster
     */
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