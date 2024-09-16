/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import GameLogic.JoystickReader;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class XErrorMessageController implements Initializable {

    @FXML
    private Button VBtn_GotIt;
    static Thread joy;
    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to control the joystick thread's execution
    private boolean running = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Start joystick handling logic
        jsThread = new JoyStickThread();
        // Begin monitoring joystick input
        jsThread.startJoystick();

        // Start the joystick thread to process actions
        startJoystickThread();

        // Set focus on the "Got It" button so it's ready to respond to input
        VBtn_GotIt.requestFocus();

    }

    private synchronized void startJoystickThread() {
        // Create and start the joystick thread
        joystickThread = new Thread(() -> {
            while (running) {
                // Get the action from the joystick
                char action = JoystickReader.getAction();
                // Reset the action to prevent repeated processing
                JoystickReader.setAction(' ');

                // If an action is detected
                if (action != ' ') {
                    // If the action is 'x' trigger the button click
                    if (action == 'x') {
                        Platform.runLater(this::fireCurrentButton);

                    }
                }
            }
        });
        // Start the joystick thread
        joystickThread.start();
    }

    // Simulate the "Got It" button click event when the joystick action is detected.
    private void fireCurrentButton() {
        VBtn_GotIt.fire();
    }

    @FXML
    private void HFunc_GotIt(ActionEvent event) {
        // Stop the joystick thread to avoid running background processes
        killThread();

        // Get the current window (stage) from the button's scene
        Stage currentStage = (Stage) VBtn_GotIt.getScene().getWindow();
        if (currentStage != null) {
            // Get the root pane of the current scene
            AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

            // Create a fade-out transition for a smooth scene change
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
            fadeOut.setNode(root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // When the fade-out is complete, load the new scene 
            fadeOut.setOnFinished(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Name_with_AI.fxml"));
                    Parent menuRoot = loader.load();
                    Scene menuScene = new Scene(menuRoot);
                    currentStage.setTitle("X-Player Naming Stage");
                    currentStage.setScene(menuScene);

                    // Create a fade-in transition for the new scene
                    AnchorPane menuPane = (AnchorPane) menuScene.getRoot();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(0));
                    fadeIn.setNode(menuPane);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    // Play the fade-in animation
                    fadeIn.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            // Play the fade-out animation
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
