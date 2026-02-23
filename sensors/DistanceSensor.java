package com.robocats.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DistanceSensor extends SubsystemBase {
  private AnalogInput sensor;
  private double tripVal = 4;
  //TODO figure out and write what units the distance sensor uses
  /**
   * @param port The port that the sensor is connected to
   */
  public DistanceSensor(int port) { sensor = new AnalogInput(port); }
  /**
   * @param port The port that the sensor is connected to 
   * @param tripValue The function isTripped() will return true if the distance is past this value
   */
  public DistanceSensor(int port, double tripValue) {
    sensor = new AnalogInput(port);
    tripVal = tripValue;
  }

  /** Writes the voltage to SmartDashboard */
  public void writeVoltage() { SmartDashboard.putNumber("Distance sensor on port: " + sensor.getChannel() + " reads: ", sensor.getVoltage()); }
  /** @return If the sensor detects something */
  public boolean isTripped() { return sensor.getVoltage() > tripVal; }
  
  public void setTripValue(double tripValue) { tripVal = tripValue; }
  public double getTripValue() { return tripVal; }
}
