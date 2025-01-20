package frc.robocats.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class distanceSensor extends SubsystemBase {
  AnalogInput sensor;
  double tripVal = 4;

  /**
   * @param port The port that the sensor is connected to
   */
  public distanceSensor(int port) { sensor = new AnalogInput(port); }
  /**
   * @param port The port that the sensor is connected to 
   * @param tripValue The function isTripped() will return true if the distance is past this value
   */
  public distanceSensor(int port, double tripValue) {
    sensor = new AnalogInput(port);
    tripVal = tripValue;
  }

  /** Writes the voltage to SmartDashboard */
  public void writeVoltage() { SmartDashboard.putNumber("Distance sensor on port: " + sensor.getChannel() + " reads: ", sensor.getVoltage()); }
  /** @return If the sensor detects something */
  public boolean isTripped() { return sensor.getVoltage() > tripVal; }
}