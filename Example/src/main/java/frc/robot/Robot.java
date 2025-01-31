// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;




/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

  SparkMax neoIntake = new SparkMax(22, MotorType.kBrushless);

  CommandXboxController driverJoystick = new CommandXboxController(0);

  double speed = 0.7;

  SparkMaxConfig config = new SparkMaxConfig();

  TouchScreenInterface touchScreenInterface = new TouchScreenInterface();

      /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    config
    .idleMode(IdleMode.kBrake)
    .smartCurrentLimit(20);
    touchScreenInterface.enableInterface();
  }

  @Override
  public void robotPeriodic() {
    touchScreenInterface.periodic();
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    touchScreenInterface.getButton(TouchScreenInterface.Button.kA).onTrue(new InstantCommand(() -> neoIntake.set(0.15)));
    touchScreenInterface.getButton(TouchScreenInterface.Button.kA).onFalse(new InstantCommand(() -> neoIntake.set(0)));   

  }
  

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
