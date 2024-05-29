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
     * The grid pane to display the map.
     */
    @FXML
    private GridPane grid;

    /**
     * The text field to display the number of moves.
     */
    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Label playerNameLabel;

    /**
     * The game state.
     */
    private GameState state;

    /**
     * The number of moves.
     */
    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    /**
     * Initializes the controller class.
     */
    @FXML
    private void initialize() {
        playerNameLabel.setText("Player: Lajos");
        bindNumberOfMoves();
        registerKeyEventHandler();
        restartGame();
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        createState();
        numberOfMoves.set(0);
        initMap();
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

            // Check if the player and monster are on the same position
            if (state.getPosition(GameState.PLAYER).equals(state.getPosition(GameState.MONSTER))) {
                Logger.info("Game over");
                Platform.runLater(this::showGameOverAlert);
            }
        } else {
            Logger.warn("Illegal move: {}", direction);
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
            Platform.runLater(this::showSolvedAlert);
        }
    }

    /**
     * Shows the game over alert.
     */
    private void showGameOverAlert() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Over");
        alert.setContentText("You have been caught by the monster. Game Over!");
        alert.showAndWait();
        restartGame();
    }

    /**
     * Shows the solved alert.
     */
    private void showSolvedAlert() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Solved");
        alert.setContentText("Congratulations, you have escaped the maze!");
        alert.showAndWait();

        try {
            MainApplication.getInstance().switchScene("/mazegame/game/fxml/leaderboard.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Binds the number of moves to the text field.
     */
    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    /**
     * Registers the key event handler for the grid.
     */
    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

}