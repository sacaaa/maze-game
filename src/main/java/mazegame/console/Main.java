package mazegame.console;

import mazegame.map.Maps;
import mazegame.model.Direction;
import mazegame.model.GameState;
import puzzle.State;
import puzzle.solver.BreadthFirstSearch;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Load the maps
        Maps.loadMaps("/maps/maps.json");
        var maps = Maps.getInstance();

        // Create a new game state
        var gameState = new GameState(maps);

//        // Solve the game
//        var solve = new BreadthFirstSearch<Direction>()
//                .solveAndPrintSolution(gameState);

        Scanner scanner = new Scanner(System.in);
        while (!gameState.isSolved()) {
            gameState.printState();

            try {
                var direction = Direction.valueOf(scanner.nextLine());
                gameState.makeMove(direction);
            } catch (Exception e) {
                // Ignore invalid input
            }
        }
    }
}