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

/**
 * The controller class for the game view.
 */
public class GameController {

    /**
     * The image storage for the images.
     */
    private static final ImageStorage<Integer> imageStorage = new OrdinalImageStorage(GameController.class,
            "player.png",
            "monster.png");

    /**
     * The number of moves.
     */
    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    /**
     * The game state.
     */
    private GameState state;

    /**
     * The grid pane to display the map.
     */
    @FXML
    private GridPane grid;

    /**
     * The text field to display the number of moves.
     */
    @FXML
    private TextField numberOfMovesField;

    /**
     * The text field to display the name of the player.
     */
    @FXML
    private Label playerNameLabel;

    /**
     * Handles the key press event.
     *
     * @param keyEvent the key event
     */
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

    /**
     * Handles the mouse click event.
     *
     * @param event the mouse event
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (Node) event.getSource();
        var row = GridPane.getRowIndex(source);
        var col = GridPane.getColumnIndex(source);
        Logger.debug("Click on square ({},{})", row, col);
        getDirectionFromClick(row, col).ifPresentOrElse(this::makeMoveIfLegal,
                () -> Logger.warn("Click does not correspond to any of the directions"));
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    private void initialize() {
        bindNumberOfMoves();
        registerKeyEventHandler();
        restartGame();
    }

    /**
     * Binds the number of moves to the text field.
     */
    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    /**
     * Creates a binding to check if the piece is on the position.
     *
     * @param index the index of the piece
     * @param row the row of the position
     * @param col the column of the position
     * @return the binding
     */
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

    /**
     * Creates an image view for the piece on the position.
     *
     * @param index the index of the piece
     * @param row the row of the position
     * @param col the column of the position
     * @return the image view
     */
    private ImageView createImageViewForPieceOnPosition(int index, int row, int col) {
        var imageView = new ImageView(imageStorage.get(index).orElseThrow());
        imageView.visibleProperty().bind(createBindingToCheckPieceIsOnPosition(index, row, col));
        return imageView;
    }

    /**
     * Creates the game state.
     */
    private void createState() {
        // Load the maps
        Maps.loadMaps("/mazegame/map/maps.json");
        var maps = Maps.getInstance();

        state = new GameState(maps, 1);
        state.solvedProperty().addListener(this::handleSolved);
    }

    /**
     * Creates a square for the map.
     *
     * @param row the row of the square
     * @param col the column of the square
     * @param block the block of the square
     * @return the square
     */
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

    /**
     * Saves the result and switches to the leaderboard scene.
     *
     * @param solved whether the game was solved
     */
    private void endGame(boolean solved) {
        try {
            MainApplication.getInstance().saveResult(solved, numberOfMoves.get());
            MainApplication.getInstance().switchScene("/mazegame/game/fxml/leaderboard.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the solved property change.
     *
     * @param observableValue the observable value
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void handleSolved(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            Platform.runLater(() -> showAlert(
                    Alert.AlertType.INFORMATION,
                    "Game Solved",
                    "Congratulations, you have escaped the maze!",
                    true));
        }
    }

    /**
     * Initializes the map.
     */
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

    /**
     * Gets the direction from the click.
     *
     * @param row the row of the click
     * @param col the column of the click
     * @return an optional direction
     */
    private Optional<Direction> getDirectionFromClick(int row, int col) {
        var positionOfBlock = state.getPosition(GameState.PLAYER);
        try {
            return Optional.of(Direction.of(row - positionOfBlock.row(), col - positionOfBlock.col()));
        } catch (IllegalArgumentException e) {
            // The click does not correspond to any of the four directions
        }
        return Optional.empty();
    }

    /**
     * Makes a move if it is legal.
     *
     * @param direction the direction to move
     */
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

    /**
     * Registers the key event handler for the grid.
     */
    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        createState();
        numberOfMoves.set(0);
        playerNameLabel.setText("Player: " + MainApplication.getInstance().getPlayerName());
        initMap();
    }

    /**
     * Shows an alert by the given parameters.
     * Shows when the game is solved or the player is caught by the monster.
     *
     * @param alertType the type of the alert
     * @param headerText the header text of the alert
     * @param contentText the content text of the alert
     * @param solved whether the game is solved
     */
    private void showAlert(Alert.AlertType alertType, String headerText, String contentText, boolean solved) {
        var alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        endGame(solved);
    }

}