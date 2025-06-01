package app;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Board 
{
    // Constants and variables
    private final int SIZE;
    private final char[][] ships;
    private final char[][] tracking;
    private static final char EMPTY = '-';
    private static final char SHIP = 'S';
    private static final char HIT = 'X';
    private static final char MISS = 'O';
    private final Random rand = new Random();

    // Constants for ship shapes
    private static final int SHAPE_LINE = 0;
    private static final int SHAPE_DIAGONAL = 1;
    private static final int SHAPE_SQUARE = 2;

    // Constructor to initialize the board with a given size
    public Board(int size) {
        SIZE = size;
        ships = new char[SIZE][SIZE];
        tracking = new char[SIZE][SIZE];
        initBoard(ships);
        initBoard(tracking);
    }

    // Initialize the board with empty cells
    // This method fills the board with the EMPTY character to represent unoccupied cells.
    private void initBoard(char[][] board) {
        for (int i = 0; i < SIZE; i++) Arrays.fill(board[i], EMPTY);
    }

    // Show the tracking board (misses and hits)
    public void displayTrackingBoard() {
        displayBoard(tracking);
    }

    // Show where ships are placed (used for debug or reveal)
    public void displayShipBoard() {
        displayBoard(ships);
    }

    // Reveal the ship locations (for game end or testing)
    public void revealShipPositions() {
        System.out.println("\nREVEALING SHIP PLACEMENTS:");
        displayBoard(ships);
    }

    // Prints the board with row and column labels
    private void displayBoard(char[][] board) {
    System.out.print("   ");
    for (int i = 1; i <= SIZE; i++) System.out.printf("%2d ", i);
    System.out.println();
    for (int i = 0; i < SIZE; i++) {
        System.out.printf("%2c ", (char)('A' + i));
        for (int j = 0; j < SIZE; j++) {
            System.out.printf("%2c ", board[i][j]); 
        }
        System.out.println();
    }
}


    // Check if a shot is valid (within bounds and not already shot)
    public boolean isValidShot(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && tracking[row][col] == EMPTY;
    }

    // Process a shot at the given coordinates
    public boolean processShot(int row, int col) {
    if (ships[row][col] == SHIP) 
{
    // Check if the ship is sunk before marking this cell as HIT
    ships[row][col] = HIT;
    tracking[row][col] = HIT;  
    System.out.println("Hit, shoot again!");

     return true;

    //If the shot is a miss
    } else {
        tracking[row][col] = MISS;  // This updates for the computer's tracking
        ships[row][col] = MISS;  // Update the ship board to show a miss
        System.out.println("Miss, next player's turn.");
        return false;
    }

    }
   
    // Check if all ships are sunk
    public boolean allShipsSunk() {
        for (char[] row : ships) {
            for (char cell : row) {
                if (cell == SHIP) return false;
            }
        }
        return true;
    }

    // Methods to place ships manually or randomly
    // This method allows the player to manually place all ships on the board.
    public void manualPlaceAllShips(Scanner scanner) {
        System.out.println("Begin by placing your ships manually onto your board.");
        placeShip(scanner, "Destroyer", 2, SHAPE_SQUARE);
        displayShipBoard();
        placeShip(scanner, "Submarine", 3, SHAPE_DIAGONAL);
        displayShipBoard();
        placeShip(scanner, "Cruiser", 3, SHAPE_LINE);
        displayShipBoard();
    }

    // This method places all ships randomly on the board.
    public void randomPlaceAllShips() {
        placeRandomShip(2, SHAPE_SQUARE);
        placeRandomShip(3, SHAPE_DIAGONAL);
        placeRandomShip(3, SHAPE_LINE);
    }

    // Place a ship on the board based on user input or random placement
    private void placeShip(Scanner scanner, String name, int length, int shape) {
        while (true) {
            if (shape == SHAPE_SQUARE) {
                System.out.println("Enter starting row and column for " + name + " (e.g., A 5):");
            } else if (shape == SHAPE_DIAGONAL) {
                System.out.println("Enter row, column, and diagonal direction for " + name +
                        " (e.g. A 5 NORTHEAST):");
            } else {
                System.out.println("Enter row, column, and direction for " + name + " (e.g., A 5 NORTH):");
            }

            String[] parts = scanner.nextLine().trim().toUpperCase().split("\\s+");
            if ((shape == SHAPE_SQUARE && parts.length != 2) || (shape != SHAPE_SQUARE && parts.length != 3)) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            int row, col;
            try {
                row = parts[0].charAt(0) - 'A';
                col = Integer.parseInt(parts[1]) - 1;
            } catch (Exception e) {
                System.out.println("Invalid row or column. Try again.");
                continue;
            }

            String direction = (shape != SHAPE_SQUARE) ? parts[2] : "";

            if (tryPlaceShip(row, col, length, direction, shape)) break;
            else System.out.println("Invalid placement. Try again.");
        }
    }

    // Place a ship randomly on the board
    // This method randomly places a ship of a given length and shape on the board.
    private void placeRandomShip(int length, int shape) {
        String[] directions = switch (shape) {
            case SHAPE_LINE -> new String[]{"NORTH", "SOUTH", "EAST", "WEST"};
            case SHAPE_DIAGONAL -> new String[]{"NORTHEAST", "NORTHWEST", "SOUTHEAST", "SOUTHWEST"};
            default -> new String[]{"ANY"};
        };

        // Keep trying to place the ship until successful
        while (true) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            String dir = directions[rand.nextInt(directions.length)];
            if (tryPlaceShip(row, col, length, dir, shape)) break;
        }
    }

    // Try to place a ship at the specified coordinates with the given length and direction
    private boolean tryPlaceShip(int row, int col, int length, String direction, int shape) {
        List<int[]> cells = new ArrayList<>();

        
        if (shape == SHAPE_SQUARE) {
            if (row + 1 >= SIZE || col + 1 >= SIZE) return false;
            cells.add(new int[]{row, col});
            cells.add(new int[]{row + 1, col});
            cells.add(new int[]{row, col + 1});
            cells.add(new int[]{row + 1, col + 1});
        } else if (shape == SHAPE_LINE) {
            switch (direction) {
                case "NORTH" -> {
                    if (row - length + 1 < 0) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row - i, col});
                }
                case "SOUTH" -> {
                    if (row + length > SIZE) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row + i, col});
                }
                case "EAST" -> {
                    if (col + length > SIZE) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row, col + i});
                }
                case "WEST" -> {
                    if (col - length + 1 < 0) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row, col - i});
                }
                default -> {
                    return false;
                }
            }
        } else if (shape == SHAPE_DIAGONAL) {
            switch (direction) {
                case "NORTHEAST" -> {
                    if (row - length + 1 < 0 || col + length > SIZE) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row - i, col + i});
                }
                case "NORTHWEST" -> {
                    if (row - length + 1 < 0 || col - length + 1 < 0) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row - i, col - i});
                }
                case "SOUTHEAST" -> {
                    if (row + length > SIZE || col + length > SIZE) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row + i, col + i});
                }
                case "SOUTHWEST" -> {
                    if (row + length > SIZE || col - length + 1 < 0) return false;
                    for (int i = 0; i < length; i++) cells.add(new int[]{row + i, col - i});
                }
                default -> {
                    return false;
                }
            }
        }

        for (int[] cell : cells) {
            if (ships[cell[0]][cell[1]] != EMPTY) return false;
        }
        for (int[] cell : cells) {
            ships[cell[0]][cell[1]] = SHIP;
        }
        return true;
    }

    // Getters for ship and tracking cells
    public char getShipCell(int row, int col) {
        return ships[row][col];
    }

    // Get the tracking cell for a shot
    public char getTrackingCell(int row, int col) {
        return tracking[row][col];
    }
}
