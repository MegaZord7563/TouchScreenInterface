package frc.robot;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.IOException;
import java.nio.file.Paths;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class NodeSelector {
  private final IntegerPublisher nodePublisher;
  private final IntegerSubscriber nodeSubscriber;
  private final BooleanPublisher coneTippedPublisher;
  private final BooleanSubscriber coneTippedSubscriber;
  private final IntegerPublisher timePublisher;
  private final BooleanPublisher isAutoPublisher;

  public NodeSelector() {
    System.out.println("[Init] Creating NodeSelectorIOServer");

    // Create publisher and subscriber
    var table = NetworkTableInstance.getDefault().getTable("nodeselector");
    nodePublisher = table.getIntegerTopic("node_robot_to_dashboard").publish();
    nodeSubscriber = table.getIntegerTopic("node_dashboard_to_robot").subscribe(-1);
    coneTippedPublisher = table.getBooleanTopic("cone_tipped_robot_to_dashboard").publish();
    coneTippedSubscriber = table.getBooleanTopic("cone_tipped_dashboard_to_robot").subscribe(false);
    timePublisher = table.getIntegerTopic("match_time").publish();
    isAutoPublisher = table.getBooleanTopic("is_auto").publish();

    // Start server
    var app =
        Javalin.create(
            config -> {
              config.staticFiles.add(
                  Paths.get(
                          Filesystem.getDeployDirectory().getAbsolutePath().toString(),
                          "nodeselector")
                      .toString(),
                  Location.EXTERNAL);
            });
            app.start(5800);
/*

    // Configure server to bind to a specific IP address
    Server server = app.jettyServer().server();
    ServerConnector connector = new ServerConnector(server);
    connector.setHost("127.0.0.1"); // Replace with your desired IP address
    connector.setPort(5321);
    server.setConnectors(new Connector[] {connector});

    app.start();
//*/

/*

    // Configure server to bind to a specific IP address
    Server server = app.jettyServer().server();
    boolean bound = false;
    for (int port = 5000; port <= 6000; port++) {
      try {
        ServerConnector connector = new ServerConnector(server);
        connector.setHost("127.0.0.1"); // Replace with your desired IP address
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});
        app.start();
        System.out.println("Server started on port " + port);
        bound = true;
        break;
      } catch (Exception e) {
        System.out.println("Port " + port + " is in use, trying next port...");
      }
    }

    if (!bound) {
      throw new RuntimeException("Failed to bind to any port in the range 5900-6000");
    }

  //*/
  }

  public void updateInputs() {
    timePublisher.set((long) Math.ceil(Math.max(0.0, DriverStation.getMatchTime())));
    isAutoPublisher.set(DriverStation.isAutonomous());
    long[] longValues = nodeSubscriber.readQueueValues();
    Double[] doubleValues = new Double[longValues.length];
    for (int i = 0; i < longValues.length; i++) {
        doubleValues[i] = (double) longValues[i];
    }
    SmartDashboard.putNumberArray("NodeSubscriber", doubleValues);
    SmartDashboard.putBooleanArray("ConeTippedSubscriber", coneTippedSubscriber.readQueueValues());
  }

  public void setSelected(long selected) {
    nodePublisher.set(selected);
  }

  public void setConeOrientation(boolean tipped) {
    coneTippedPublisher.set(tipped);
  }
}