import mazegame.map.Maps;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GameStateTest {

    static Maps maps;

    @BeforeAll
    static void init() {
        Maps.loadMaps("/mazegame/map/maps.json");
        maps = Maps.getInstance();
    }
}
