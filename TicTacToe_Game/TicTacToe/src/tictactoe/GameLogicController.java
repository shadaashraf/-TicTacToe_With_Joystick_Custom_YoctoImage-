/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import GameLogic.ComputerPlayer;
import GameLogic.HumanPlayer;
import GameLogic.JoystickReader;
import GameLogic.TicTacToeGame;
import GameLogic.GameMode;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author doaa
 */
public class GameLogicController implements Initializable {

    @FXML
    private Button box1;
    @FXML
    private Text player1;
    @FXML
    private Text player2;
    @FXML
    private Button box2;
    @FXML
    private Button box3;
    @FXML
    private Button box4;
    @FXML
    private Button box7;
    @FXML
    private Button box5;
    @FXML
    private Button box8;
    @FXML
    private Button box6;
    @FXML
    private Button box9;
    @FXML
    private Button restartBtn;
    @FXML
    private Button endGameBtn;

    Button[] buttons;

    // Index of the button for Player 1
    private int player1Index = 0;

    // Index of the button for Player 2
    private int player2Index = 0;

    // Current selected button index
    private int playerIndex = 0;

    Button selectedButton;

    // Action received from joystick
    char action;
    int[] move;
    String win;

    // Game running status
    private boolean running = true;

    // Thread for handling joystick input
    JoyStickThread jsThread;
    private GameMode mode;
    private char level;

    JoystickReader joystick1;
    JoystickReader joystick2;

    String borderColor = "#e00051;";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the buttons array
        buttons = new Button[]{box1, box2, box3, box4, box5, box6, box7, box8, box9};
        joystick1 = new JoystickReader();
        joystick2 = new JoystickReader();  // Second joystick for two human players

        // Set game mode
        if (JoyStickThread.gameMode == 0) {
            mode = GameMode.PLAYER_VS_COMPUTER;
        } else {
            mode = GameMode.PLAYER_VS_PLAYER;
        }

        // Set difficulty level
        if (JoyStickThread.level == 0) {
            level = 'e';
        } else if (JoyStickThread.level == 1) {
            level = 'm';
        } else if (JoyStickThread.level == 2) {
            level = 'h';
        }

        TicTacToeGame game = new TicTacToeGame(mode, level, joystick1, joystick2);

        jsThread = new JoyStickThread();
        // Start the joystick thread
        jsThread.startJoystick();

        // Set the border color for the initial selected button
        buttons[player1Index].setStyle(buttons[player1Index].getStyle() + "-fx-border-color:  #e00051;");

        // Set the player names
        player1.setText(JoyStickThread.xPlayer);
        player2.setText(JoyStickThread.oPlayer);

        // Start a new thread to handle game updates
        new Thread() {
            public void run() {
                while (running) {
                    // Check if the current player is a human and the game is not over
                    if (TicTacToeGame.getCurrentPlayer() instanceof HumanPlayer && !TicTacToeGame.getGameOver()) {
                        HumanPlayer humanPlayer = (HumanPlayer) TicTacToeGame.getCurrentPlayer();

                        // Get the action from the joystick
                        action = JoystickReader.getAction();

                        // If an action is detected
                        if (action != ' ') {

                            // Get the move based on joystick input
                            move = humanPlayer.getJoystickInput();

                            // Update the border color based on the current player
                            if (humanPlayer == TicTacToeGame.getPlayer1()) {
                                borderColor = "#e00051;";
                                playerIndex = player1Index;

                            } else if (humanPlayer == TicTacToeGame.getPlayer2()) {
                                borderColor = "#800000;";
                                playerIndex = player2Index;
                            }

                            // Handle joystick actions (up, down, left, right, fire, restart)
                            switch (action) {
                                case 'u':
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color:  #3a8fcf;");
                                    if (playerIndex >= 3) {
                                        playerIndex -= 3;
                                    } else {
                                        playerIndex += 6;
                                    }
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color: " + borderColor);
                                    joystick1.setAction(' ');
                                    break;
                                case 'd':
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color:  #3a8fcf;");
                                    if (playerIndex <= 5) {
                                        playerIndex += 3;
                                    } else {
                                        playerIndex -= 6;
                                    }
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color: " + borderColor);
                                    joystick1.setAction(' ');
                                    break;
                                case 'l':
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color:  #3a8fcf;");
                                    if (playerIndex % 3 > 0) {
                                        playerIndex -= 1;
                                    } else {
                                        playerIndex += 2;
                                    }
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color: " + borderColor);
                                    joystick1.setAction(' ');
                                    break;
                                case 'r':
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color:  #3a8fcf;");
                                    if (playerIndex % 3 < 2) {
                                        playerIndex += 1;
                                    } else {
                                        playerIndex -= 2;
                                    }
                                    buttons[playerIndex].setStyle(buttons[playerIndex].getStyle() + "-fx-border-color: " + borderColor);
                                    joystick1.setAction(' ');
                                    break;
                                case 'f':
                                    // Handle end game action
                                    Platform.runLater(() -> endButton());
                                    break;
                                case 'z':
                                    // Handle restart game action
                                    Platform.runLater(() -> restartButton());
                                    break;
                                default:
                                    break;
                            }

                            // Update the player index based on the current player
                            if (humanPlayer == TicTacToeGame.getPlayer1()) {
                                player1Index = playerIndex;

                            } else if (humanPlayer == TicTacToeGame.getPlayer2()) {
                                player2Index = playerIndex;
                            }

                            // Execute the player's move if the action is fired
                            if (humanPlayer.isFired()) {
                                executePlayerMove(humanPlayer, move);
                            }
                        }
                    } else if (TicTacToeGame.getCurrentPlayer() instanceof ComputerPlayer && !TicTacToeGame.getGameOver()) {
                        // Handle computer player move
                        handleComputerMove();
                    } else {
                        // Check the game result and handle win or draw scenarios
                        win = TicTacToeGame.checkWin();
                        if ("false".equals(win)) {
                            handleDrawScenario();
                        } else {
                            highlightWinningButtons(win);
                            transitionToWinnerScene();
                        }
                        // Reset the game for a new round
                        TicTacToeGame.resetGame();
                    }
                }
            }
        }.start();
    }

    // Method to trigger the end game button
    public void endButton() {
        endGameBtn.fire();
    }

    // Method to trigger the restart button
    public void restartButton() {
        restartBtn.fire();
    }

    // Event handler for the restart button
    @FXML
    private void restart(ActionEvent event) {
        // Stop the joystick thread
        killThread();
        // Get the current stage from the restart button's scene
        Stage currentStage = (Stage) restartBtn.getScene().getWindow();
        if (currentStage != null) {
            // Get the root node of the current scene
            AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

            // Create a fade-out transition to smoothly hide the current scene
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
            fadeOut.setNode(root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Action to perform after fade-out completes
            fadeOut.setOnFinished(e -> {
                try {
                    // Reload the same scene (GameLogic.fxml) to reset the game state
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLogic.fxml"));
                    Parent menuRoot = loader.load();
                    Scene menuScene = new Scene(menuRoot);

                    // Set up a fade-in transition to smoothly display the reloaded scene
                    AnchorPane menuPane = (AnchorPane) menuScene.getRoot();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(0));
                    fadeIn.setNode(menuPane);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    // Update the current stage with the new scene and play the fade-in effect
                    currentStage.setTitle("X-O Game");
                    currentStage.setScene(menuScene);
                    fadeIn.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            // Play the fade-out effect
            fadeOut.play();

            // Reset game state: Clear button texts and reset styles
            TicTacToeGame.resetGame();
            for (Button button : buttons) {
                button.setText("");
                button.setStyle(button.getStyle() + "-fx-border-color:  #3a8fcf; -fx-background-color:  transparent;");
            }
            buttons[0].setStyle(buttons[0].getStyle() + "-fx-border-color:  #e00051;");
        }
    }

    // Event handler for the end game button
    @FXML
    private void endGame(ActionEvent event) {
        // Stop the joystick thread
        killThread();
        Stage currentStage = (Stage) endGameBtn.getScene().getWindow();
        if (currentStage != null) {
            // Transition to the Menu scene
            performFadeTransition(currentStage, "Menu.fxml", "Game Mode Menu");
        }
    }

    // Executes the player's move based on joystick input
    public void executePlayerMove(HumanPlayer humanPlayer, int[] move) {
        if (humanPlayer.isFired()) {
            // Make the move in the game
            humanPlayer.makeMove(move[1], move[0]);
            Platform.runLater(() -> {

                // Calculate button index
                int index = move[1] * 3 + move[0];
                Button selectedButton = buttons[index];
                if (selectedButton.getText().isEmpty()) {

                    // Set button text to player's symbol
                    selectedButton.setText(humanPlayer.getSymbol());

                    // Switch to the next player
                    TicTacToeGame.togglePlayer();
                }
            });

            // Reset fired state
            humanPlayer.releaseFired();
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameLogicController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Handles the move for a computer player
    public void handleComputerMove() {
        ComputerPlayer computerPlayer = (ComputerPlayer) TicTacToeGame.getCurrentPlayer();
        int[] move = computerPlayer.getBestMove();
        // Make the move in the game
        computerPlayer.makeMove(move[0], move[1]);

        Platform.runLater(() -> {

            // Calculate button index
            int index = move[0] * 3 + move[1];
            Button selectedButton = buttons[index];
            if (selectedButton.getText().isEmpty()) {

                // Set button text to computer's symbol
                selectedButton.setText("O");

                // Switch to the next player
                TicTacToeGame.togglePlayer();
            }
        });
        try {
            sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameLogicController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the scenario where the game ends in a draw
    private void handleDrawScenario() {
        // Pause before showing the draw scene
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameLogicController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Stop the joystick thread
        killThread();
        Stage currentStage = (Stage) endGameBtn.getScene().getWindow();
        if (currentStage != null) {
            // Transition to the draw scene
            performFadeTransition(currentStage, "Draw.fxml", "Tie");
        }
    }

    // Highlights the buttons corresponding to the winning combination
    private void highlightWinningButtons(String win) {
        // Parse winning data
        String[] data = win.split("\\.");
        switch (data[0]) {
            case "r":
                // Highlight winning row
                highlightRow(Integer.parseInt(data[1]));
                break;
            case "c":
                // Highlight winning column
                highlightColumn(Integer.parseInt(data[1]));
                break;
            case "d1":
                // Highlight diagonal from top-left to bottom-right
                highlightDiagonal1();
                break;
            case "d2":
                // Highlight diagonal from top-right to bottom-left
                highlightDiagonal2();
                break;
        }

        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameLogicController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Highlights the buttons in the specified row
    private void highlightRow(int rowIndex) {
        int baseIndex = rowIndex * 3;
        buttons[baseIndex].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[baseIndex + 1].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[baseIndex + 2].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
    }

    // Highlights the buttons in the specified column
    private void highlightColumn(int colIndex) {
        buttons[colIndex].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[colIndex + 3].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[colIndex + 6].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
    }

    // Highlights the diagonal from top-left to bottom-right
    private void highlightDiagonal1() {
        buttons[0].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[4].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[8].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
    }

    // Highlights the diagonal from top-right to bottom-left
    private void highlightDiagonal2() {
        buttons[2].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[4].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
        buttons[6].setStyle(buttons[player1Index].getStyle() + "-fx-background-color: #7cfc00;");
    }

    // Transitions to the winner scene based on the current player
    private void transitionToWinnerScene() {
        if (TicTacToeGame.getCurrentPlayer() == TicTacToeGame.getPlayer1()) {
            killThread();
            Stage currentStage = (Stage) endGameBtn.getScene().getWindow();
            if (currentStage != null) {
                performFadeTransition(currentStage, "OWinner.fxml", "O Winner");
            }
        } else {
            killThread();
            Stage currentStage = (Stage) endGameBtn.getScene().getWindow();
            if (currentStage != null) {
                performFadeTransition(currentStage, "XWinner.fxml", "X Winner");
            }
        }
    }

    private void performFadeTransition(Stage currentStage, String fxmlFile, String title) {
        // Get the root of the current scene
        AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

        // Create a fade-out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
        fadeOut.setNode(root);
        fadeOut.setFromValue(1.0);  // Start fully visible
        fadeOut.setToValue(0.0);    // End fully invisible

        // Action to perform after fade-out completes
        fadeOut.setOnFinished(e -> {
            Platform.runLater(() -> {
                try {
                    // Load the new FXML scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent menuRoot = loader.load();

                    // Create a new scene with the loaded FXML
                    Scene menuScene = new Scene(menuRoot);

                    // Set up a fade-in transition
                    AnchorPane menuPane = (AnchorPane) menuScene.getRoot();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(0));
                    fadeIn.setNode(menuPane);
                    fadeIn.setFromValue(0.0);  // Start fully invisible
                    fadeIn.setToValue(1.0);    // End fully visible

                    // Switch to the new scene and play the fade-in effect
                    currentStage.setScene(menuScene);
                    currentStage.setTitle(title);

                    fadeIn.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        // Play the fade-out effect
        fadeOut.play();
    }

    // Method to stop the joystick thread and set running flag to false
    public void killThread() {
        running = false;
        jsThread.stopJoystick();
    }

}
