/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import GameLogic.JoystickReader;

public class JoyStickThread {

    public static String xPlayer;
    public static String oPlayer;
    public static int gameMode, level;
    JoystickReader jsReader = new JoystickReader();
    Thread jsThread;

    public JoyStickThread() {

    }

    public synchronized void startJoystick() {
        jsThread = new Thread(jsReader);
        jsThread.start();

    }

    // Method to stop the thread
    public void stopJoystick() {
        jsThread.stop();
    }
}
