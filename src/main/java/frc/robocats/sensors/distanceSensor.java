package frc.robocats.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class distanceSensor extends SubsystemBase {
  AnalogInput sensor;
  double tripVal = 4;

  public distanceSensor(int port) { sensor = new AnalogInput(port); }
  /**
   * @param port The port that the sensor is connected to 
   * @param tripValue The function isTripped() will return true if the distance is past this value
   */
  public distanceSensor(int port, double tripValue) {
    sensor = new AnalogInput(port);
    tripVal = tripValue;
  }

  /** Writes to SmartDashboard the voltage */
  public void writeVoltage() { SmartDashboard.putNumber("Distance sensor on port: " + sensor.getChannel() + " reads: ", sensor.getVoltage()); }
  /** @return If the sensor detects something */
  public boolean isTripped() { return sensor.getVoltage() > tripVal; }
}