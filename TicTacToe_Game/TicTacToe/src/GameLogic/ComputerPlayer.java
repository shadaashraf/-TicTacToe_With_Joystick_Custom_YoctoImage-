/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.Random;

/**
 *
 * @author doaa
 */
public class ComputerPlayer extends Player {

    // Difficulty level of the computer player
    private static char difficultyLevel;

    public ComputerPlayer(String symbol, char difficultyLevel) {
        super(symbol);
        this.difficultyLevel = difficultyLevel;
    }

    public char getDifficultyLevel() {
        return difficultyLevel;
    }

    // Determine the best move for the computer player based on its difficulty level.
    public static int[] getBestMove() {
        int[] bestMove = new int[2];
        if (difficultyLevel == 'e') {
            // Easy difficulty: random move
            bestMove = getRandomMove();
        } else if (difficultyLevel == 'm') {
            // Medium difficulty: minimax with depth limit
            bestMove = findBestMove(4);
        } else if (difficultyLevel == 'h') {
            // Hard difficulty: minimax with no depth limit
            bestMove = findBestMove(Integer.MAX_VALUE);
        }
        return bestMove;
    }

    // Generate a random move for the computer player
    private static int[] getRandomMove() {
        int[] bestMove = new int[2];
        Random random = new Random();
        do {
            // Random row index
            bestMove[0] = random.nextInt(3);
            // Random column index
            bestMove[1] = random.nextInt(3);
        } while (!TicTacToeGame.isValidMove(bestMove[0], bestMove[1])); // Ensure the move is valid
        return bestMove;
    }

    // Find the best move for the computer player using the minimax algorithm
    public static int[] findBestMove(int depthlimit) {
        // Initialize bestScore for maximizing player
        int bestScore = Integer.MIN_VALUE;
        String[][] board = TicTacToeGame.getBoard();
        int[] bestMove = new int[2];
        int moveScore;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    // Simulate move
                    board[i][j] = TicTacToeGame.getPlayer2().getSymbol();
                    // Evaluate move
                    moveScore = minimax(0, depthlimit, false);
                    // Undo move
                    board[i][j] = "";
                    // Update best move if better score is found
                    if (moveScore > bestScore) {
                        bestScore = moveScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    // Minimax algorithm to evaluate the best move for the computer player
    private static int minimax(int depth, int depthlimit, boolean isMaximizing) {
        if (TicTacToeGame.checkWin() != "false") {
            // Return score based on who wins
            return isMaximizing ? -10 + depth : 10 - depth;
        }
        if (TicTacToeGame.isBoardFull() || depth >= depthlimit) {
            // Return 0 for a draw or depth limit reached
            return 0;
        }

        // Recursively evaluate moves
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            String[][] board = TicTacToeGame.getBoard();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].isEmpty()) {
                        // Simulate move
                        board[i][j] = TicTacToeGame.getPlayer2().getSymbol();
                        // Recursively minimize
                        int score = minimax(depth + 1, depthlimit, false);
                        // Undo move
                        board[i][j] = "";
                        // Maximize score
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            String[][] board = TicTacToeGame.getBoard();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].isEmpty()) {
                        // Simulate move
                        board[i][j] = TicTacToeGame.getPlayer1().getSymbol();
                        // Recursively maximize
                        int score = minimax(depth + 1, depthlimit, true);
                        // Undo move
                        board[i][j] = "";
                        // Minimize score
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

}
