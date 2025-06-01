package app;
// Utility class for Battleship game
// This class contains methods to validate and parse user input for the Battleship game.
class BoardUtils {
   // Check if user input (e.g., A5) is valid
// This method checks if the input string is in the correct format and within the valid range for the game board.
    public static boolean isValidInput(String input) {
        if (input.length() < 2 || input.length() > 3) return false;
        char rowChar = input.charAt(0);
        if (rowChar < 'A' || rowChar > 'J') return false;
        try {
            int col = Integer.parseInt(input.substring(1));
            return col >= 1 && col <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
// Parse user input (e.g., A5) into row and column indices
// This method converts the input string into an array of integers representing the row and column indices.
    public static int[] parseInput(String input) {
        int row = input.charAt(0) - 'A';
        int col = Integer.parseInt(input.substring(1)) - 1;
        return new int[]{row, col};
    }
}
