package app;
import java.util.*;

class Board {
    private final int SIZE;
    private final char[][] ships;
    private final char[][] tracking;
    private static final char EMPTY = '-';
    private static final char SHIP = 'S';
    private static final char HIT = 'X';
    private static final char MISS = 'O';
    private final Random rand = new Random();

    private static final int SHAPE_LINE = 0;
    private static final int SHAPE_DIAGONAL = 1;
    private static final int SHAPE_SQUARE = 2;

    public Board(int size) {
        SIZE = size;
        ships = new char[SIZE][SIZE];
        tracking = new char[SIZE][SIZE];
        initBoard(ships);
        initBoard(tracking);
    }

    private void initBoard(char[][] board) {
        for (int i = 0; i < SIZE; i++) Arrays.fill(board[i], EMPTY);
    }

    public void displayTrackingBoard() {
        displayBoard(tracking);
    }

    public void displayShipBoard() {
        displayBoard(ships);
    }

    public void revealShipPositions() {
        System.out.println("\nREVEALING SHIP PLACEMENTS:");
        displayBoard(ships);
    }

    private void displayBoard(char[][] board) {
        System.out.print("   ");
        for (int i = 1; i <= SIZE; i++) System.out.printf("%2d ", i);
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("%2c ", (char)('A' + i));
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public boolean isValidShot(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && tracking[row][col] == EMPTY;
    }

    public boolean processShot(int row, int col) {
    if (ships[row][col] == SHIP) {
        ships[row][col] = HIT;
        tracking[row][col] = HIT;  
        System.out.println("Hit, shoot again!");
        if (isShipSunk(row, col)) {
            System.out.println("Ship sunk! Keep shooting.");
        }
        return true;
    } else {
        tracking[row][col] = MISS;  // This updates for the computer's tracking
        ships[row][col] = MISS;  // Update the ship board to show a miss
        System.out.println("Miss, next player's turn.");
        return false;
    }

    }

    private boolean isShipSunk(int row, int col) {
    // Scan the board to see if any remaining ship cells exist
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            if (ships[i][j] == SHIP) { // If any ship cell remains, the ship isn't fully sunk
                return false;
            }
        }
    }
    return true; // If no ship cells remain, the ship is sunk
}

    public boolean allShipsSunk() {
        for (char[] row : ships) {
            for (char cell : row) {
                if (cell == SHIP) return false;
            }
        }
        return true;
    }

    public void manualPlaceAllShips(Scanner scanner) {
        System.out.println("Begin by placing your ships manually onto your board.");
        placeShip(scanner, "Destroyer", 2, SHAPE_SQUARE);
        displayShipBoard();
        placeShip(scanner, "Submarine", 3, SHAPE_DIAGONAL);
        displayShipBoard();
        placeShip(scanner, "Cruiser", 3, SHAPE_LINE);
        displayShipBoard();
    }

    public void randomPlaceAllShips() {
        placeRandomShip(2, SHAPE_SQUARE);
        placeRandomShip(3, SHAPE_DIAGONAL);
        placeRandomShip(3, SHAPE_LINE);
    }

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

    private void placeRandomShip(int length, int shape) {
        String[] directions = switch (shape) {
            case SHAPE_LINE -> new String[]{"NORTH", "SOUTH", "EAST", "WEST"};
            case SHAPE_DIAGONAL -> new String[]{"NORTHEAST", "NORTHWEST", "SOUTHEAST", "SOUTHWEST"};
            default -> new String[]{"ANY"};
        };

        while (true) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            String dir = directions[rand.nextInt(directions.length)];
            if (tryPlaceShip(row, col, length, dir, shape)) break;
        }
    }

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

    public char getShipCell(int row, int col) {
        return ships[row][col];
    }

    public char getTrackingCell(int row, int col) {
        return tracking[row][col];
    }
}
