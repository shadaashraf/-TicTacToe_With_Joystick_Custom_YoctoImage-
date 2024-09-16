/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

/**
 *
 * @author doaa
 */
public class TicTacToeGame {

    // Game board represented as a 2D array
    private static String[][] board;
    private static Player player1, player2;

    // The player whose turn it is
    private static Player currentPlayer;

    private static GameMode gameMode;
    private JoystickReader joystick1, joystick2;

    // Stores the last move made
    private static int[] move;

    // Flag indicating if the game is over
    private static boolean gameOver = false;

    public TicTacToeGame(GameMode gameMode, char difficultyLevel, JoystickReader joystick1, JoystickReader joystick2) {
        // Initialize the game board
        this.board = new String[3][3];

        // Reset the game state
        resetGame();
        this.gameMode = gameMode;
        this.joystick1 = joystick1;
        this.joystick2 = joystick2;

        // Create the first player
        this.player1 = new HumanPlayer("X", joystick1);
        if (gameMode == GameMode.PLAYER_VS_COMPUTER) {
            // Create the computer player
            this.player2 = new ComputerPlayer("O", difficultyLevel);
        } else {
            // Create the second human player
            this.player2 = new HumanPlayer("O", joystick2);
        }

        // Player 1 starts the game
        this.currentPlayer = player1;
    }

    // Check if the move is valid (the cell is empty)
    public static boolean isValidMove(int x, int y) {
        return board[x][y].isEmpty();
    }

    // Check if the board is full (no empty cells)
    public static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Toggles the current player.
    public static void togglePlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    // Check for a win condition by evaluating rows, columns, and diagonals
    public static String checkWin() {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].isEmpty()) {
                // Row win
                return "r." + i;
            }
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].isEmpty()) {
                // Column win
                return "c." + i;
            }
        }

        // Check diagonals
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].isEmpty()) {
            // Diagonal from top-left to bottom-right
            return "d1";
        }
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].isEmpty()) {
            // Diagonal from top-right to bottom-left
            return "d2";
        }
        // No win
        return "false";
    }

    public static void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
        // Player 1 always starts first
        currentPlayer = player1;
        gameOver = false;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static boolean getGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gOver) {
        gameOver = gOver;
    }

    public static String[][] getBoard() {
        return board;
    }

    public static Player getPlayer1() {
        return player1;
    }

    public static Player getPlayer2() {
        return player2;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    public static int[] getMove() {
        return move;
    }

}
