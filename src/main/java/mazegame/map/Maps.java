package mazegame.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import mazegame.solver.Main;
import org.tinylog.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the maps in the game.
 */
@Getter
public class Maps {

    /**
     * The list of maps.
     */
    private final List<MapData> maps = new ArrayList<>();

    /**
     * Constructs a new maps with the specified file path.
     *
     * @param filePath the file path of the maps
     */
    public Maps(String filePath) {
        loadMaps(filePath);
    }

    /**
     * Returns the map with the specified id.
     *
     * @param id the id of the map
     * @return the map with the specified id
     * @throws IllegalArgumentException if no map found with the specified id
     */
    public MapData getMap(int id) {
        return maps.stream()
                .filter(map -> map.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No map found with id: " + id));
    }

    /**
     * Loads the maps from the specified file path.
     *
     * @param filePath the file path of the maps
     * @throws IllegalArgumentException if the file path is invalid
     */
    private void loadMaps(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream resourceStream = getClass().getResourceAsStream(filePath);
            List<MapData> loadedMaps = objectMapper.readValue(resourceStream, new TypeReference<List<MapData>>() {});
            maps.addAll(loadedMaps);
        } catch (IOException e) {
            Logger.error(e, "Failed to load maps from file: {}", filePath);
        }
    }

    @Override
    public String toString() {
        return String.format("Maps{Loaded maps=%d}", maps.size());
    }

}

