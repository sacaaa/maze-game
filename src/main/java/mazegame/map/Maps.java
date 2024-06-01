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

    private final List<MapData> maps = new ArrayList<>();

    public Maps(String filePath) {
        loadMaps(filePath);
    }

    public MapData getMap(int id) {
        return maps.stream()
                .filter(map -> map.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No map found with id: " + id));
    }

    private void loadMaps(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream resourceStream = getClass().getResourceAsStream(filePath);
            String jsonContent = new BufferedReader(new InputStreamReader(resourceStream)).lines().collect(Collectors.joining("\n"));
            List<MapData> loadedMaps = objectMapper.readValue(jsonContent, new TypeReference<List<MapData>>() {});
            maps.addAll(loadedMaps);
        } catch (IOException e) {
            Logger.error(e, "Failed to load maps from file: {}", filePath);
        }
    }


}

