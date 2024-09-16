/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author shada
 */
public class OWinnerController implements Initializable {

    @FXML
    private Button VBtn_ORestart;
    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to control the joystick thread's running state
    private boolean running = true;
    @FXML
    private Button VBtn_Exit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Start the joystick reading thread
        jsThread = new JoyStickThread();
        jsThread.startJoystick();

        // Start the thread to handle joystick actions like button presses
        startJoystickThread();
    }

    // Method to start the thread that listens for joystick actions
    private synchronized void startJoystickThread() {
        // Initialize the joystick thread
        joystickThread = new Thread(() -> {
            while (running) {
                // Get joystick action
                char action = JoystickReader.getAction();
                // Reset the joystick action after reading it
                JoystickReader.setAction(' ');
                // If an action is detected
                if (action != ' ') {
                    if (action == 'x') {
                        // If the action is 'x', fire the current button (restart the game)
                        Platform.runLater(this::fireCurrentButton);
                    } else if (action == 'f') {
                        // If the action is 'f', fire the exit button
                        Platform.runLater(this::fireExitButton);
                    }
                }
            }
        });
        // Start the joystick thread
        joystickThread.start();
    }

    // Method to simulate clicking the restart button
    private void fireCurrentButton() {
        VBtn_ORestart.fire();
    }

    // Method to simulate clicking the exit button
    private void fireExitButton() {
        VBtn_Exit.fire();
    }

    // Method to handle the restart button click
    @FXML
    private void HFunc_ORestart(ActionEvent event) {
        // Stop the joystick thread
        killThread();
        // Get the current stage (window) and fade out the current scene
        Stage currentStage = (Stage) VBtn_ORestart.getScene().getWindow();
        AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

        // Create a fade-out transition for a smooth scene change
        FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
        fadeOut.setNode(root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // After fade-out completes, load the Menu scene and fade it in
        fadeOut.setOnFinished(e -> {
            try {
                // Load the Menu scene from the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                Parent menuRoot = loader.load();
                Scene menuScene = new Scene(menuRoot);
                currentStage.setTitle("Game Mode Menu");
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

    // Method to handle the exit button click
    @FXML
    private void HFunc_Exit(ActionEvent event) {
        // Stop the joystick thread
        killThread();
        // Get the current stage from the event source (the button)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the stage, which will close the application
        currentStage.close();

    }

    public void killThread() {
        // Stop the loop
        running = false;

        // Stop the joystick thread
        jsThread.stopJoystick();
    }

}
