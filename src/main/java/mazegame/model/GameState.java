package mazegame.model;

import lombok.Getter;
import mazegame.map.Block;
import mazegame.map.Maps;
import puzzle.State;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

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
    private final int mapIndex = 1;

    /**
     * The positions of the player, monster, and end.
     */
    private Position[] positions;

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
    public GameState(Maps maps) {
        this.maps = maps;
        this.positions = new Position[3];
        init();
    }

    /**
     * Initializes the positions of the player, monster, and end.
     */
    private void init() {
        var map = maps.getMap(mapIndex);

        for (int i = 0; i < map.rows(); i++) {
            for (int j = 0; j < map.cols(); j++) {
                var block = map.blocks().get(i).get(j);

                if (block.start()) {
                    positions[PLAYER] = new Position(i, j);
                } else if (block.end()) {
                    positions[END] = new Position(i, j);
                } else if (block.monster()) {
                    positions[MONSTER] = new Position(i, j);
                }
            }
        }
    }

    public Position getPosition(int index) {
        return positions[index];
    }

    public void printState() {
        var map = maps.getMap(mapIndex);

        for (int i = 0; i < map.rows(); i++) {
            for (int j = 0; j < map.cols(); j++) {
                var position = new Position(i, j);

                if (position.equals(getPosition(PLAYER))) {
                    System.out.print("P ");
                } else if (position.equals(getPosition(MONSTER))) {
                    System.out.print("M ");
                } else if (position.equals(getPosition(END))) {
                    System.out.print("E ");
                } else {
                    System.out.print(map.blocks().get(i).get(j) + " ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public boolean isSolved() {
        return getPosition(PLAYER).equals(getPosition(END))
                || getPosition(PLAYER).equals(getPosition(MONSTER));
    }

    @Override
    public boolean isLegalMove(Direction direction) {
        var playerBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).row()).
                get(getPosition(PLAYER).col());

        return switch (direction) {
            case UP -> canMoveUp(playerBlock);
            case RIGHT -> canMoveRight(playerBlock);
            case DOWN -> canMoveDown(playerBlock);
            case LEFT -> canMoveLeft(playerBlock);
        };
    }

    private boolean canMoveUp(Block playerBlock) {
        if (getPosition(PLAYER).row() == 0 || playerBlock.wallTop()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveUp().row()).
                get(getPosition(PLAYER).moveUp().col());

        return !nextBlock.wallBottom();
    }

    private boolean canMoveRight(Block playerBlock) {
        if  (getPosition(PLAYER).col() == maps.getMap(mapIndex).cols() - 1 || playerBlock.wallRight()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveRight().row()).
                get(getPosition(PLAYER).moveRight().col());

        return !nextBlock.wallLeft();
    }

    private boolean canMoveDown(Block playerBlock) {
        if (getPosition(PLAYER).row() == maps.getMap(mapIndex).rows() - 1 || playerBlock.wallBottom()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveDown().row()).
                get(getPosition(PLAYER).moveDown().col());

        return !nextBlock.wallTop();
    }

    private boolean canMoveLeft(Block playerBlock) {
        if (getPosition(PLAYER).col() == 0 || playerBlock.wallLeft()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveLeft().row()).
                get(getPosition(PLAYER).moveLeft().col());

        return !nextBlock.wallRight();
    }

    @Override
    public void makeMove(Direction direction) {
        if (isLegalMove(direction)) {
            positions[PLAYER] = getPosition(PLAYER).move(direction);
        }
    }

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

    @Override
    public GameState clone() {
        GameState copy;
        try {
            copy = (GameState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.positions = positions.clone();
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof GameState other) && Arrays.equals(positions, other.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }
}
