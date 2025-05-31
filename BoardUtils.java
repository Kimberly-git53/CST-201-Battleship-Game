package app;

class BoardUtils {
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

    public static int[] parseInput(String input) {
        int row = input.charAt(0) - 'A';
        int col = Integer.parseInt(input.substring(1)) - 1;
        return new int[]{row, col};
    }
}
