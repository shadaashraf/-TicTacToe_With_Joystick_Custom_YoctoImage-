// Native methods implementation of
// /home/doaa/NetBeansProjects/TicTacToe/src/tictactoe/JoystickReader.java

#include "JoystickReader.h"
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <linux/joystick.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>

typedef struct {
    int js_fd;
    int path; // Store the joystick path (0 for js0, 1 for js1, etc.)
    struct js_event js;
    char data_buffer[256];
    pthread_mutex_t mutex;
    int running;
    pthread_t thread;
} Joystick;
int status;
int last_t = 0;

void *read_joystick_thread(void *arg) {
    Joystick *joystick = (Joystick *)arg;

    while (joystick->running) {
        // Attempt to open the joystick device if not already open
        if (joystick->js_fd < 0) {
            if (joystick->path == 1) {
                joystick->js_fd = open("/dev/input/js0", O_RDONLY | O_NONBLOCK);
            } else if (joystick->path == 2) {
                joystick->js_fd = open("/dev/input/js1", O_RDONLY | O_NONBLOCK);
            }

            if (joystick->js_fd >= 0) {
                status=1;
            } else {
                sleep(1); // Wait before trying again
                continue; // Skip to the next iteration if not connected
            }
        }

        // Read joystick events
        ssize_t bytes = read(joystick->js_fd, &joystick->js, sizeof(struct js_event));
        if (bytes < 0) {
            if (errno == EAGAIN) {
                continue; // No data available, keep looping
            } else if (errno == ENODEV) {
                status=0;
                close(joystick->js_fd);
                joystick->js_fd = -1; // Mark joystick as disconnected
                continue;
            } else {
                perror("Joystick read error");
                break; // Exit the loop on other errors
            }
        }

        // Store the data in the buffer
        pthread_mutex_lock(&joystick->mutex);
        snprintf(joystick->data_buffer, sizeof(joystick->data_buffer),
                 "%u.%d.%d.%d",
                 joystick->js.type, joystick->js.number, joystick->js.value,status);
        pthread_mutex_unlock(&joystick->mutex);
    }

    return NULL;
}

JNIEXPORT jlong JNICALL Java_GameLogic_JoystickReader_createJoystick
(JNIEnv *env, jobject obj, jint path) {
    Joystick *joystick = (Joystick *)malloc(sizeof(Joystick));
    if (!joystick) {
        perror("Malloc failed");
        return 0;
    }

    joystick->js_fd = -1; // Initially set the file descriptor to invalid
    joystick->path = path; // Store the joystick path (0 for js0, 1 for js1)

    if (pthread_mutex_init(&joystick->mutex, NULL) != 0) {
        perror("Mutex init failed");
        free(joystick);
        return 0;
    }

    joystick->running = 1;
    if (pthread_create(&joystick->thread, NULL, read_joystick_thread, joystick) != 0) {
        perror("Failed to create thread");
        pthread_mutex_destroy(&joystick->mutex);
        free(joystick);
        return 0;
    }

    return (jlong)joystick;
}

JNIEXPORT void JNICALL Java_GameLogic_JoystickReader_destroyJoystick
(JNIEnv *env, jobject obj, jlong joystickPtr) {
    Joystick *joystick = (Joystick *)joystickPtr;
    if (joystick) {
        joystick->running = 0;
        pthread_join(joystick->thread, NULL);
        close(joystick->js_fd);
        pthread_mutex_destroy(&joystick->mutex);
        free(joystick);
    }
}

JNIEXPORT jstring JNICALL Java_GameLogic_JoystickReader_readJoystick
(JNIEnv *env, jobject obj, jlong joystickPtr) {
    int curr_t;
    Joystick *joystick = (Joystick *)joystickPtr;
    if (!joystick) {
        return (*env)->NewStringUTF(env, "Joystick not initialized");
    }

    pthread_mutex_lock(&joystick->mutex);
    curr_t = joystick->js.time;
    jstring result = NULL;

    if (last_t < curr_t) {
        result = (*env)->NewStringUTF(env, joystick->data_buffer);
        last_t = curr_t;
    }

    pthread_mutex_unlock(&joystick->mutex);
    return result;
}
