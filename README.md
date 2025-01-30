
# 2025 Megazord 7563 Operator Touch-screen Interface

Welcome to the base code repository for the **2025 Megazord Operator Touch-screen Interface (FRC Team 7563)**. This documentation provides an overview of how the interface works, how to install it, how to use and integrate it with the robot code, and some example codes.



https://github.com/user-attachments/assets/0196e820-e6ba-48a1-985c-26b94871a3bf



## Interface Overview

The **Interface** is designed to control auto-generated paths, optimizing routines to score corals in the Reef. While the driver only presses the score button in their joystick, the operator should pre-select the desired position to supply the score button, selecting the desired reef position (A-L) and level (L1-L4). 

![image](https://github.com/user-attachments/assets/f7de2333-f88d-40c6-ad17-63a09cdee125) 

### Interface Features

The **Interface** is split into two sections: the status section and the command section.

### 1. **Status Section**

This part of the interface provides the operator with the connection status with the NetworkTables (NT) and the robot, as well as the match and FMS info. Also, the **Interface Status** informs whether the interface is enabled to use or not, and the **Alliance Status** informs whether the team is a blue or red alliance and automatically changes the reef image to the correct alliance.

![Interface Status](https://github.com/user-attachments/assets/17f87dc1-76c1-4ed6-a6f7-b9c8ae770713)

### 2. **Command Section**

This part of the interface contains the virtual buttons to configure the desired path to score the coral in the Reef, including the desired position (A-L) and level (L1-L4).

![image](https://github.com/user-attachments/assets/dd7888c3-ff02-4e8b-b509-e23b2e734a95)


## Reference: pynetworktables2js library

The **Interface** uses the `pynetworktables2js` library to connect the HTML/JS interface with the NetworkTables and the FRC-roborio.

### pynetworktables2js

A cross-platform library that forwards NetworkTables key/values over a WebSocket, so that you can easily write a Driver Station Dashboard for your robot in HTML5 + JavaScript.

This library does not provide a full dashboard solution but is intended to provide the necessary plumbing for creating one with only knowledge of HTML/JavaScript. Because the communications layer uses NetworkTables, you can connect to all FRC languages (C++, Java, LabVIEW, Python).

Note

NetworkTables is a protocol used for robot communication in the FIRST Robotics Competition and can be used to talk to Shuffleboard/SmartDashboard. It does not have any security, and should never be used on untrusted networks.

### Documentation

Documentation can be found at http://pynetworktables2js.readthedocs.org/

### Installation

To install the `pynetworktables2js`, follow the instructions at https://github.com/robotpy/pynetworktables2js/tree/main.

> Remember: The last Python 3 version must be previously installed on the machine. Install through https://www.python.org/downloads/

## Usage

To run the **Interface** is necessary to run the pynetworktables2js.exe file. It is preffered to execute the file in the terminal from inside the directory.

**1. Open Interface's directory:**
```
cd "C:\Users\my-folder-path\src\main\deploy\UIv1"
```

**2. To run in simulation robot code:**
```
./pynetworktables2js.exe
```

**3. To run in the FRC-roborio through FRC Driver Station:**
```
./pynetworktables2js.exe --robot roborio-XXXX-frc.local
```
