package mazegame.map;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Maps {

    private final List<MapData> maps = new ArrayList<>();

    public void addMap(MapData map) {
        if (maps.contains(map)) {
            throw new IllegalArgumentException("The map already exists");
        }

        maps.add(map);
    }

    public void removeMap(MapData map) {
        if (!maps.contains(map)) {
            throw new IllegalArgumentException("The map does not exist");
        }

        maps.remove(map);
    }

    public MapData getMap(int id) {
        for (var map : maps) {
            if (map.id() == id) {
                return map;
            }
        }

        throw new IllegalArgumentException("The map does not exist");
    }

}
