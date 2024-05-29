package mazegame.map;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MapData(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("rows") int rows,
                      @JsonProperty("cols") int cols, @JsonProperty("blocks") List<List<Block>> blocks) {

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
