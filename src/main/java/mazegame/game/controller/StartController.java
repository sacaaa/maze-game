package mazegame.game.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mazegame.game.MainApplication;
import org.tinylog.Logger;

import java.io.IOException;

public class StartController {

    @FXML
    private TextField playerNameField;

    @FXML
    private Button startGameButton;

    @FXML
    public void initialize() {
        startGameButton.setOnAction(event -> {
            try {
                MainApplication.getInstance().switchScene("/mazegame/game/fxml/game.fxml", playerNameField.getText());
            } catch (IOException e) {
                Logger.error(e, "Failed to switch scene");
            }
        });
    }
}