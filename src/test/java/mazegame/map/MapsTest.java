package mazegame.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapsTest {

    Maps maps;

    @BeforeEach
    void loadMaps() {
        maps = new Maps("/mazegame/map/maps.json");
        assertThrows(NullPointerException.class, () -> new Maps("mazegame/map/maps.json"));
    }

    @Test
    void getMap() {
        assertNotNull(maps.getMap(1));
        assertTrue(maps.getMap(1).rows() > 0);
        assertTrue(maps.getMap(1).cols() > 0);
        assertThrows(IllegalArgumentException.class, () -> maps.getMap(0));
    }

    @Test
    void getMaps() {
        assertNotNull(maps.getMaps());
        assertTrue(!maps.getMaps().isEmpty());
    }

}