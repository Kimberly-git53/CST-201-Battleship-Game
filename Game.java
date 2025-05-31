package app;
import java.util.*;


class Game {
    private final int SIZE = 10;
    private final Board playerBoard = new Board(SIZE);
    private final Board computerBoard = new Board(SIZE);
    private final Scanner scanner = new Scanner(System.in);
    private final Random rand = new Random();

    
    public void start() {
        System.out.println("Key for Testing");
        computerBoard.randomPlaceAllShips();
        computerBoard.revealShipPositions();
        
        System.out.println("Welcome to Battleship!");
        displayBothBoards();
        playerBoard.manualPlaceAllShips(scanner);
        
        while (true) {
            // Player's turn
            while (true) {
                displayBothBoards();
                System.out.print("Enter shot (e.g., A5): ");
                String input = scanner.nextLine().trim().toUpperCase();
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
            while (true) {
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

    private void displayBothBoards() {
        System.out.println("\nPLAYER BOARD\t\t\t\t\tCOMPUTER BOARD");
        System.out.print("   ");
        for (int i = 1; i <= SIZE; i++) System.out.print(i + (i < 10 ? "  " : " "));
        System.out.print("\t\t   ");
        for (int i = 1; i <= SIZE; i++) System.out.print(i + (i < 10 ? "  " : " "));
        System.out.println();

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
