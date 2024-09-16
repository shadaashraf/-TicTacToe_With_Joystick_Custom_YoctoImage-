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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class OErrorMessageController implements Initializable {

    @FXML
    private Button Vbtn_OGitIt;
    private Thread joystickThread;
    private JoyStickThread jsThread;
    private boolean running = true;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize and start the joystick handling thread.
        jsThread = new JoyStickThread();
        jsThread.startJoystick();

        // Start the thread that listens for joystick actions.
        startJoystickThread();

        // Automatically request focus on the "Got it" button.
        Vbtn_OGitIt.requestFocus();
    }

    // Starts a thread that continuously listens for joystick input When the 'x' action is detected, it triggers the button action.
    private synchronized void startJoystickThread() {
        // Create and start a new thread for joystick input.
        joystickThread = new Thread(() -> {
            while (running) {
                // Get joystick action
                char action = JoystickReader.getAction();
                // Reset the joystick action after reading it
                JoystickReader.setAction(' ');

                // If a valid action is detected
                if (action != ' ') {
                    if (action == 'x') {
                        // Fire the currently focused button (on 'x' press).
                        Platform.runLater(this::fireCurrentButton);
                    }

                }
            }
        });
        joystickThread.start();
    }

    // Trigges the click event of the currently focused button (Got it)
    private void fireCurrentButton() {
        Vbtn_OGitIt.fire();
    }

    // Handle the "Got it" button click event.
    @FXML
    private void HFunc_OGotit(ActionEvent event) {
        // Stop the joystick thread.
        killThread();
        // Get the current stage (window).
        Stage currentStage = (Stage) Vbtn_OGitIt.getScene().getWindow();

        // If the stage is valid, proceed with transition
        if (currentStage != null) {
            // Get the root of the current scene
            AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

            // Create a fade-out transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
            fadeOut.setNode(root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Action to perform after fade-out completes
            fadeOut.setOnFinished(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Naming.fxml"));
                    Parent menuRoot = loader.load();

                    // Create a new scene with the loaded FXML
                    Scene menuScene = new Scene(menuRoot);

                    // Set up a fade-in transition
                    AnchorPane menuPane = (AnchorPane) menuScene.getRoot();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(0));
                    fadeIn.setNode(menuPane);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    // Switch to the new scene and play the fade-in effect
                    currentStage.setTitle("Players Naming Stage");
                    currentStage.setScene(menuScene);

                    fadeIn.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
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
