package mazegame.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private static MainApplication instance;

    private Stage stage;

    public MainApplication() {
        instance = this;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public void switchScene(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Maze Game");
        switchScene("/mazegame/game/fxml/start.fxml");
    }

}