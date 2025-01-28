package frc.robot;
// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.



import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ObjectiveTracker {
  private final NodeSelector selectorIO;
  public final Objective objective = new Objective();

  public static class Objective {
    public int nodeRow;
    public NodeLevel nodeLevel;
    public ConeOrientation coneOrientation;
    public boolean lastIntakeFront;

    public Objective(
        int nodeRow,
        NodeLevel nodeLevel,
        ConeOrientation coneOrientation,
        boolean lastIntakeFront) {
      this.nodeRow = nodeRow;
      this.nodeLevel = nodeLevel;
      this.coneOrientation = coneOrientation;
      this.lastIntakeFront = lastIntakeFront;
    }

    public Objective() {
      this(0, NodeLevel.HYBRID, ConeOrientation.UPRIGHT, false);
    }

    public boolean isConeNode() {
      return nodeLevel != NodeLevel.HYBRID
          && (nodeRow == 0
              || nodeRow == 2
              || nodeRow == 3
              || nodeRow == 5
              || nodeRow == 6
              || nodeRow == 8);
    }

    public ScoringSide getScoringSide() {
      if (nodeLevel == NodeLevel.HYBRID) {
        return ScoringSide.BACK;
      } else if (!isConeNode()) {
        return ScoringSide.EITHER;
      } else {
        return coneOrientation == ConeOrientation.TIPPED ^ lastIntakeFront
            ? ScoringSide.FRONT
            : ScoringSide.BACK;
      }
    }
  }

  public ObjectiveTracker(NodeSelector selectorIO) {
    System.out.println("[Init] Creating ObjectiveTracker");
    this.selectorIO = selectorIO;
  }

  public void periodic() {
    selectorIO.updateInputs();

    // Send cone orientation to selector
    selectorIO.setConeOrientation(objective.coneOrientation == ConeOrientation.TIPPED);

    

    // Send cone orientation to dashboard and LEDs
    SmartDashboard.putBoolean("Cone Tipped", objective.coneOrientation == ConeOrientation.TIPPED);
  }


  /** Command factory to toggle whether the cone is tipped. */
  public Command toggleConeOrientationCommand() {
    return Commands.runOnce(
            () -> {
              switch (objective.coneOrientation) {
                case UPRIGHT:
                  objective.coneOrientation = ConeOrientation.TIPPED;
                  break;
                case TIPPED:
                  objective.coneOrientation = ConeOrientation.UPRIGHT;
                  break;
              }
            })
        .ignoringDisable(true);
  }

  public static enum NodeLevel {
    HYBRID,
    MID,
    HIGH
  }

  public static enum ConeOrientation {
    UPRIGHT,
    TIPPED,
  }

  public static enum ScoringSide {
    FRONT,
    BACK,
    EITHER
  }

  public static enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN
  }
}
