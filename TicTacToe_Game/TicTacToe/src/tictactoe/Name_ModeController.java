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
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Name_ModeController implements Initializable {

    static Thread joystickThread;
    @FXML
    private RadioButton Vbtn_Easy;
    @FXML
    private RadioButton Vbtn_Meduim;
    @FXML
    private RadioButton Vbtn_Hard;

    // 0 for Easy, 1 for Medium, 2 for Hard
    private int currentSelection = 0;

    // Thread control flag
    private boolean running = true;
    JoyStickThread jsThread;
    @FXML
    private Button Vbtn_Back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initially hover Easy button
        Vbtn_Easy.requestFocus();
        jsThread = new JoyStickThread();

        // Start joystick input
        jsThread.startJoystick();

        // Begin joystick thread for handling input
        startJoystickThread();

    }

    // Method that listens to joystick actions and changes button focus accordingly
    private synchronized void startJoystickThread() {

        joystickThread = new Thread(() -> {

            while (running) {
                // Get joystick action
                char action = JoystickReader.getAction();

                // Clear joystick action
                JoystickReader.setAction(' ');
                if (action != ' ') {
                    if (action == 'd') {
                        // Move selection down
                        Platform.runLater(this::moveSelectionDown);
                    } else if (action == 'u') {
                        // Move selection up
                        Platform.runLater(this::moveSelectionUp);
                    } else if (action == 'x') {
                        // Simulate button press
                        Platform.runLater(this::fireCurrentButton);

                    } else if (action == 'f') {
                        // Simulate Back button press
                        Platform.runLater(this::firebackButton);
                    }
                }

            }
        });

        // Start the joystick handling thread
        joystickThread.start();
    }

    // Moves focus to the next button in the selection
    private void moveSelectionDown() {
        switch (currentSelection) {
            case 0:
                Vbtn_Easy.setSelected(false);
                Vbtn_Meduim.requestFocus();
                currentSelection = 1;
                break;
            case 1:
                Vbtn_Meduim.setSelected(false);
                Vbtn_Hard.requestFocus();
                currentSelection = 2;
                break;
            case 2:
                Vbtn_Hard.setSelected(false);
                Vbtn_Easy.requestFocus(); // Wrap around to Easy
                currentSelection = 0;
                break;
            default:
                break;
        }
    }

    // Moves focus to the previous button in the selection
    private void moveSelectionUp() {
        switch (currentSelection) {
            case 0:
                Vbtn_Easy.setSelected(false);
                Vbtn_Hard.requestFocus(); // Wrap around to Hard
                currentSelection = 2;
                break;
            case 1:
                Vbtn_Meduim.setSelected(false);
                Vbtn_Easy.requestFocus();
                currentSelection = 0;
                break;
            case 2:
                Vbtn_Hard.setSelected(false);
                Vbtn_Meduim.requestFocus();
                currentSelection = 1;
                break;
            default:
                break;
        }
    }

    private void fireCurrentButton() {
        switch (currentSelection) {
            case 0:
                Vbtn_Easy.fire();  // Simulate pressing the Easy button
                break;
            case 1:
                Vbtn_Meduim.fire();  // Simulate pressing the Medium button
                break;
            case 2:
                Vbtn_Hard.fire();  // Simulate pressing the Hard button
                break;
            default:
                break;
        }
    }

    private void firebackButton() {
        Vbtn_Back.fire();
    }

    // Handles the Easy button click
    @FXML
    private void Hfunc_Easy(ActionEvent event) {
        killThread();
        Stage currentStage = (Stage) Vbtn_Easy.getScene().getWindow();
        if (currentStage != null) {
            JoyStickThread.level = 0;
            performFadeTransition(currentStage, "Name_with_AI.fxml", "Players Naming Stage");
        }

    }

    // Handles the Medium button click
    @FXML
    private void Hfunc_Meduim(ActionEvent event) {
        killThread();
        Stage currentStage = (Stage) Vbtn_Meduim.getScene().getWindow();
        if (currentStage != null) {
            JoyStickThread.level = 1;
            performFadeTransition(currentStage, "Name_with_AI.fxml", "Players Naming Stage");
        }

    }

    // Handles the Hard button click
    @FXML
    private void Hfunc_Hard(ActionEvent event) {
        killThread();
        Stage currentStage = (Stage) Vbtn_Hard.getScene().getWindow();
        if (currentStage != null) {
            JoyStickThread.level = 2;
            performFadeTransition(currentStage, "Name_with_AI.fxml", "Players Naming Stage");
        }
    }

    // Handle the Back button click
    @FXML
    private void HFunc_Back(ActionEvent event) {
        Stage currentStage = (Stage) Vbtn_Back.getScene().getWindow();
        killThread();
        if (currentStage != null) {
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

    // Stops the joystick thread and cleans up
    public void killThread() {
        // Stop the thread loop
        running = false;

        // Stop joystick input
        jsThread.stopJoystick();
    }
}
