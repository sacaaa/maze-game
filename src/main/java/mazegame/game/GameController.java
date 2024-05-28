package mazegame.game;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mazegame.map.Block;
import mazegame.map.Maps;
import mazegame.model.GameState;

public class GameController {

    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    private GameState state;

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        bindNumberOfMoves();
        registerKeyEventHandler();
        restartGame();
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    private void handleKeyPress(KeyEvent keyEvent) {
    }

    private void restartGame() {
        createState();
        numberOfMoves.set(0);
        initMap();
    }

    private void createState() {
        // Load the maps
        Maps.loadMaps("/maps/maps.json");
        var maps = Maps.getInstance();

        state = new GameState(maps, 1);
        state.solvedProperty().addListener(this::handleSolved);
    }

    private void handleSolved(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            Platform.runLater(this::showSolvedAlert);
        }
    }

    private void showSolvedAlert() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Over");
        alert.setContentText("Congratulations, you have escaped the maze!");
        alert.showAndWait();
        restartGame();
    }

    private void initMap() {
        var map = state.getCurrentMap();
        grid.getChildren().clear();  // Clear the existing cells

        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var col = 0; col < grid.getColumnCount(); col++) {
                var square = createSquare(row, col, map.blocks().get(row).get(col));
                grid.add(square, col, row);
            }
        }
    }

    private StackPane createSquare(int row, int col, Block block) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.getStyleClass().add((row + col) % 2 == 0 ? "light": "dark");

        BorderWidths borderWidths = new BorderWidths(
                block.wallTop() ? 3.0 : 0.5,
                block.wallRight() ? 3.0 : 0.5,
                block.wallBottom() ? 3.0 : 0.5,
                block.wallLeft() ? 3.0 : 0.5
        );

        BorderStroke borderStroke = new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                borderWidths
        );

        square.setBorder(new Border(borderStroke));

        return square;
    }

}