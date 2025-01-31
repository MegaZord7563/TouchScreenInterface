
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

Note: NetworkTables is a protocol used for robot communication in the FIRST Robotics Competition and can be used to talk to Shuffleboard/SmartDashboard. It does not have any security, and should never be used on untrusted networks.

## Documentation

Documentation can be found at http://pynetworktables2js.readthedocs.org/

## Installation

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

---

## Implementing in the code

Similar to the XboxController class, the **Interface** has its own easy-to-control subsystem class: the `TouchScreenInterface.java`. That way, to implement the **Interface** in the robot code it is only necessary to copy the `TouchScreenInterface.java` class in the Subsystems directory.

```java
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.BooleanTopic;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.NetworkButton;

public class TouchScreenInterface extends SubsystemBase {


  public enum Button {
    /** A button. */
    kA("A"),
    /** B button. */
    kB("B"),
    /** C button. */
    kC("C"),
    /** D button. */
    kD("D"),
    /** E button. */
    kE("E"),
    /** F button. */
    kF("F"),
    /** G button. */
    kG("G"),
    /** H button. */
    kH("H"),
    /** I button. */
    kI("I"),
    /** J button. */
    kJ("J"),
    /** K button. */
    kK("K"),
    /** L button. */
    kL("L"),
    /** L button. */
    kL1("L1"),
    /** L button. */
    kL2("L2"),
    /** L button. */
    kL3("L3"),
    /** L button. */
    kL4("L4");

    public final String value;

    Button(String value) {
      this.value = value;
    }

    /**
     * Get the human-friendly name of the button, matching the relevant methods. This is done by
     * stripping the leading `k`, and appending `Button`.
     *
     * <p>Primarily used for automated unit tests.
     *
     * @return the human-friendly name of the button.
     */
    @Override
    public String toString() {
      // Remove leading `k`
      return this.name().substring(1) + "Button";
    }
  }

  // Instance NetworkTables and Entries 

  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  BooleanTopic booleanTopic[] = new BooleanTopic[16];
  boolean[] btnValue = new boolean[16];
  BooleanEntry[] bEntry = new BooleanEntry[16];
  NetworkButton[] networkButtons = new NetworkButton[16];

  boolean enableInterface = false;


  /** Creates a new TouchScreenInterface. */
  public TouchScreenInterface() {
    for (Button button : Button.values()) {
      // Create a new boolean topic for each button
      booleanTopic[button.ordinal()] = new BooleanTopic(inst.getBooleanTopic(button.toString()));
      // Set the default value of the button to false
      btnValue[button.ordinal()] = false;
      // Create a new boolean entry for each button
      bEntry[button.ordinal()] = booleanTopic[button.ordinal()].getEntryEx("boolean", btnValue[button.ordinal()]);
      // Create a new network button for each button
      networkButtons[button.ordinal()] = new NetworkButton(bEntry[button.ordinal()].getTopic()
                                              .subscribeEx("boolean", btnValue[button.ordinal()]));
      // Set the default value of the button to false and put it on the SmartDashboard
      SmartDashboard.putBoolean(button.toString()+"Value", btnValue[button.ordinal()]);
    }
    // Put the enableInterface boolean on the SmartDashboard
    SmartDashboard.putBoolean("enableInterface", enableInterface);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Get the value of the enableInterface boolean from the SmartDashboard
    enableInterface = SmartDashboard.putBoolean("enableInterface", isInterfaceEnabled());
    for (Button button : Button.values()) {
      // Get the value of the button from the SmartDashboard
      btnValue[button.ordinal()] = SmartDashboard.getBoolean(button.toString()+"Value", btnValue[button.ordinal()]);
      // Set the value of the button to the value from the SmartDashboard
      bEntry[button.ordinal()].get(btnValue[button.ordinal()]);
      bEntry[button.ordinal()].set(btnValue[button.ordinal()]);
    }

  }

  // Get the boolean value of the button
  public boolean getButtonValue(Button button) {
    return networkButtons[button.ordinal()].getAsBoolean();
  }

  // Get the NetworkButton object of the button
  public NetworkButton getButton(Button button) {
    return networkButtons[button.ordinal()];
  }

  // Enable or disable the interface
  public void enableInterface() {
    enableInterface = true;
  }

  // Enable or disable the interface
  public void disableInterface() {
    enableInterface = false;
  }

  // Get the value of the enableInterface boolean
  public boolean isInterfaceEnabled() {
    return enableInterface;
  }

}
```

## Optimizing the NetworkTables 

By default, the `TouchScreenInterface.java` instantiates sixteen NetworkButton; however, if the network is overloaded, there is an option to comment those buttons and only use the boolean values. To do so, comment the following lines:

```java
[...]

-->     // Create a new boolean topic for each button
-->    booleanTopic[button.ordinal()] = new BooleanTopic(inst.getBooleanTopic(button.toString()));

[...]

-->     // Create a new boolean entry for each button
-->     bEntry[button.ordinal()] = booleanTopic[button.ordinal()].getEntryEx("boolean", btnValue[button.ordinal()]);
-->     // Create a new network button for each button
-->     networkButtons[button.ordinal()] = new NetworkButton(bEntry[button.ordinal()].getTopic()
                                              .subscribeEx("boolean", btnValue[button.ordinal()]));
[...]
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    for (Button button : Button.values()) {
-->      // Set the value of the button to the value from the SmartDashboard
-->      bEntry[button.ordinal()].get(btnValue[button.ordinal()]);
-->      bEntry[button.ordinal()].set(btnValue[button.ordinal()]);
    }

[...]
```
