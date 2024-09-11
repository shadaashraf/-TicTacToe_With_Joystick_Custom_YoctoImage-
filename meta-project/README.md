# Meta-Project: Tic-Tac-Toe Yocto Layer

## Overview
This meta layer, `meta-project`, is created for building a Yocto image for Raspberry Pi 4B with custom packages for a Tic-Tac-Toe game. It integrates various features such as networking, Bluetooth, joystick support, and graphical capabilities. The custom Yocto image is based on the `Tic-Tac-Toe` distribution with `systemd` as the init manager.

## Directory Structure
```bash
meta-project/
├── conf
│   ├── distro
│   │   └── Tic-Tac-Toe.conf  # Custom distro configuration
│   └── layer.conf            # Layer configuration
├── COPYING.MIT                # License file
├── README                     # Readme file
├── recipes-bsp                # BSP recipe
│   └── bsp
│       └── bsp.bb             # Custom package group for Tic-Tac-Toe
├── recipes-tic-tac-image      # Custom image recipe
│   └── tic-tac-image
│       └── tic-tac-image.bb   # Custom image definition
└── touch
```
## Configuration Files
**- Tic-Tac-Toe.conf**

The custom distribution is defined in conf/distro/Tic-Tac-Toe.conf. Key configurations include:
  **- DISTRO:** Tic-Tac-Toe
  
  **- DISTRO_VERSION:** 4.0.20 (kirkstone)
  
  **- DISTRO_FEATURES:** opengl, wayland, vulkan, ssh, wifi, x11
  
  **- IMAGE_INSTALL:** dropbear, systemd, systemd-serialgetty
  
  **- MAINTAINER:** abdoabo3lm@gmail.com


**- bsp.bb**

This recipe defines the custom package group for the Tic-Tac-Toe image, adding support for:

- **Basic utilities and development tools:** openssh-sftp-server, rsync, python3-modules, util-linux, etc.
- **Python support:** python3
- **Networking and connectivity:** connman, iptables, hostapd, etc.
- **Bluetooth support:** bluez5, pi-bluetooth
- **Joystick support**
- **I2C support**
- **Firmware and kernel modules**
- **X11 and GUI support:** xserver-xorg, xf86-video-fbdev, xterm, etc.
- **Splash screen support**


**- tic-tac-image.bb**


This recipe defines the image for the Tic-Tac-Toe project, including:

- **IMAGE_FEATURES:** splash, package-management, x11-sato, ssh-server-dropbear, hwcodecs
- **DISTRO_FEATURES:** systemd, with sysvinit excluded
- **X11 and GUI support**

## Layers

The project uses the following layers:

- **Poky layers:** meta, meta-poky, meta-yocto-bsp
- **Raspberry Pi support:** meta-raspberrypi
- **Additional layers:** meta-openembedded, meta-python, meta-networking, meta-java, meta-gnome, meta-xfce, meta-multimedia

## How to Build

- Clone the Yocto Project and the necessary layers.
- Add the meta-project layer to your bblayers.conf.
- Build the image using BitBake:

```bash
bitbake tic-tac-image
```
