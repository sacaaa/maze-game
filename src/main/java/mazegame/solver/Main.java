package mazegame.solver;

import mazegame.map.Maps;
import mazegame.model.Direction;
import mazegame.model.GameState;
import puzzle.solver.BreadthFirstSearch;

public class Main {

    public static void main(String[] args) {
        // Load the maps
        var maps = new Maps("/mazegame/map/maps.json");

        // Solve the game
        var bfs = new BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new GameState(maps, 1));
    }

}