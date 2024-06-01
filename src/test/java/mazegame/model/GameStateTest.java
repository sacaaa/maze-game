package mazegame.model;

import mazegame.map.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static mazegame.model.GameState.PLAYER;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    Maps maps;
    GameState gameState1;

    GameState gameState2;

    GameState gameState3;

    GameState gameState4;

    @BeforeEach
    void init() {
        maps = new Maps("/mazegame/map/maps.json");

        gameState1 = new GameState(maps, 1);
        for (var i = 0; i < 4; i++) {
            gameState1.makeMove(Direction.RIGHT);
        }

        gameState2 = new GameState(maps, 1);
        gameState2.makeMove(Direction.RIGHT);

        gameState3 = new GameState(maps, 1);
        gameState3.makeMove(Direction.RIGHT);
        gameState3.makeMove(Direction.DOWN);

        gameState4 = new GameState(maps, 1);
        for (var i = 0; i < 3; i++) {
            gameState4.makeMove(Direction.RIGHT);
        }
        for (var i = 0; i < 4; i++) {
            gameState4.makeMove(Direction.DOWN);
        }
    }

    @Test
    void isSolved() {
        assertTrue(gameState1.isSolved());
        assertFalse(gameState2.isSolved());
        assertFalse(gameState3.isSolved());
        assertFalse(gameState4.isSolved());
    }

    @Test
    void state1_isLegalMove() {
        assertFalse(gameState1.isLegalMove(Direction.RIGHT));
        assertFalse(gameState1.isLegalMove(Direction.LEFT));
        assertFalse(gameState1.isLegalMove(Direction.UP));
        assertFalse(gameState1.isLegalMove(Direction.DOWN));
    }

    @Test
    void state2_isLegalMove() {
        assertTrue(gameState2.isLegalMove(Direction.RIGHT));
        assertTrue(gameState2.isLegalMove(Direction.LEFT));
        assertTrue(gameState2.isLegalMove(Direction.DOWN));
        assertFalse(gameState2.isLegalMove(Direction.UP));
    }

    @Test
    void state3_isLegalMove() {
        assertTrue(gameState3.isLegalMove(Direction.UP));
        assertTrue(gameState3.isLegalMove(Direction.LEFT));
        assertFalse(gameState3.isLegalMove(Direction.RIGHT));
        assertFalse(gameState3.isLegalMove(Direction.DOWN));
    }

    @Test
    void makeMove_right_state4() {
        var stateBeforeMove = gameState4.clone();
        gameState4.makeMove(Direction.RIGHT);
        assertEquals(stateBeforeMove.getPosition(PLAYER).moveRight(), gameState4.getPosition(PLAYER));
    }

    @Test
    void makeMove_down_state4() {
        var stateBeforeMove = gameState4.clone();
        gameState4.makeMove(Direction.RIGHT);
        stateBeforeMove.makeMove(Direction.RIGHT);

        gameState4.makeMove(Direction.DOWN);
        assertEquals(stateBeforeMove.getPosition(PLAYER).moveDown(), gameState4.getPosition(PLAYER));
    }

    @Test
    void makeMove_left_state4() {
        var stateBeforeMove = gameState4.clone();
        gameState4.makeMove(Direction.RIGHT);
        stateBeforeMove.makeMove(Direction.RIGHT);

        gameState4.makeMove(Direction.LEFT);
        assertEquals(stateBeforeMove.getPosition(PLAYER).moveLeft(), gameState4.getPosition(PLAYER));
    }

    @Test
    void makeMove_up_state4() {
        var stateBeforeMove = gameState4.clone();
        gameState4.makeMove(Direction.RIGHT);
        stateBeforeMove.makeMove(Direction.RIGHT);
        gameState4.makeMove(Direction.DOWN);
        stateBeforeMove.makeMove(Direction.DOWN);

        gameState4.makeMove(Direction.UP);
        assertEquals(stateBeforeMove.getPosition(PLAYER).moveUp(), gameState4.getPosition(PLAYER));
    }

    @Test
    void getLegalMoves() {
        assertEquals(EnumSet.noneOf(Direction.class), gameState1.getLegalMoves());
        assertEquals(EnumSet.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT), gameState2.getLegalMoves());
        assertEquals(EnumSet.of(Direction.UP, Direction.LEFT), gameState3.getLegalMoves());
        assertEquals(EnumSet.allOf(Direction.class), gameState4.getLegalMoves());
    }

    @Test
    void testEquals() {
        assertTrue(gameState1.equals(gameState1));

        var clone = gameState2.clone();
        clone.makeMove(Direction.RIGHT);
        assertFalse(clone.equals(gameState2));

        assertFalse(gameState1.equals(null));
        assertFalse(gameState1.equals("Hello, World!"));
        assertFalse(gameState1.equals(gameState3));
    }

    @Test
    void testHashCode() {
        assertTrue(gameState1.hashCode() == gameState1.hashCode());
        assertTrue(gameState1.hashCode() == gameState1.clone().hashCode());
    }

    @Test
    void testClone() {
        var clone = gameState1.clone();
        assertTrue(clone.equals(gameState1));
        assertNotSame(clone, gameState1);
    }

    @Test
    void testToString() {
        assertEquals("[(0,4),(2,1),(0,4)]", gameState1.toString());
        assertEquals("[(0,1),(2,1),(0,4)]", gameState2.toString());
        assertEquals("[(1,1),(2,1),(0,4)]", gameState3.toString());
        assertEquals("[(4,3),(4,3),(0,4)]", gameState4.toString());
    }

}