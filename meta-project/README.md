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

```bash
include meta-poky/conf/distro/poky.conf

DISTRO = "Tic-Tac-Toe"
DISTRO_NAME = "Tic-Tac-Toe"
#DISTRO_VERSION = "3.4+snapshot-${METADATA_REVISION}"
DISTRO_VERSION = "4.0.20"
DISTRO_CODENAME = "kirkstone"

IMAGE_INSTALL:append = " dropbear systemd systemd-serialgetty "

MAINTAINER = "abdoabo3lm@gmail.com"

Aboalam_DEFAULT_DISTRO_FEATURES = "largefile opengl ptest multiarch wayland vulkan ssh wifi x11"

DISTRO_FEATURES ?= "${DISTRO_FEATURES_DEFAULT} ${Aboalam_DEFAULT_DISTRO_FEATURES}"
```

The custom distribution is defined in conf/distro/Tic-Tac-Toe.conf. Key configurations include:
  **- DISTRO:** Tic-Tac-Toe
  
  **- DISTRO_VERSION:** 4.0.20 (kirkstone)
  
  **- DISTRO_FEATURES:** opengl, wayland, vulkan, ssh, wifi, x11
  
  **- IMAGE_INSTALL:** dropbear, systemd, systemd-serialgetty
  
  **- MAINTAINER:** abdoabo3lm@gmail.com


**- bsp.bb**


```bash
# Basic Utilities and Development Tools
RDEPENDS:${PN} += "\
    openssh-sftp-server \
    rsync \
    python3-modules \
    python3-misc \
    util-linux \
    coreutils \
"

# Python Support
RDEPENDS:${PN} += "python3"

# Networking and Connectivity
RDEPENDS:${PN} += "\
    connman \
    connman-client \
    dhcpcd \
    iptables \
    bridge-utils \
    hostapd \
    wpa-supplicant \
"

# Bluetooth Support
RDEPENDS:${PN} += "\
    bluez5 \
    pi-bluetooth \
    bluez5-testtools \
"

# I2C Support
RDEPENDS:${PN} += "i2c-tools"

# Firmware and Kernel Modules
RDEPENDS:${PN} += "\
    linux-firmware \
    kernel-modules \
    linux-firmware-ralink \
    linux-firmware-rtl8192ce \
    linux-firmware-rtl8192cu \
    linux-firmware-rtl8192su \
    linux-firmware-rpidistro-bcm43430 \
    linux-firmware-bcm43430 \
"

# Joystick Support
RDEPENDS:${PN} += "joystick"

# Device Management
RDEPENDS:${PN} += "udev-rules-rpi"

# Wireless Tools
RDEPENDS:${PN} += "iw"

# Splash Screen and Visual Feedback
RDEPENDS:${PN} += "\
    psplash \
    psplash-raspberrypi \
"
RDEPENDS:${PN} += "\
    apt \
    bash \
    git \
"

# X11 and GUI Support
RDEPENDS:${PN} += "\
    xserver-xorg \
    xf86-video-fbdev \
    xf86-input-evdev \
    xterm \
    matchbox-wm \
"

# Additional DISTRO_FEATURES
DISTRO_FEATURES:append = "\
    bluez5 \
    bluetooth \
    wifi \
    pi-bluetooth \
    linux-firmware-bcm43430 \
    usrmerge \
    ipv4 \
"

# Additional IMAGE_FEATURES
IMAGE_FEATURES += "splash package-management"

# Additional MACHINE_FEATURES
MACHINE_FEATURES:append = "\
    bluetooth \
    wifi \
    vc4graphics \
"

# X Server Configuration
XSERVER ?= "xserver-xorg \
            xf86-video-fbdev \
            xf86-video-modesetting \
"
XSERVERCODECS ?= ""

# Ensure X server and codecs are installed
RDEPENDS:${PN} += "\
    ${XSERVER} \
    ${XSERVERCODECS} \
"
```
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

```bash 
DESCRIPTION = "Image with Sato, a mobile environment and visual style for \
mobile devices. The image supports X11 with a Sato theme, Pimlico \
applications, and contains terminal, editor, and file manager."
HOMEPAGE = "https://www.yoctoproject.org/"

IMAGE_FEATURES += "splash package-management x11-base x11-sato ssh-server-dropbear hwcodecs"

LICENSE = "MIT"

inherit core-image

ImMAGE_INSTALL:append= " bsp"

 DISTRO_FEATURES:append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = "systemd-compat-units"
IMAGE_ROOTFS_EXTRA_SPACE = "5242880"

TOOLCHAIN_HOST_TASK:append = " nativesdk-intltool nativesdk-glib-2.0"
TOOLCHAIN_HOST_TASK:remove:task-populate-sdk-ext = " nativesdk-intltool nativesdk-glib-2.0"

QB_MEM = '${@bb.utils.contains("DISTRO_FEATURES", "opengl", "-m 512", "-m 256", d)}'
QB_MEM:qemuarmv5 = "-m 256"
QB_MEM:qemumips = "-m 256"


IMAGE_FSTYPES = "tar.bz2 ext4 rpi-sdimg"

IMAGE_INSTALL:append = "  "
```

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

# Add JAVA and JAVAFX to IMAGE

- After open Image and connect to WIFI we can send Files and Packges by SCB

## For Java 

- Download Java SE Development Kit [Linux Arm 64 RPM Package](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

- In Image
```bash
sudo rpm -i jdk-17.0.2_linux-aarch64_bin.rpm
```
## For JavaFX

- Download the JavaFX SDK from [OpenJFX azul](https://www.azul.com/downloads/?version=java-22&os=linux&architecture=arm-64-bit&package=jdk-fx#zulu)

- Then we want to move libraries to /lib

```bash
sudo tar -xvf openjfx-17.0.2_linux-aarch64_bin-sdk.zip -C /usr/lib/jvm/
```
- To run app

```bash
java --module-path "$JAVAFX_HOME/lib" --add-modules javafx.base,javafx.controls,javafx.graphics,javafx.fxml,javafx.media,javafx.web -jar your-app.jar
```
