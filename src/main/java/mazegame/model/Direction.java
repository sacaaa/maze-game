package mazegame.model;

import lombok.Getter;

/**
 * Represents the four main directions.
 */
@Getter
public enum Direction {

    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    /**
     * The change in the row coordinate.
     */
    private final int rowChange;

    /**
     * The change in the column coordinate.
     */
    private final int colChange;

    /**
     * Constructs a new direction with the specified coordinate changes.
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     */
    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * {@return the direction that corresponds to the coordinate changes
     * specified}
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     */
    public static Direction of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

}
