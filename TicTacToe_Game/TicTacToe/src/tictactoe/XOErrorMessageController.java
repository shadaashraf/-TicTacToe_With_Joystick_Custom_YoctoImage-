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
public class XOErrorMessageController implements Initializable {

    @FXML
    private Button VBtnXO_GotIt;
    static Thread joy;
    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to keep the joystick thread running
    private boolean running = true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jsThread = new JoyStickThread();
        // Start joystick input handling
        jsThread.startJoystick();

        // Start the thread that handles joystick actions
        startJoystickThread();

        // Request focus on the "Got It" button to make it respond to joystick input
        VBtnXO_GotIt.requestFocus();
    }

    private synchronized void startJoystickThread() {
        // Create a new thread to handle joystick input
        joystickThread = new Thread(() -> {
            while (running) {
                // Read the action from the joystick
                char action = JoystickReader.getAction();

                // Reset the joystick action
                JoystickReader.setAction(' ');

                // If a valid action is detected
                if (action != ' ') {
                    // If the action is 'x' (a button press), trigger the current button
                    if (action == 'x') {
                        Platform.runLater(this::fireCurrentButton);

                    }

                }
            }
        });
        // Start the joystick thread
        joystickThread.start();
    }

    // Simulate the "Got It" button click event when joystick action is detected
    private void fireCurrentButton() {
        VBtnXO_GotIt.fire();
    }

    @FXML
    private void HFunc_XOGotIt(ActionEvent event) {
        // Stop the joystick thread to prevent background processing
        killThread();

        // Get the current window (stage) from the button's scene
        Stage currentStage = (Stage) VBtnXO_GotIt.getScene().getWindow();
        if (currentStage != null) {
            AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

            // Fade-out transition for the current scene
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
            fadeOut.setNode(root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Once fade-out is complete, load the new scene
            fadeOut.setOnFinished(e -> {
                try {
                    // Load the Naming.fxml file for the new scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Naming.fxml"));
                    Parent menuRoot = loader.load();
                    Scene menuScene = new Scene(menuRoot);
                    currentStage.setTitle("Player Naming Stage");
                    currentStage.setScene(menuScene);

                    // Fade-in transition for the new scene
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
