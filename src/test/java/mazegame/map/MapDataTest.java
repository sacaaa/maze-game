package mazegame.map;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapDataTest {

    @Test
    void success_MapData() {
        var id = 1;
        var name = "Map 1";
        var rows = 3;
        var cols = 3;
        var blocks = List.of(
                List.of(Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false)),
                List.of(Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false)),
                List.of(Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false))
        );

        MapData mapData = new MapData(id, name, rows, cols, blocks);

        assertEquals(id, mapData.id());
        assertEquals(name, mapData.name());
        assertEquals(rows, mapData.rows());
        assertEquals(cols, mapData.cols());
        assertEquals(blocks, mapData.blocks());
    }

    @Test
    void failure_MapData() {
        assertThrows(IllegalArgumentException.class, () -> new MapData(1, "Map 1", 0, 0, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new MapData(1, "Map 1", 3, 3, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new MapData(1, "Map 1", 3, 4, List.of(
                List.of(Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false)),
                List.of(Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false)),
                List.of(Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false),
                        Block.of(true, false, false, true, false, false, false)))));
    }

}