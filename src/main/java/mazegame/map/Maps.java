package mazegame.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.java.Log;
import mazegame.solver.Main;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the maps in the game.
 */
@Getter
public class Maps {

    /**
     * The instance of the maps.
     */
    private static Maps instance;

    /**
     * The list of maps.
     */
    private final List<MapData> maps = new ArrayList<>();

    /**
     * Returns the instance of the maps.
     *
     * @return the instance of the maps
     * @throws IllegalStateException if the maps are not loaded
     */
    public static Maps getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Maps not loaded");
        }
        return instance;
    }

    /**
     * Returns the map with the given id.
     *
     * @param id the id of the map
     * @return the map with the given id
     * @throws IllegalArgumentException if the map does not exist
     */
    public MapData getMap(int id) {
        for (var map : maps) {
            if (map.id() == id) {
                return map;
            }
        }

        throw new IllegalArgumentException("The map does not exist");
    }

    /**
     * Loads the maps from the given file.
     *
     * @param filePath the path of the file containing the maps
     * @throws RuntimeException if the maps cannot be loaded
     */
    public static void loadMaps(String filePath) {
        try {
            Path resourcePath = Paths.get(Objects.requireNonNull(Main.class.getResource(filePath)).toURI());
            String jsonContent = Files.readString(resourcePath);
            ObjectMapper objectMapper = new ObjectMapper();
            instance = objectMapper.readValue(jsonContent, Maps.class);
            Logger.info("Maps loaded");
        } catch (IOException | URISyntaxException e) {
            Logger.error(e, "Error loading maps");
            throw new RuntimeException("Error loading maps", e);
        }
    }
}

