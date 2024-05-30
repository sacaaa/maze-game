package mazegame.game;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import gameresult.OnePlayerGameResult;
import gameresult.manager.OnePlayerGameResultManager;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import org.tinylog.Logger;

public class GameResultManagerImpl implements OnePlayerGameResultManager {

    private List<OnePlayerGameResult> results = new ArrayList<>();

    @Override
    public List<OnePlayerGameResult> add(OnePlayerGameResult result) throws IOException {
        results.add(result);
        return results;
    }

    @Override
    public List<OnePlayerGameResult> getAll() throws IOException {
        return new ArrayList<>(results);
    }

    public void save() throws IOException {
        OnePlayerGameResultManager manager = new JsonOnePlayerGameResultManager(Path.of("results.json"));
        for (var state : results) {
            manager.add(state);
            Logger.info("Game result saved: {}", state);
        }
    }
}
