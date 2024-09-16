package tictactoe;

import GameLogic.JoystickReader;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartController implements Initializable {

    @FXML
    private Button Vbtn_StartGame;
    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to control the joystick thread's running state
    private boolean running = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Start joystick control logic
        jsThread = new JoyStickThread();
        // Start reading joystick actions
        jsThread.startJoystick();
        // Start the thread to handle joystick input actions
        startJoystickThread();

    }

    private synchronized void startJoystickThread() {
        // Initialize and start the joystick thread
        joystickThread = new Thread(() -> {
            while (running) {
                // Get the action performed by the joystick
                char action = JoystickReader.getAction();
                // Reset the action to prevent duplicate handling
                JoystickReader.setAction(' ');

                // If there was an action detected
                if (action != ' ') {
                    if (action == 'x') {
                        // If the action is 'x', trigger the current button (start game)
                        Platform.runLater(this::fireCurrentButton);
                    }
                }
            }
        });
        // Start the joystick thread
        joystickThread.start();
    }

    // Simulate a button click event to start the game when the joystick 'x' action is detected.
    private void fireCurrentButton() {
        Vbtn_StartGame.fire();
    }

    @FXML
    private void HFunc_StartGame(ActionEvent event) {
        // Stop the joystick thread
        killThread();

        // Get the current window (stage) from the button's scene
        Stage currentStage = (Stage) Vbtn_StartGame.getScene().getWindow();
        if (currentStage != null) {
            // Get the root pane of the current scene
            AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

            // Create a fade-out transition for a smooth scene change
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
            fadeOut.setNode(root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // When fade-out is complete, load the new scene (Menu.fxml)
            fadeOut.setOnFinished(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    Parent menuRoot = loader.load();
                    Scene menuScene = new Scene(menuRoot);
                    // Set window title
                    currentStage.setTitle("Game Mode Menu");
                    // Switch to the new scene
                    currentStage.setScene(menuScene);

                    // Create a fade-in transition for the new scene
                    AnchorPane menuPane = (AnchorPane) menuScene.getRoot();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(0));
                    fadeIn.setNode(menuPane);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    // Play the fade-in transition
                    fadeIn.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            // Play the fade-out transition
            fadeOut.play();
        }
    }

    public void killThread() {
        // Stop the loop
        running = false;

        // Stop the joystick thread
        jsThread.stopJoystick();
    }
}
