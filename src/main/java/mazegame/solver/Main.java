package mazegame.solver;

import mazegame.map.Maps;
import mazegame.model.Direction;
import mazegame.model.GameState;
import puzzle.solver.BreadthFirstSearch;

public class Main {

    public static void main(String[] args) {
        // Load the maps
        Maps.loadMaps("/mazegame/map/maps.json");
        var maps = Maps.getInstance();

        // Solve the game
        var bfs = new BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new GameState(maps, 1));
    }

}