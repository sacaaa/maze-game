package mazegame.game.controller;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mazegame.game.MainApplication;
import mazegame.map.Block;
import mazegame.map.Maps;
import mazegame.model.Direction;
import mazegame.model.GameState;
import org.tinylog.Logger;
import util.OrdinalImageStorage;
import util.javafx.ImageStorage;

import java.io.IOException;
import java.util.Optional;

public class GameController {

    private static final ImageStorage<Integer> imageStorage = new OrdinalImageStorage(GameController.class,
            "player.png",
            "monster.png");

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    private GameState state;

    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Label playerNameLabel;

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var restartKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (restartKeyCombination.match(keyEvent)) {
            Logger.debug("Restarting game");
            restartGame();
        } else if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        } else if (keyEvent.getCode() == KeyCode.UP) {
            Logger.debug("UP pressed");
            makeMoveIfLegal(Direction.UP);
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            Logger.debug("RIGHT pressed");
            makeMoveIfLegal(Direction.RIGHT);
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            Logger.debug("DOWN pressed");
            makeMoveIfLegal(Direction.DOWN);
        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            Logger.debug("LEFT pressed");
            makeMoveIfLegal(Direction.LEFT);
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (Node) event.getSource();
        var row = GridPane.getRowIndex(source);
        var col = GridPane.getColumnIndex(source);
        Logger.debug("Click on square ({},{})", row, col);
        getDirectionFromClick(row, col).ifPresentOrElse(this::makeMoveIfLegal,
                () -> Logger.warn("Click does not correspond to any of the directions"));
    }

    @FXML
    private void initialize() {
        bindNumberOfMoves();
        registerKeyEventHandler();
        restartGame();
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    private BooleanBinding createBindingToCheckPieceIsOnPosition(int index, int row, int col) {
        return new BooleanBinding() {
            {
                super.bind(state.positionProperty(index));
            }
            @Override
            protected boolean computeValue() {
                var position = state.getPosition(index);
                return position.row() == row && position.col() == col;
            }
        };
    }

    private ImageView createImageViewForPieceOnPosition(int index, int row, int col) {
        var imageView = new ImageView(imageStorage.get(index).orElseThrow());
        imageView.visibleProperty().bind(createBindingToCheckPieceIsOnPosition(index, row, col));
        return imageView;
    }

    private void createState() {
        // Load the maps
        Maps.loadMaps("/mazegame/map/maps.json");
        var maps = Maps.getInstance();

        state = new GameState(maps, 1);
        state.solvedProperty().addListener(this::handleSolved);
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

        var playerImageView = createImageViewForPieceOnPosition(0, row, col);
        var monsterImageView = createImageViewForPieceOnPosition(1, row, col);
        square.getChildren().addAll(playerImageView, monsterImageView);

        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void endGame(boolean solved) {
        try {
            MainApplication.getInstance().saveResult(solved, numberOfMoves.get());
            MainApplication.getInstance().switchScene("/mazegame/game/fxml/leaderboard.fxml");
        } catch (IOException e) {
            Logger.error(e, "Failed to switch scene or save result");
        }
    }

    private void handleSolved(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            Platform.runLater(() -> showAlert(
                    Alert.AlertType.INFORMATION,
                    "Game Solved",
                    "Congratulations, you have escaped the maze!",
                    true));
        }
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

    private Optional<Direction> getDirectionFromClick(int row, int col) {
        var positionOfBlock = state.getPosition(GameState.PLAYER);
        try {
            return Optional.of(Direction.of(row - positionOfBlock.row(), col - positionOfBlock.col()));
        } catch (IllegalArgumentException e) {
            // The click does not correspond to any of the four directions
        }
        return Optional.empty();
    }

    private void makeMoveIfLegal(Direction direction) {
        if (state.isLegalMove(direction)) {
            Logger.info("Moving {}", direction);
            state.makeMove(direction);
            Logger.trace("New state after move: {}", state);
            numberOfMoves.set(numberOfMoves.get() + 1);

            if (state.getPosition(GameState.PLAYER).equals(state.getPosition(GameState.MONSTER))) {
                Logger.info("Game over");
                Platform.runLater(() -> showAlert(
                        Alert.AlertType.INFORMATION,
                        "Game Over",
                        "You have been caught by the monster. Game Over!",
                        false));
            }
        } else {
            Logger.warn("Illegal move: {}", direction);
        }
    }

    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    private void restartGame() {
        createState();
        numberOfMoves.set(0);
        playerNameLabel.setText("Player: " + MainApplication.getInstance().getPlayerName());
        initMap();
    }

    private void showAlert(Alert.AlertType alertType, String headerText, String contentText, boolean solved) {
        var alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        endGame(solved);
    }

}