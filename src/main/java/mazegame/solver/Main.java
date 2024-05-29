package mazegame.solver;

import mazegame.map.Maps;
import mazegame.model.Direction;
import mazegame.model.GameState;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Load the maps
        Maps.loadMaps("/mazegame/map/maps.json");
        var maps = Maps.getInstance();

        // Solve the game
//        var bfs = new BreadthFirstSearch<Direction>();
//        bfs.solveAndPrintSolution(new GameState(maps, 1));

        var gameState = new GameState(maps, 1);
        Scanner scanner = new Scanner(System.in);
        while (!gameState.isSolved()) {
            try {
                var direction = Direction.valueOf(scanner.nextLine());
                gameState.makeMove(direction);
            } catch (Exception e) {
                // Ignore invalid input
            }
        }

    }

}