package mazegame.map;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the data of a map in the game.
 *
 * @param id the id of the map
 * @param name the name of the map
 * @param rows the number of rows in the map
 * @param cols the number of columns in the map
 * @param blocks the blocks of the map
 */
public record MapData(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("rows") int rows,
                      @JsonProperty("cols") int cols, @JsonProperty("blocks") List<List<Block>> blocks) {

    /**
     * Constructor for creating a new MapData object.
     *
     * @param id the id of the map
     * @param name the name of the map
     * @param rows the number of rows in the map
     * @param cols the number of columns in the map
     * @param blocks the blocks of the map
     * @throws IllegalArgumentException if the number of rows or columns is not positive, or the number of rows or columns does not match the number of blocks
     */
    public MapData(int id, String name, int rows, int cols, List<List<Block>> blocks) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("The number of rows and columns must be positive");
        }

        if (blocks.size() != rows) {
            throw new IllegalArgumentException("The number of rows must match the number of blocks");
        }

        for (var row : blocks) {
            if (row.size() != cols) {
                throw new IllegalArgumentException("The number of columns must match the number of blocks");
            }
        }

        this.id = id;
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.blocks = blocks;
    }

}
