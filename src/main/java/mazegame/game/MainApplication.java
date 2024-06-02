package mazegame.game;

import gameresult.OnePlayerGameResult;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public class MainApplication extends Application {

    @Getter
    private static MainApplication instance;

    @Getter
    private static GameResultManagerImpl manager = new GameResultManagerImpl();

    private Stage stage;

    @Getter
    private String playerName;

    @Getter
    private ZonedDateTime created;

    public MainApplication() {
        instance = this;
    }

    public void saveResult(boolean solved, int numberOfMoves) throws IOException {
        var now = ZonedDateTime.now();
        var duration = Duration.ofSeconds(now.toEpochSecond() - created.toEpochSecond());
        manager.add(OnePlayerGameResult.builder()
                .playerName(this.playerName)
                .solved(solved)
                .numberOfMoves(numberOfMoves)
                .duration(duration)
                .created(this.created)
                .build());
        manager.save();
    }

    public void switchScene(String fxml) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
        Logger.debug("Switched to scene: {}", fxml);
    }

    public void switchScene(String fxml, String playerName) throws IOException {
        this.playerName = playerName;
        this.created = ZonedDateTime.now();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
        Logger.debug("Switched to scene: {}", fxml);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Maze Game");
        switchScene("/mazegame/game/fxml/start.fxml");
    }

}