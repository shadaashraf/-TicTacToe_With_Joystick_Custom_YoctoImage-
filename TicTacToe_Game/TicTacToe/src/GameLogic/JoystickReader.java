/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author doaa
 */
public class JoystickReader implements Runnable {

    // Volatile variable to store the current joystick action
    static volatile char action;

    // Player type (default to 1)
    private static int pType = 1;

    // X coordinate
    private int x;

    // y coordinate
    private int y;

    // State of the fire button
    private boolean fireButtonPressed;

    private long joystickPtr;
    private long joystickPtr2;

    public JoystickReader() {
        this.x = 0;
        this.y = 0;
        this.fireButtonPressed = false;
        action = ' ';
    }

    public JoystickReader(int number) {
        this();
        joystickPtr = createJoystick(number);
        if (joystickPtr == 0) {
            System.out.println("Failed to initialize joystick" + number);
        }
    }

    public static void setPType(int type) {
        pType = type;
    }

    // Load the native library for joystick handling
    static {
        System.loadLibrary("CppJNILibrary_4"); // Make sure this matches the shared library name
    }

    // Native methods for joystick operations
    private native long createJoystick(int path);

    private native void destroyJoystick(long joystickPtr);

    private native String readJoystick(long joystickPtr);

    //Continuously reads data from the joystick and processes it.
    public void getData() {
        JoystickReader reader = new JoystickReader();

        // Initialize joystick 1
        joystickPtr = reader.createJoystick(1);

        if (joystickPtr == 0) {
            System.out.println("Failed to initialize joystick1");
            return;
        }

        // Initialize second joystick if playing PLAYER_VS_PLAYER mode
        if (TicTacToeGame.getGameMode() == GameMode.PLAYER_VS_PLAYER) {
            joystickPtr2 = reader.createJoystick(2);
            if (joystickPtr2 == 0) {
                System.out.println("Failed to initialize joystick2");
                return;
            }

        }

        while (true) {
            try {
                sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(JoystickReader.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Read data based on current player
            if (TicTacToeGame.getCurrentPlayer() == TicTacToeGame.getPlayer1()) {
                String data = reader.readJoystick(joystickPtr);
                if (data != null && !data.isEmpty()) {
                    String[] dataParts = data.split("\\.");
                    if (dataParts.length >= 4) {
                        String xAxis = dataParts[1];
                        String yAxis = dataParts[2];
                        String buttonState = dataParts[0];
                        String status = dataParts[3] + 1;

                        // Process joystick input and set action
                        switch (buttonState) {
                            case "2":
                                if ("0".equals(xAxis) | "6".equals(xAxis)) {
                                    if ("-32767".equals(yAxis.trim())) {
                                        // Move left
                                        action = 'l';
                                    } else if ("32767".equals(yAxis.trim())) {
                                        // Move right
                                        action = 'r';
                                    }
                                } else if ("1".equals(xAxis) || "7".equals(xAxis)) {
                                    if ("-32767".equals(yAxis.trim())) {
                                        // Move up
                                        action = 'u';
                                    } else if ("32767".equals(yAxis.trim())) {
                                        // Move down
                                        action = 'd';
                                    }
                                }
                                break;
                            case "1":
                                if ("2".equals(xAxis) || "4".equals(xAxis)) {
                                    // Fire action
                                    action = 'x';
                                } else if ("0".equals(xAxis)) {
                                    action = 'f';
                                } else if ("3".equals(xAxis)) {
                                    action = 'z';
                                }

                                break;
                            default:
                                break;
                        }

                    }

                }
            } else if (TicTacToeGame.getGameMode() == GameMode.PLAYER_VS_PLAYER) {
                String data2 = reader.readJoystick(joystickPtr2);
                if (data2 != null && !data2.isEmpty()) {
                    String[] dataParts = data2.split("\\.");
                    if (dataParts.length >= 4) {
                        String xAxis = dataParts[1];
                        String yAxis = dataParts[2];
                        String buttonState = dataParts[0];
                        String status = dataParts[3] + 2;

                        // Process second joystick input and set action
                        switch (buttonState) {
                            case "2":
                                if ("0".equals(xAxis) | "6".equals(xAxis)) {
                                    if ("-32767".equals(yAxis.trim())) {
                                        // Move left
                                        action = 'l';
                                    } else if ("32767".equals(yAxis.trim())) {
                                        // Move right
                                        action = 'r';
                                    }
                                } else if ("1".equals(xAxis) || "7".equals(xAxis)) {
                                    if ("-32767".equals(yAxis.trim())) {
                                        // Move up
                                        action = 'u';
                                    } else if ("32767".equals(yAxis.trim())) {
                                        // Move down
                                        action = 'd';
                                    }
                                }
                                break;
                            case "1":
                                if ("2".equals(xAxis) || "4".equals(xAxis)) {
                                    // Fire action
                                    action = 'x';
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

        }

    }

    // Clean up joystick resources.
    public void distroy() {

        // Destroy first joystick
        if (joystickPtr != 0) {
            destroyJoystick(joystickPtr);
        }

        // Destroy second joystick (if applicable)
        if (joystickPtr2 != 0) {
            destroyJoystick(joystickPtr2);
        }

        System.out.println("Joystick cleanup completed.");
    }

    // Execute the joystick data reading in a separate thread.
    @Override
    public void run() {
        getData();

    }

    // Process joystick actions and updates coordinates.
    public int[] getInput() {

        switch (action) {

            case 'u':
                // Move up
                y = (y - 1 + 3) % 3;
                action = ' ';

                break;
            case 'd':
                // Move down
                y = (y + 1) % 3;
                action = ' ';
                break;
            case 'l':
                // Move left
                x = (x - 1 + 3) % 3;
                action = ' ';
                break;
            case 'r':
                // Move right
                x = (x + 1) % 3;
                action = ' ';
                break;
            case 'x':
                // Fire button pressed
                fireButtonPressed = true;
                action = ' ';
                break;
            default:

        }

        // Return current coordinates
        return new int[]{x, y};
    }

    // Check if the fire button is currently pressed.
    public boolean isActive() {
        return fireButtonPressed;
    }

    // Toggle the state of the fire button
    public void toggleFireButton() {
        fireButtonPressed = !fireButtonPressed;
    }

    // Get the current action.
    public static char getAction() {
        return action;
    }

    // Set the current action.
    public static void setAction(char ac) {
        action = ac;
    }

}
