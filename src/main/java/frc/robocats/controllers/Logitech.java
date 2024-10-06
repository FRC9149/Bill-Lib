package frc.robocats.controllers;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**A class that allows for input with a Logitech controller
 * As used in the first 2 years of the season.
 * (maybe more/less, but I can't see into the future)
 * @author El Campus - 2026
 * @since 2024-09-29
 */
public class Logitech implements Controller {
  //The gamepad that we are recieving inputs from
  private XboxController gamepad;
  private BooleanSupplier isX;

  /**
   * @param gamepadPort The port that the controller is plugged into
   */
  public Logitech(int gamepadPort) {
    //when gamepad is in Xbox mode, there are 6 axises (4 in D mode)
    // () -> is a supplier or lambda. it's used essentially just a function that you can use as a variable
    isX = () -> gamepad.getAxisCount() == 6;
  }

  public Trigger onA() {
    //There are 4 modes to the controller (2 of them we don't care about)
    //On the back of the controller there is a switch that changes from xbox controls to D controls (idk what D stands for)
    //isX returns true if we are in Xbox mode and then we find the correct button mapping from there
    //I found the button mapping by going to driverstation and hitting all the buttons in each mode (hard coded)
    //(the other 2 modes just switch the D-pad and left-joystick, so it has no application in code)

    int buttonNumber = isX.getAsBoolean() ? 1 : 2;
    return new JoystickButton(gamepad, buttonNumber);
  }
  public Trigger onB() {
    int buttonNumber = isX.getAsBoolean() ? 2 : 3;
    return new JoystickButton(gamepad, buttonNumber);
  }
  public Trigger onX() {
    int buttonNumber = isX.getAsBoolean() ? 3 : 1;
    return new JoystickButton(gamepad, buttonNumber);
  }
  public Trigger onY() {
    //Y is always 4 (for some reason)
    return new JoystickButton(gamepad, 4);
  }
  public Trigger onLeftBumper() {
    return new JoystickButton(gamepad, 5);
  }
  public Trigger onRightBumper() {
    return new JoystickButton(gamepad, 6);
  }
  public Trigger onBack() {
    int buttonNumber = isX.getAsBoolean() ? 7 : 9;
    return new JoystickButton(gamepad, buttonNumber);
  }
  public Trigger onStart() {
    int buttonNumber = isX.getAsBoolean() ? 8 : 10;
    return new JoystickButton(gamepad, buttonNumber);
  }
  public Trigger onLeftStickIn() {
    int buttonNumber = isX.getAsBoolean() ? 9 : 11;
    return new JoystickButton(gamepad, buttonNumber);
  }
  public Trigger onRightStickIn() {
    int buttonNumber = isX.getAsBoolean() ? 10 : 12;
    return new JoystickButton(gamepad, buttonNumber);
  }
  
  public Trigger onLeftTrigger(double threshold) {
    if( !isX.getAsBoolean() ) { return new JoystickButton(gamepad, 7); }
    return new Trigger(() -> gamepad.getLeftTriggerAxis() > threshold);
  }
  public Trigger onRightTrigger(double threshold) {
    if( !isX.getAsBoolean() ) { return new JoystickButton(gamepad, 8); }
    return new Trigger(() -> gamepad.getRightTriggerAxis() > threshold);
  }

  public double getLeftX()  { return gamepad.getLeftX(); }
  public double getLeftY()  { return gamepad.getLeftY(); }
  public double getRightX() { return gamepad.getRightX(); }
  public double getRightY() { return gamepad.getRightY(); }

  public Trigger onDPadUp()       { return new Trigger(() -> gamepad.getPOV() == 0); }
  public Trigger onDPadUpRight()  { return new Trigger(() -> gamepad.getPOV() == 45); }
  public Trigger onDPadRight()    { return new Trigger(() -> gamepad.getPOV() == 90); }
  public Trigger onDPadDownRight(){ return new Trigger(() -> gamepad.getPOV() == 135); }
  public Trigger onDPadDown()     { return new Trigger(() -> gamepad.getPOV() == 180); }
  public Trigger onDPadDownLeft() { return new Trigger(() -> gamepad.getPOV() == 225); }
  public Trigger onDPadLeft()     { return new Trigger(() -> gamepad.getPOV() == 270); }
  public Trigger onDPadUpLeft()   { return new Trigger(() -> gamepad.getPOV() == 315); }
  public Trigger onDPadNull()     { return new Trigger(() -> gamepad.getPOV() == -1); }
  public int getDpadAngle() { return gamepad.getPOV(); }
}