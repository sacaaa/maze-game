import mazegame.map.Maps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapsTest {

    @Test
    void success_testLoadMaps() {
        Maps.loadMaps("/mazegame/map/maps.json");
        var maps = Maps.getInstance();
        assertNotNull(maps.getMap(1));
        assertTrue(maps.getMap(1).rows() > 0);
        assertTrue(maps.getMap(1).cols() > 0);
    }

    @Test
    void failure_testLoadMaps() {
        assertThrows(NullPointerException.class, () -> Maps.loadMaps("mazegame/map/maps.json"));

        Maps.loadMaps("/mazegame/map/maps.json");
        var maps = Maps.getInstance();
        assertThrows(IllegalArgumentException.class, () -> maps.getMap(0));
    }

}
