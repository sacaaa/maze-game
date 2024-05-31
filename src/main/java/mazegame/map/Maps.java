package mazegame.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import mazegame.solver.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Maps {

    private static Maps instance;

    private final List<MapData> maps = new ArrayList<>();

    public static Maps getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Maps not loaded");
        }
        return instance;
    }

    public MapData getMap(int id) {
        for (var map : maps) {
            if (map.id() == id) {
                return map;
            }
        }

        throw new IllegalArgumentException("The map does not exist");
    }

    public static void loadMaps(String filePath) {
        try {
            Path resourcePath = Paths.get(Objects.requireNonNull(Main.class.getResource(filePath)).toURI());
            String jsonContent = Files.readString(resourcePath);
            ObjectMapper objectMapper = new ObjectMapper();
            instance = objectMapper.readValue(jsonContent, Maps.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error loading maps", e);
        }
    }
}

