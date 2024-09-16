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
 * @author Lenovo
 */
public class MenuController implements Initializable {

    // Thread for joystick input handling
    static Thread joystickThread;
    @FXML
    private Button Vbtn_Multi;
    @FXML
    private Button Vbtn_Exit;
    @FXML
    private Button Vbtn_Single;

    // Keeps track of the currently selected button
    private int currentSelection = 0;

    // Flag to control the joystick thread
    private boolean running = true;
    JoyStickThread jsThread;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set initial focus to the single-player button
        Vbtn_Single.requestFocus();
        jsThread = new JoyStickThread();

        // Start joystick input handling
        jsThread.startJoystick();

        // Start the thread that handles joystick actions
        startJoystickThread();

    }

    // Method that listens to joystick actions and changes button focus accordingly
    private void startJoystickThread() {

        joystickThread = new Thread(() -> {
            while (running) {
                // Read the joystick action
                char action = JoystickReader.getAction();

                // Reset the joystick action
                JoystickReader.setAction(' ');
                if (action != ' ') {
                    if (action == 'd') {
                        // Move selection down if 'd' is pressed
                        Platform.runLater(this::moveSelectionDown);
                    } else if (action == 'u') {
                        // Move selection up if 'u' is pressed
                        Platform.runLater(this::moveSelectionUp);
                    } else if (action == 'x') {
                        // Fire the currently selected button if 'x' is pressed
                        Platform.runLater(this::fireCurrentButton);

                    }
                }

            }
        });

        // Start the joystick thread
        joystickThread.start();
    }

    // Move focus to the button below the currently selected one
    private void moveSelectionDown() {
        switch (currentSelection) {
            case 0:
                Vbtn_Exit.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #f9f7f6;");
                Vbtn_Single.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #e00051;");

                // Move focus to the multi-player button
                Vbtn_Multi.requestFocus();

                // Update the current selection index
                currentSelection = 1;
                break;
            case 1:
                Vbtn_Single.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #f9f7f6;");
                Vbtn_Multi.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #e00051;");

                // Move focus to the exit button
                Vbtn_Exit.requestFocus();

                // Update the current selection index
                currentSelection = 2;
                break;
            case 2:
                Vbtn_Multi.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #f9f7f6;");
                Vbtn_Exit.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #e00051;");

                // Wrap around to the single-player button
                Vbtn_Single.requestFocus();

                // Update the current selection index
                currentSelection = 0;
                break;
            default:
                break;
        }
    }

    // Moves focus to the button above the currently selected one
    private void moveSelectionUp() {
        switch (currentSelection) {
            case 0:
                Vbtn_Exit.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #f9f7f6;");
                Vbtn_Single.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #e00051;");

                // Move focus to the exit button
                Vbtn_Exit.requestFocus();

                // Update the current selection index
                currentSelection = 2;
                break;
            case 1:
                Vbtn_Single.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #f9f7f6;");
                Vbtn_Multi.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #e00051;");

                // Move focus to the single-player button
                Vbtn_Single.requestFocus();

                // Update the current selection index
                currentSelection = 0;
                break;
            case 2:
                Vbtn_Multi.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #f9f7f6;");
                Vbtn_Exit.setStyle(Vbtn_Single.getStyle() + "-fx-border-color:  #e00051;");

                // Move focus to the multi-player button
                Vbtn_Multi.requestFocus();

                // Update the current selection index
                currentSelection = 1;
                break;
            default:
                break;
        }
    }

    // Fire the action of the currently selected button
    private void fireCurrentButton() {
        switch (currentSelection) {
            case 0:
                // Trigger single-player button action
                Vbtn_Single.fire();
                break;
            case 1:
                // Trigger multi-player button action
                Vbtn_Multi.fire();
                break;
            case 2:
                // Trigger exit button action
                Vbtn_Exit.fire();
                break;
            default:
                break;
        }
    }

    // Handle the single-player button action
    @FXML
    private void Hfunc_Single(ActionEvent event) {
        // Stop joystick thread
        killThread();
        // Get the current stage from the button
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (currentStage != null) {
            JoyStickThread.gameMode = 0;
            performFadeTransition(currentStage, "Name_Mode.fxml", "Game Complexity");

        }
    }

    // Handle the multi-player button action
    @FXML
    private void Hfunc_Multi(ActionEvent event) {
        // Stop joystick thread
        killThread();
        // Get the current stage from the button
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (currentStage != null) {
            JoyStickThread.gameMode = 1;
            performFadeTransition(currentStage, "Naming.fxml", "Players Naming Stage");
        }
    }

    @FXML
    private void Hfunc_Exit(ActionEvent event) {
        killThread();
        // Get the current stage from the event source (the button)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the stage, which will close the application
        currentStage.close();
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

    // Method to stop the joystick thread
    public void killThread() {

        // Stop the loop in the joystick thread
        running = false;

        // Stop joystick input handling
        jsThread.stopJoystick();
    }

}
