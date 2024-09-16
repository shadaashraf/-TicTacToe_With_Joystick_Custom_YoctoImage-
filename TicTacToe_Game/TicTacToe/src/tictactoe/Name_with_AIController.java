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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class Name_with_AIController implements Initializable {

    @FXML
    private TextField VXPlayerName;
    @FXML
    private Button Vbtn_Play;

    // Holds Player X's name
    private String xPlayerName;

    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to control joystick thread activity
    private boolean running = true;
    @FXML
    private Button Vbtn_Back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create and start the joystick thread
        jsThread = new JoyStickThread();
        jsThread.startJoystick();

        // Start a separate thread to handle joystick actions
        startJoystickThread();

        // Automatically focus the Play button
        Vbtn_Play.requestFocus();

    }

    private synchronized void startJoystickThread() {

        joystickThread = new Thread(() -> {
            while (running) {
                // Get the joystick action
                char action = JoystickReader.getAction();
                JoystickReader.setAction(' ');
                if (action != ' ') {
                    if (action == 'x') {
                        // Trigger Play button when 'x' is pressed
                        Platform.runLater(this::fireCurrentButton);
                    } else if (action == 'f') {
                        // Trigger Back button when 'f' is pressed
                        Platform.runLater(this::fireBackButton);
                    }

                }

            }
        });
        // Start the joystick thread
        joystickThread.start();
    }

    private void fireCurrentButton() {
        // Fire the Play button event
        Vbtn_Play.fire();
    }

    private void fireBackButton() {
        // Focus on the Back button
        Vbtn_Back.requestFocus();

        // Fire the Back button event
        Vbtn_Back.fire();
    }

    @FXML
    private void HFunc_XName(ActionEvent event) {
        // Get the name entered by the user for Player X
        xPlayerName = VXPlayerName.getText();
    }

    // Handle the Play button click event to start the game
    @FXML
    private void HFun_PlayBtn(ActionEvent event) {
        // Get the Player X's name
        xPlayerName = VXPlayerName.getText();

        // If Player X name is empty, show an error message
        if (xPlayerName.isEmpty()) {
            // Stop joystick handling
            killThread();
            Stage currentStage = (Stage) Vbtn_Play.getScene().getWindow();
            if (currentStage != null) {
                // Transition to an error message screen
                performFadeTransition(currentStage, "XErrorMessage.fxml", "X-PLayer's Naming Warning");
            }
        } else {
            // Stop joystick handling
            killThread();
            Stage currentStage = (Stage) Vbtn_Play.getScene().getWindow();
            if (currentStage != null) {
                // Set player names for joystick thread
                JoyStickThread.xPlayer = xPlayerName;
                JoyStickThread.oPlayer = "AI Player";
                // Transition to the game scene
                performFadeTransition(currentStage, "GameLogic.fxml", "X-O Game");

            }
        }

    }

    // Handle the Back button click event to return to the previous screen
    @FXML
    private void HFunc_Back(ActionEvent event) {
        // Stop joystick handling
        killThread();
        Stage currentStage = (Stage) Vbtn_Back.getScene().getWindow();
        if (currentStage != null) {
            // Transition back to the naming mode scene
            performFadeTransition(currentStage, "Name_Mode.fxml", "Game Complexity");

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

    public void killThread() {
        // Stop the loop
        running = false;

        // Stop the joystick thread
        jsThread.stopJoystick();
    }

}
