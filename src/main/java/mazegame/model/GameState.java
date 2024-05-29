package mazegame.model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import lombok.Getter;
import mazegame.map.Block;
import mazegame.map.MapData;
import mazegame.map.Maps;
import org.tinylog.Logger;
import puzzle.State;

import java.util.*;

/**
 * Represents the state of the game.
 */
@Getter
public class GameState implements State<Direction> {

    /**
     * The maps of the game.
     */
    private final Maps maps;

    /**
     * The index of the current map.
     */
    private final int mapIndex;

    /**
     * The positions of the player, monster, and end.
     */
    private final ReadOnlyObjectWrapper<Position>[] positions;

    /**
     * The solved property.
     */
    private final ReadOnlyBooleanWrapper solved;

    /**
     * The index of the player in the positions array.
     */
    public static final int PLAYER = 0;

    /**
     * The index of the monster in the positions array.
     */
    public static final int MONSTER = 1;

    /**
     * The index of the end in the positions array.
     */
    public static final int END = 2;

    /**
     * Constructs a new GameState with the specified maps.
     *
     * @param maps the maps of the game
     */
    public GameState(Maps maps, int mapIndex) {
        this.maps = maps;
        this.mapIndex = mapIndex;
        this.positions = new ReadOnlyObjectWrapper[3];
        init();
        this.solved = new ReadOnlyBooleanWrapper();
        this.solved.bind(this.positions[PLAYER].isEqualTo(this.positions[END]));
    }

    /**
     * Initializes the positions of the player, monster, and end.
     */
    private void init() {
        var map = getCurrentMap();

        for (int i = 0; i < map.rows(); i++) {
            for (int j = 0; j < map.cols(); j++) {
                var block = map.blocks().get(i).get(j);

                if (block.start()) {
                    positions[PLAYER] = new ReadOnlyObjectWrapper<>(new Position(i, j));
                } else if (block.end()) {
                    positions[END] = new ReadOnlyObjectWrapper<>(new Position(i, j));
                } else if (block.monster()) {
                    positions[MONSTER] = new ReadOnlyObjectWrapper<>(new Position(i, j));
                }
            }
        }
    }

    /**
     * Checks if the game is solved.
     *
     * @return true if the player is on the end position, false otherwise
     */
    @Override
    public boolean isSolved() {
        return getPosition(PLAYER).equals(getPosition(END));
    }

    /**
     * Returns the legal moves of the player.
     *
     * @return the legal moves of the player
     */
    @Override
    public Set<Direction> getLegalMoves() {
        var legalMoves = EnumSet.noneOf(Direction.class);
        for (var direction : Direction.values()) {
            if (isLegalMove(direction)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    /**
     * Checks if the monster can move in the given direction.
     *
     * @param direction the direction to move
     * @return true if the monster can move in the given direction, false otherwise
     */
    private boolean isLegalMoveMonster(Direction direction) {
        var currentMap = getCurrentMap();
        var block = currentMap.blocks().
                get(getPosition(MONSTER).row()).
                get(getPosition(MONSTER).col());

        return switch (direction) {
            case UP -> canMoveUp(currentMap, block, MONSTER);
            case RIGHT -> canMoveRight(currentMap, block, MONSTER);
            case DOWN -> canMoveDown(currentMap, block, MONSTER);
            case LEFT -> canMoveLeft(currentMap, block, MONSTER);
        };
    }

    /**
     * Checks if the player can move in the given direction.
     *
     * @param direction the direction to move
     * @return true if the player can move in the given direction, false otherwise
     */
    @Override
    public boolean isLegalMove(Direction direction) {
        var currentMap = getCurrentMap();
        var block = currentMap.blocks().
                get(getPosition(PLAYER).row()).
                get(getPosition(PLAYER).col());

        return switch (direction) {
            case UP -> canMoveUp(currentMap, block, PLAYER);
            case RIGHT -> canMoveRight(currentMap, block, PLAYER);
            case DOWN -> canMoveDown(currentMap, block, PLAYER);
            case LEFT -> canMoveLeft(currentMap, block, PLAYER);
        };
    }

    /**
     * Checks if the {@code character} can move up.
     * Characters are the player and the monster.
     *
     * @param currentMap the current map
     * @param block the block of the character
     * @param character the index of the character in the positions array
     * @return true if the character can move up, false otherwise
     */
    private boolean canMoveUp(MapData currentMap, Block block, int character) {
        if (getPosition(character).row() == 0 || block.wallTop()) {
            return false;
        }

        var nextBlock = currentMap.blocks().
                get(getPosition(character).moveUp().row()).
                get(getPosition(character).moveUp().col());

        return !nextBlock.wallBottom();
    }

    /**
     * Checks if the {@code character} can move right.
     * Characters are the player and the monster.
     *
     * @param currentMap the current map
     * @param block the block of the character
     * @param character the index of the character in the positions array
     * @return true if the character can move right, false otherwise
     */
    private boolean canMoveRight(MapData currentMap, Block block, int character) {
        if  (getPosition(character).col() == currentMap.cols() - 1 || block.wallRight()) {
            return false;
        }

        var nextBlock = currentMap.blocks().
                get(getPosition(character).moveRight().row()).
                get(getPosition(character).moveRight().col());

        return !nextBlock.wallLeft();
    }

    /**
     * Checks if the {@code character} can move down.
     * Characters are the player and the monster.
     *
     * @param currentMap the current map
     * @param block the block of the character
     * @param character the index of the character in the positions array
     * @return true if the character can move down, false otherwise
     */
    private boolean canMoveDown(MapData currentMap, Block block, int character) {
        if (getPosition(character).row() == currentMap.rows() - 1 || block.wallBottom()) {
            return false;
        }

        var nextBlock = currentMap.blocks().
                get(getPosition(character).moveDown().row()).
                get(getPosition(character).moveDown().col());

        return !nextBlock.wallTop();
    }

    /**
     * Checks if the {@code character} can move left.
     * Characters are the player and the monster.
     *
     * @param currentMap the current map
     * @param block the block of the character
     * @param character the index of the character in the positions array
     * @return true if the character can move left, false otherwise
     */
    private boolean canMoveLeft(MapData currentMap, Block block, int character) {
        if (getPosition(character).col() == 0 || block.wallLeft()) {
            return false;
        }

        var nextBlock = currentMap.blocks().
                get(getPosition(character).moveLeft().row()).
                get(getPosition(character).moveLeft().col());

        return !nextBlock.wallRight();
    }

    /**
     * Tries to move the monster towards the player.
     */
    private void tryToMoveMonster() {
        Position playerPos = getPosition(PLAYER);
        Position monsterPos = getPosition(MONSTER);

        int distanceX = playerPos.col() - monsterPos.col();
        int distanceY = playerPos.row() - monsterPos.row();

        int stepsTaken = 0;

        // Move horizontally first
        stepsTaken += moveInDirection(distanceX, Direction.RIGHT, Direction.LEFT, stepsTaken);

        // Move vertically if steps left
        if (stepsTaken < 2) {
            stepsTaken += moveInDirection(distanceY, Direction.DOWN, Direction.UP, stepsTaken);
        }

        // Move horizontally again if steps left
        if (stepsTaken < 2 && distanceX != 0) {
            moveInDirection(distanceX, Direction.RIGHT, Direction.LEFT, stepsTaken);
        }
    }

    /**
     * Moves the monster in the given direction.
     *
     * @param distance the distance to move
     * @param positiveDir the direction to move in positive
     * @param negativeDir the direction to move in negative
     * @param stepsTaken the number of steps taken
     * @return the number of steps taken
     */
    private int moveInDirection(int distance, Direction positiveDir, Direction negativeDir, int stepsTaken) {
        if (distance == 0) return 0;

        Direction direction = distance > 0 ? positiveDir : negativeDir;

        int steps = 0;
        for (var i = 0; i < Math.abs(distance) && stepsTaken + steps < 2; i++) {
            if (isLegalMoveMonster(direction)) {
                moveMonster(getPosition(MONSTER), direction);
                steps++;
            }
        }

        return steps;
    }

    /**
     * Sets the position of the monster after moving.
     *
     * @param monsterPosition the current position of the monster
     * @param direction the direction to move
     */
    private void moveMonster(Position monsterPosition, Direction direction) {
        if (isLegalMoveMonster(direction)) {
            monsterPosition = monsterPosition.move(direction);
            positions[MONSTER].set(monsterPosition);
            Logger.info("Monster moved to {}", positions[MONSTER].get());
        }
    }

    /**
     * Makes a move in the given direction.
     *
     * @param direction the direction to move
     */
    @Override
    public void makeMove(Direction direction) {
        if (isLegalMove(direction)) {
            positions[PLAYER].set(positions[PLAYER].get().move(direction));
            Logger.info("Player moved to {}", positions[PLAYER].get());

            if (isSolved()) {
                Logger.info("Game solved");
            } else {
                tryToMoveMonster();

                if (getPosition(PLAYER).equals(getPosition(MONSTER))) {
                    Logger.info("Game over");
                    init();
                }
            }
        }
    }

    /**
     * Gets the solved property as a read-only boolean property.
     *
     * @return the solved property
     */
    public ReadOnlyBooleanProperty solvedProperty() {
        return solved.getReadOnlyProperty();
    }

    /**
     * Gets the position of the character at the specified index.
     *
     * @param index the index of the character
     * @return the position of the character
     */
    public Position getPosition(int index) {
        return positions[index].get();
    }

    /**
     * Gets the current map.
     *
     * @return the current map
     */
    public MapData getCurrentMap() {
        return maps.getMap(mapIndex);
    }

    /**
     * Clones the game state.
     *
     * @return a clone of the game state
     */
    @Override
    public GameState clone() {
        var clone = new GameState(maps, mapIndex);
        clone.positions[PLAYER].set(getPosition(PLAYER));
        clone.positions[MONSTER].set(getPosition(MONSTER));
        clone.positions[END].set(getPosition(END));
        return clone;
    }

    /**
     * Checks if the game state is equal to the specified object.
     *
     * @param o the object to compare
     * @return true if the game state is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof GameState other)
                && getPosition(PLAYER).equals(other.getPosition(PLAYER))
                && getPosition(END).equals(other.getPosition(END))
                && getPosition(MONSTER).equals(other.getPosition(MONSTER));
    }

    /**
     * Returns the hash code of the game state.
     *
     * @return the hash code of the game state
     */
    @Override
    public int hashCode() {
        return Objects.hash(getPosition(PLAYER), getPosition(END), getPosition(MONSTER));
    }

    /**
     * Returns the string representation of the game state.
     *
     * @return the string representation of the game state
     */
    @Override
    public String toString() {
        var sj = new StringJoiner(",", "[", "]");
        for (var position : positions) {
            sj.add(position.get().toString());
        }
        return sj.toString();
    }

}
