import mazegame.map.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    void success_Block() {
        var block = Block.of(true, false, false, true, false, false, false);
        assertTrue(block.wallTop());
        assertFalse(block.wallRight());
        assertFalse(block.wallBottom());
        assertTrue(block.wallLeft());
        assertFalse(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void failure_Block() {
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, false, false, true, true, true, false));
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, false, false, true, true, false, true));
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, false, false, true, false, true, true));
    }

}
