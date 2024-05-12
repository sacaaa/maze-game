package mazegame;

import mazegame.map.Maps;
import mazegame.model.GameState;

import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        Maps.loadMaps("/maps/maps.json");
        var maps = Maps.getInstance();
        var game = new GameState(maps);
        System.out.println(game.getLegalMoves());
    }

}