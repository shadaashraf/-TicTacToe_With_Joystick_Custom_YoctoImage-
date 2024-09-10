SUMMARY = "Custom Package Group for Sato on RPI 4B"
DESCRIPTION = "This package group installs custom packages for Tic Tac Toe"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

# Runtime Dependencies
# Include all the packages necessary for your application


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
