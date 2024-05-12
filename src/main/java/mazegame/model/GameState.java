package mazegame.model;

import lombok.Getter;
import mazegame.map.Block;
import mazegame.map.MapData;
import mazegame.map.Maps;
import puzzle.State;

import java.util.EnumSet;
import java.util.Set;

@Getter
public class GameState implements State<Direction> {

    private final Maps maps;

    private final int mapIndex = 1;

    private Position[] positions;

    public static final int PLAYER = 0;

    public static final int MONSTER = 1;

    public static final int END = 2;

    public GameState(Maps maps) {
        this.maps = maps;
        this.positions = new Position[3];
        init();
    }

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

    @Override
    public boolean isSolved() {
        return getPosition(PLAYER).equals(getPosition(END));
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
        if (playerBlock.wallTop()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveUp().row()).
                get(getPosition(PLAYER).moveUp().col());

        return !nextBlock.wallBottom();
    }

    private boolean canMoveRight(Block playerBlock) {
        if (playerBlock.wallRight()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveRight().row()).
                get(getPosition(PLAYER).moveRight().col());

        return !nextBlock.wallLeft();
    }

    private boolean canMoveDown(Block playerBlock) {
        if (playerBlock.wallBottom()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveDown().row()).
                get(getPosition(PLAYER).moveDown().col());

        return !nextBlock.wallTop();
    }

    private boolean canMoveLeft(Block playerBlock) {
        if (playerBlock.wallLeft()) {
            return false;
        }

        var nextBlock = maps.getMap(mapIndex).blocks().
                get(getPosition(PLAYER).moveLeft().row()).
                get(getPosition(PLAYER).moveLeft().col());

        return !nextBlock.wallRight();
    }

    @Override
    public void makeMove(Direction direction) {
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
        return copy;
    }
}
