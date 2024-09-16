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
public class DrawController implements Initializable {

    @FXML
    private Button VBtn_Draw;
    @FXML
    private Button VBtn_Exit;

    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to control the joystick thread execution
    private boolean running = true;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the joystick thread
        jsThread = new JoyStickThread();
        jsThread.startJoystick();

        // Start the joystick input handling thread
        startJoystickThread();
    }

    private synchronized void startJoystickThread() {

        joystickThread = new Thread(() -> {
            while (running) {
                // Get the latest action from the JoystickReader
                char action = JoystickReader.getAction();

                // Reset the action after processing
                JoystickReader.setAction(' ');
                if (action != ' ') {
                    // Perform action based on the joystick input
                    if (action == 'x') {
                        // Trigger the draw button action
                        Platform.runLater(this::fireCurrentButton);
                    } else if (action == 'f') {
                        // Trigger the exit button action
                        Platform.runLater(this::fireExitButton);
                    }
                }

            }
        });
        // Start the joystick input thread
        joystickThread.start();
    }

    public void killThread() {
        // Stop the thread loop
        running = false;

        // Stop joystick operations
        jsThread.stopJoystick();

    }

    private void fireCurrentButton() {
        VBtn_Draw.fire();
    }

    private void fireExitButton() {
        VBtn_Exit.fire();
    }

    // Handle the draw button action event
    @FXML
    private void HFunc_Draw(ActionEvent event) {
        // Stop the joystick thread
        killThread();

        // Get the current stage and perform a scene transition
        Stage currentStage = (Stage) VBtn_Draw.getScene().getWindow();
        if (currentStage != null) {
            AnchorPane root = (AnchorPane) currentStage.getScene().getRoot();

            // Create and configure a fade-out transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0));
            fadeOut.setNode(root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                try {
                    // Load the new scene and transition to it
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    Parent menuRoot = loader.load();
                    Scene menuScene = new Scene(menuRoot);
                    currentStage.setTitle("Game Mode Menu");
                    currentStage.setScene(menuScene);

                    // Create and configure a fade-in transition
                    AnchorPane menuPane = (AnchorPane) menuScene.getRoot();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(0));
                    fadeIn.setNode(menuPane);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    // Start the fade-in transition
                    fadeIn.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            // Start the fade-out transition
            fadeOut.play();
        }
    }

    // Handles the exit button action event
    @FXML
    private void HFunc_Exit(ActionEvent event) {
        killThread();
        // Get the current stage from the event source (the button)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the stage, which will close the application
        currentStage.close();
    }

}
