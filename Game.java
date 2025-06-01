package app;
import java.util.*;

// Battleship Game Implementation
// This code implements a simple text-based Battleship game where a player can play against the computer.
class Game 
{
    // Constants
    // The game board size is set to 10x10
    private final int SIZE = 10;
    private final Board playerBoard = new Board(SIZE);
    private final Board computerBoard = new Board(SIZE);
    private final Scanner scanner = new Scanner(System.in);
    private final Random rand = new Random();

    // Method that start the game
    // It initializes the game, places ships randomly for the computer, and allows the player to place their ships manually.
    public void start() 
    {
        // Display Computer's ships for testing purposes
        System.out.println("Key for Testing");
        computerBoard.randomPlaceAllShips();
        computerBoard.revealShipPositions();
        
        // Print boards to begin the game
        System.out.println("Welcome to Battleship!");
        displayBothBoards();
        playerBoard.manualPlaceAllShips(scanner);
        
        // Game loop
        while (true) 
        {
            // Player's turn
            while (true) 
            {
                // Display both boards and prompt for input
                displayBothBoards();
                System.out.print("Enter shot (e.g., A5): ");
                String input = scanner.nextLine().trim().toUpperCase();
                // Validate input and process shot
                if (!BoardUtils.isValidInput(input)) {
                    System.out.println("Invalid input. Use format A-J followed by 1-10. (No space)");
                    continue;
                }
                int[] coords = BoardUtils.parseInput(input);
                if (!computerBoard.isValidShot(coords[0], coords[1])) {
                    System.out.println("Invalid shot. Try again.");
                    continue;
                }
                boolean hit = computerBoard.processShot(coords[0], coords[1]);
                
                if (computerBoard.allShipsSunk()) {
                    System.out.println("You win! All enemy ships sunk!");
                    computerBoard.revealShipPositions();
                    return;
                }
                if (!hit) break;
            }

            // Computer's turn
            while (true) 
            {
                // Display both boards and make a random shot as the computer
                displayBothBoards();
                int x = rand.nextInt(SIZE);
                int y = rand.nextInt(SIZE);
                if (!playerBoard.isValidShot(x, y)) continue;
                System.out.println("Computer shoots at: " + (char) ('A' + x) + (y + 1));
                boolean hit = playerBoard.processShot(x, y);
                
                if (playerBoard.allShipsSunk()) {
                    System.out.println("Computer wins!");
                    playerBoard.revealShipPositions();
                    return;
                }
                if (!hit) break;
                
            }
        }
    }

    // Method to display both player and computer boards
    private void displayBothBoards() {
        System.out.println("\nPLAYER BOARD\t\t\t\t\tCOMPUTER BOARD");
        System.out.print("   ");
        // Print column headers for both boards
        for (int i = 1; i <= SIZE; i++) System.out.print(i + (i < 10 ? "  " : " "));
        System.out.print("\t\t   ");
        for (int i = 1; i <= SIZE; i++) System.out.print(i + (i < 10 ? "  " : " "));
        System.out.println();
        
        // Print each row of both boards
        for (int i = 0; i < SIZE; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + "  ");
            for (int j = 0; j < SIZE; j++) System.out.print(playerBoard.getShipCell(i, j) + "  ");
            System.out.print("\t\t" + rowLabel + "  ");
            for (int j = 0; j < SIZE; j++) System.out.print(computerBoard.getTrackingCell(i, j) + "  ");
            System.out.println();
        }
    }
}
