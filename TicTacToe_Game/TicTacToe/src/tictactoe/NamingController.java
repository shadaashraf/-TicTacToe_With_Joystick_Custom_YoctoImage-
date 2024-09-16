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
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class NamingController implements Initializable {

    static Thread joy;

    @FXML
    private TextField VMXP_Name;
    @FXML
    private TextField VMOP_Name;
    @FXML
    private Button Vbtn_Play;

    // String to hold Player X's name
    private String xPlayerName;

    // String to hold Player O's name
    private String oPlayerName;
    private Thread joystickThread;
    private JoyStickThread jsThread;

    // Flag to control the joystick thread
    private boolean running = true;
    @FXML
    private Button Vbtn_Back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize joystick handling thread
        jsThread = new JoyStickThread();
        jsThread.startJoystick();
        startJoystickThread();

        // Request focus for the Play button
        Vbtn_Play.requestFocus();
    }

    // Start a separate thread that listens to joystick input and fires the appropriate button actions based on joystick events
    private synchronized void startJoystickThread() {
        joystickThread = new Thread(() -> {
            while (running) {
                // Get action from the joystick
                int action = JoystickReader.getAction();
                // Reset joystick action
                JoystickReader.setAction(' ');

                // Check if any action is detected
                if (action != ' ') {
                    if (action == 'x') {
                        // Press Play button on 'x'
                        Platform.runLater(this::fireCurrentButton);
                    } else if (action == 'f') {
                        // Press Back button on 'f'
                        Platform.runLater(this::fireBackButton);
                    }
                }
            }
        });
        // Start the joystick listener thread
        joystickThread.start();
    }

    // Simulate pressing the Play button when 'x' is detected from the joystick.
    private void fireCurrentButton() {
        Vbtn_Play.fire();
    }

    // Simulate pressing the Back button when 'f' is detected from the joystick.
    private void fireBackButton() {
        Vbtn_Back.fire();
    }

    @FXML
    private void HFunc_XMPName(ActionEvent event) {
        // Get the name entered by the user for Player X
        xPlayerName = VMXP_Name.getText();
    }

    @FXML
    private void HFunc_OMPName(ActionEvent event) {
        // Get the name entered by the user for Player O
        oPlayerName = VMOP_Name.getText();
    }

    @FXML
    private void HFun_PlayBtn(ActionEvent event) {

        // Get the entered names from both text fields
        xPlayerName = VMXP_Name.getText();
        oPlayerName = VMOP_Name.getText();

        // Check the conditions
        Stage currentStage = (Stage) Vbtn_Play.getScene().getWindow();
        // 1. If both text fields are empty
        if (xPlayerName.isEmpty() && oPlayerName.isEmpty()) {
            // Stop the joystick thread
            killThread();
            if (currentStage != null) {
                performFadeTransition(currentStage, "XOErrorMessage.fxml", "Naming Warning");
            }
        } else if (xPlayerName.isEmpty() && !oPlayerName.isEmpty()) {
            // Stop the joystick thread
            killThread();
            if (currentStage != null) {
                // Transition to the XNewNaming scene
                performFadeTransition(currentStage, "XNewNaming.fxml", "Naming Warning");
            }

        } else if (oPlayerName.isEmpty() && !xPlayerName.isEmpty()) {
            // Stop the joystick thread
            killThread();
            if (currentStage != null) {
                // Transition to the OErrorMessage scene
                performFadeTransition(currentStage, "OErrorMessage.fxml", "Naming Warning");
            }
        } else if (!oPlayerName.isEmpty() && !xPlayerName.isEmpty()) {
            // Stop the joystick thread
            killThread();
            if (currentStage != null) {
                // Set player names for joystick thread
                JoyStickThread.xPlayer = xPlayerName;
                JoyStickThread.oPlayer = oPlayerName;

                // Transition to the GameLogic scene
                performFadeTransition(currentStage, "GameLogic.fxml", "X-O Game");
            }
        }
    }

    @FXML
    private void HFunc_Back(ActionEvent event) {
        // Stop the joystick thread
        killThread();
        Stage currentStage = (Stage) Vbtn_Back.getScene().getWindow();
        if (currentStage != null) {
            // Transition to the Menu scene
            performFadeTransition(currentStage, "Menu.fxml", "Game Mode Menu");
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
