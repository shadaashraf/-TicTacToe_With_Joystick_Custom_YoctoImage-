/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

/**
 *
 * @author doaa
 */
public class HumanPlayer extends Player {

    private JoystickReader joystick;

    public HumanPlayer(String symbol, JoystickReader joystick) {
        super(symbol);
        this.joystick = joystick;
    }

    // Get the joystick input as an array of coordinates
    public int[] getJoystickInput() {

        return joystick.getInput();
    }

    // Check if the joystick's fire button is currently active
    public boolean isFired() {
        return joystick.isActive();
    }

    // Release the joystick's fire button.
    public void releaseFired() {
        joystick.toggleFireButton();
    }
}
