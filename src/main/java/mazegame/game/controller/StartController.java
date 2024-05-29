package mazegame.game.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mazegame.game.MainApplication;

import java.io.IOException;

public class StartController {

    @FXML
    private TextField playerNameField;

    @FXML
    private Button startGameButton;

    @FXML
    public void initialize() {
        startGameButton.setOnAction(event -> {
            String playerName = playerNameField.getText();
            try {
                MainApplication.getInstance().switchScene("/mazegame/game/fxml/game.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}