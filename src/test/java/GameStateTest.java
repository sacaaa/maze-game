import mazegame.map.Maps;
import mazegame.model.Direction;
import mazegame.model.GameState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTest {

    static Maps maps;
    static GameState gameState;

    @BeforeAll
    static void init() {
        Maps.loadMaps("/mazegame/map/maps.json");
        maps = Maps.getInstance();
        gameState = new GameState(maps, 1);
    }

    @Test
    void isSolved() {
        assertFalse(gameState.isSolved());
        for (var i = 0; i < 4; i++) {
            gameState.makeMove(Direction.RIGHT);
        }
        assertTrue(gameState.isSolved());
    }
}
