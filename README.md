
# 2025 Megazord 7563 Operator Touch-screen Interface

Welcome to the base code repository for the **2025 Megazord Operator Touch-screen Interface (FRC Team 7563)**. This documentation provides an overview of how the interface works, how to install it, how to use and integrate it with the robot code, and some example codes.


https://github.com/user-attachments/assets/77dfcdde-2e52-4892-9046-d1d3d98797f8

## Robot Systems Overview

### Swerve Drive

The **Swerve Drive** enables precise, omnidirectional movement, allowing the robot to navigate the field with agility. The code is separated into three main components:

### 1. **SwerveModules**

This part of the code controls the output for each individual wheel. Each module is responsible for one wheel's direction and speed. The configuration of the modules is shown in the image below:

![Swerve Modules](https://github.com/user-attachments/assets/3db5946f-9272-4c8a-95ba-dd79039eae32)

### 2. **Swerve Subsystem / Command**

Here, the code combines the wheel outputs and creates **ChassisSpeeds** to control the robot’s movement. Joystick inputs are processed and translated into robot output speeds.

![Swerve Subsystem](https://github.com/user-attachments/assets/bf6685db-2e2a-4c8e-84aa-dfef68be0d86)

### 3. **Swerve Control System**

This section includes key controls to automate the robot's movements, such as:

- **Pose Estimator**: Used to estimate the robot's position on the field by combining encoder data and vision inputs.
- **Path Planner**: For autonomous navigation and path-following.

### Pose Estimator![Interface Overview](https://github.com/user-attachments/assets/8cbbb779-a0c5-41d8-b1ae-ad3111e58d50)


The **Pose Estimator** class wraps the **Swerve Drive Odometry** and helps fuse latency-compensated vision data with encoder data. This allows us to accurately estimate the robot's position on the field.

#### Instantiating the Pose Estimator:

```java
private final SwerveDrivePoseEstimator m_poseEstimator =
    new SwerveDrivePoseEstimator(
        DriveConstants.kDriveKinematics,
        gyro.getRotation2d(),
        new SwerveModulePosition[] {
          frontLeftModule.getPosition(),
          frontRightModule.getPosition(),![336fbbb8-718f-417d-a4b1-6f2b29deace9](https://github.com/user-attachments/assets/3201dba8-1a00-49cd-8536-19da4dc9f471)
![Interface Overview](https://github.com/user-attachments/assets/e61c251d-1bd4-4fc2-972f-4a350d0fabf1)

          backLeftModule.getPosition(),
          backRightModule.getPosition()
        },
        new Pose2d(),
        VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
        VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30))
    );
```

---

## Path Planner

**Path Planner** is a motion profile generator used in FRC robots. It allows for precise path-following and generates motion profiles using Bézier curves. Some notable features of **Path Planner** include:

- **Holonomic mode**: This mode decouples the robot's rotation from its direction of travel, allowing for more flexible path following.
- **Real-time path preview**: Visualize paths in real-time during robot operation.
- **Event markers**: Markers can trigger other code along the path, such as triggering actions at specific points during path following.
- **Path generation**: Create motion profiles and commands for autonomous routines.
- **Path reloading**: Paths can be reloaded on the robot without the need to redeploy code.

### Instantiating Path Planner

To set up **Path Planner**, we configure the **AutoBuilder** with the required parameters, such as the robot's pose, odometry reset method, and driving method:

```java
AutoBuilder.configure(
    this::getPoseEstimator,        // Robot pose supplier
    this::resetOdometry,           // Method to reset odometry
    this::getChassisSpeeds,        // ChassisSpeeds supplier (ROBOT RELATIVE)
    this::drive,                   // Method to drive the robot (ROBOT RELATIVE)
    PathPlannerConstants.AutoConfig, 
    config,                        // Robot configuration
    () -> {
        // Control path mirroring for the red alliance
        var alliance = DriverStation.getAlliance();
        return alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red;
    },
    this                            // Reference to this subsystem
);
```

In this code, we configure the **AutoBuilder** with the following:
- A **pose estimator** to provide the current pose of the robot.
- A method to **reset odometry** when a new autonomous path starts.
- A **ChassisSpeeds supplier** that provides robot velocities relative to the robot's frame of reference.
- A **drive method** to apply velocities to the robot's motors.

### In the Robot Container

To use **Path Planner** for autonomous routines, define an autonomous path and add it to the **SendableChooser**. This allows the robot to select and execute specific paths at runtime:

```java
PathPlannerAuto autoTest = new PathPlannerAuto("AutoTest");
chooserAuto.addOption("AutoTest", autoTest);
```

This code adds a new autonomous routine named "AutoTest" to the **SendableChooser**, which will allow the robot to choose this routine during a match.

---

## Pathfinding

**Pathfinding** with **PathPlannerLib** enables the robot to automatically generate a path between two points while avoiding obstacles. The generated path can be combined with pre-planned paths for more refined control.

### Initializing Pathfinding

To use the **Pathfinding** capabilities, first create the necessary constraints (e.g., velocity, acceleration, etc.):

```java
// Pathfinding constraints
PathConstraints constraints = new PathConstraints(
    maxVelocity, 
    PathPlannerConstants.maxAccelerationPath,
    PathPlannerConstants.maxAngularVelocityRadPerSec,
    PathPlannerConstants.maxAngularAccelerationRadPerSecSq
);

// Building the pathfinding command
pathFindingCommand = AutoBuilder.pathfindToPose(
    targetPose,
    constraints,
    setEndVelocity  // Goal end velocity in meters/sec
);

pathFindingCommand.schedule();
```

This code defines the constraints for pathfinding, including maximum velocity, angular velocity, and acceleration. Then, it creates the pathfinding command that will generate a path to the target pose and execute the movement.

### Stopping Pathfinding

When pathfinding is no longer needed (for example, if the task is finished or the robot encounters an obstacle), the following method can be called to cancel the pathfinding command and stop the robot:

```java
public void endCommand() {
    pathFindingCommand.cancel();
    PPHolonomicDriveController.overrideXYFeedback(() -> swerveDrive.getPoseEstimator().getX(), 
                                                   () -> swerveDrive.getPoseEstimator().getY());
    PPHolonomicDriveController.overrideRotationFeedback(() -> swerveDrive.getPoseEstimator().getRotation().getRadians());
    System.out.println("EndCommand");
    finish = true;
}
```

This code cancels the ongoing **Pathfinding** command and stops the robot's movement. It also overrides the robot's feedback controls to ensure that the robot is no longer following the generated path.
```

```
## Swerve initialization

### Pigeon Orientation

![image](https://github.com/user-attachments/assets/daf11b26-5f23-4e2e-bedb-bd8ce64805d8)

Always the front side of the robot must be turned to the red side of the field.
