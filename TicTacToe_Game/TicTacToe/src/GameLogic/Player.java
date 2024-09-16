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
public abstract class Player {

    // Symbol representing the player's move ("X" or "O")
    protected String symbol;

    public Player(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    // Make a move on the game board if the move is valid.
    public static void makeMove(int x, int y) {
        String[][] board = TicTacToeGame.getBoard();
        if (TicTacToeGame.isValidMove(x, y)) {
            board[x][y] = TicTacToeGame.getCurrentPlayer().getSymbol();
            if (TicTacToeGame.checkWin() != "false") {
                TicTacToeGame.setGameOver(true);
            } else if (TicTacToeGame.isBoardFull()) {
                TicTacToeGame.setGameOver(true);
                //resetGame();
            }
        }
    }

}
