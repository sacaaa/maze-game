package mazegame.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    void of() {
        var block = Block.of();
        assertFalse(block.wallTop());
        assertFalse(block.wallRight());
        assertFalse(block.wallBottom());
        assertFalse(block.wallLeft());
        assertFalse(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of2() {
        var block = Block.of(true);
        assertTrue(block.wallTop());
        assertFalse(block.wallRight());
        assertFalse(block.wallBottom());
        assertFalse(block.wallLeft());
        assertFalse(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of3() {
        var block = Block.of(true, true);
        assertTrue(block.wallTop());
        assertTrue(block.wallRight());
        assertFalse(block.wallBottom());
        assertFalse(block.wallLeft());
        assertFalse(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of4() {
        var block = Block.of(true, true, true);
        assertTrue(block.wallTop());
        assertTrue(block.wallRight());
        assertTrue(block.wallBottom());
        assertFalse(block.wallLeft());
        assertFalse(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of5() {
        var block = Block.of(true, true, true, true);
        assertTrue(block.wallTop());
        assertTrue(block.wallRight());
        assertTrue(block.wallBottom());
        assertTrue(block.wallLeft());
        assertFalse(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of6() {
        var block = Block.of(true, true, true, true, true);
        assertTrue(block.wallTop());
        assertTrue(block.wallRight());
        assertTrue(block.wallBottom());
        assertTrue(block.wallLeft());
        assertTrue(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of7() {
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, true, true, true, true, true));
        var block = Block.of(true, true, true, true, true, false);
        assertTrue(block.wallTop());
        assertTrue(block.wallRight());
        assertTrue(block.wallBottom());
        assertTrue(block.wallLeft());
        assertTrue(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void of8() {
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, true, true, true, true, true));
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, true, true, true, true, false, true));
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, true, true, true, false, true, true));
        assertThrows(IllegalArgumentException.class, () -> Block.of(true, true, true, true, true, true, true));
        var block = Block.of(true, true, true, true, true, false, false);
        assertTrue(block.wallTop());
        assertTrue(block.wallRight());
        assertTrue(block.wallBottom());
        assertTrue(block.wallLeft());
        assertTrue(block.start());
        assertFalse(block.end());
        assertFalse(block.monster());
    }

    @Test
    void testToString() {
        assertEquals("Block{wallTop=true, wallRight=true, wallBottom=true, wallLeft=true, start=true, end=false, monster=false}",
                Block.of(true, true, true, true, true, false, false).toString());
        assertEquals("Block{wallTop=true, wallRight=false, wallBottom=true, wallLeft=true, start=false, end=true, monster=false}",
                Block.of(true, false, true, true, false, true, false).toString());
        assertEquals("Block{wallTop=false, wallRight=false, wallBottom=false, wallLeft=false, start=false, end=false, monster=true}",
                Block.of(false, false, false, false, false, false, true).toString());
    }

    @Test
    void wallTop() {
        assertFalse(Block.of().wallTop());
        assertTrue(Block.of(true).wallTop());
    }

    @Test
    void wallRight() {
        assertFalse(Block.of().wallRight());
        assertTrue(Block.of(false, true).wallRight());
    }

    @Test
    void wallBottom() {
        assertFalse(Block.of().wallBottom());
        assertTrue(Block.of(false, false, true).wallBottom());
    }

    @Test
    void wallLeft() {
        assertFalse(Block.of().wallLeft());
        assertTrue(Block.of(false, false, false, true).wallLeft());
    }

    @Test
    void start() {
        assertFalse(Block.of().start());
        assertTrue(Block.of(false, false, false, false, true).start());
    }

    @Test
    void end() {
        assertFalse(Block.of().end());
        assertTrue(Block.of(false, false, false, false, false, true).end());
    }

    @Test
    void monster() {
        assertFalse(Block.of().monster());
        assertTrue(Block.of(false, false, false, false, false, false, true).monster());
    }
}