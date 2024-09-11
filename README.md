
# Tic-Tac-Toe Game with Hardware Integration and Custom Linux Image

## Overview
This project is divided into three main parts:
1. **Joystick Integration (Hardware)**  
   We capture joystick input via the `/dev/input` file in C and send the data to a JavaFX-based Tic-Tac-Toe game using JNI.
   
2. **Tic-Tac-Toe Game (Software)**  
   A two-mode Tic-Tac-Toe game with a graphical interface built using JavaFX. The game supports two players or a single player versus the computer, with difficulty levels (Easy, Medium, Hard) based on the Minimax algorithm. The game includes different scenes for each phase:
   - Home screen with a Start button.
   - Menu screen to select between the two modes (Single Player or Multiplayer).
   - For Single Player mode: A scene to input the player’s name.
   - For Multiplayer: A scene to input both players' names and select the difficulty level.
   - A game board scene where gameplay happens.
   - Victory screens for X, O, and ties.
   - Warning screens if the player does not input their name, X/O selection, or both.

3. **Custom Linux Image (Yocto)**  
   We created a custom Linux image using Yocto, which includes JDK for handling JavaFX, and a driver for the joystick. The game automatically pauses if the joystick is unplugged and resumes once reconnected.

## Features
- **Joystick Control**: Reads input from the joystick in real-time, sending it from C to Java through JNI. We handle edge cases, such as pausing the game when the joystick is unplugged.
- **Tic-Tac-Toe Gameplay**:  
  - Two modes: Single Player (against AI with Minimax algorithm) and Multiplayer.
  - Three AI difficulty levels: Easy, Medium, Hard.
  - Full JavaFX-based GUI for seamless interaction.
  - Handles corner cases such as:
    - Ensuring correct inputs (player names, X/O choice).
    - Proper synchronization when reading and writing to files.

## Corner Cases and Error Handling
- **Unplugging Joystick**: The game automatically pauses and waits for the joystick to be reconnected.
- **File Access**: Opened and read files are handled in a synchronized manner to avoid race conditions.
- **Input Validation**: Prompts the user to enter correct inputs, such as player names and X/O selection, with clear warnings.

## Setup and Installation
### Hardware Setup
- Ensure the joystick is connected to the `/dev/input` device.

### Software Requirements
- JavaFX for GUI.
- JNI for communication between C and Java.
- Yocto for building a custom Linux image with JDK and joystick drivers.

### Build Instructions
1. **Joystick Integration (C to Java)**:
   - Use JNI to send data from C (reading from `/dev/input`) to the Java application.
   
2. **Tic-Tac-Toe Game**:
   - Build the JavaFX application.
   - Use the Minimax algorithm for the AI in single-player mode.
   
3. **Custom Linux Image**:
   - Use Yocto to include the required packages (JDK, WIFI , joystick driver).

## How to Play
1. Start the game from the home screen.
2. Choose between Single Player or Multiplayer.
3. Input player names and, if applicable, choose a difficulty level.
4. Play the Tic-Tac-Toe game with joystick controls.
5. Win by aligning Xs or Os, or end in a tie.

