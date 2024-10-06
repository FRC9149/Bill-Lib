package frc.robocats.controllers;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/** An interface to apply universal Controls.
 * To implement a controller you must define how each button is processed.
 * @author El Campus - 2026
 * @since 2024-09-29
 */
public interface Controller {
  /**@return a trigger that is called when the A button is pressed. */
  public Trigger onA();
  /**@return a trigger that is called when the B button is pressed. */
  public Trigger onB();
  /**@return a trigger that is called when the X button is pressed. */
  public Trigger onX();
  /**@return a trigger that is called when the Y button is pressed. */
  public Trigger onY();
  /**@return a trigger that is called when the left bumper is pressed. */
  public Trigger onLeftBumper();
  /**@return a trigger that is called when the right bumper is pressed. */
  public Trigger onRightBumper();
  /**@return a trigger that is called when the back button is pressed. */
  public Trigger onBack();
  /**@return a trigger that is called when the start button is pressed. */
  public Trigger onStart();
  /**@return a trigger that is called when the left stick is pressed. */
  public Trigger onLeftStickIn();
  /**@return a trigger that is called when the right stick is pressed. */
  public Trigger onRightStickIn();

  /**
   * @param threshold a number bewtween 0 and 1 (not 0). defines how far you have to press the Trigger
   * @return A trigger that is called when that threshold is reached
   */
  public Trigger onLeftTrigger(double threshold);
  /**
   * @param threshold a number bewtween 0 and 1 (not 0). defines how far you have to press the Trigger
   * @return A trigger that is called when that threshold is reached
   */
  public Trigger onRightTrigger(double threshold);

  /**@return a number between -1, 1 representing where the Left joystick is (left to right) */
  public double getLeftX();
   /**@return a number between -1, 1 representing where the Left joystick is (up and down) */
  public double getLeftY();
   /**@return a number between -1, 1 representing where the Right joystick is (left to right) */
  public double getRightX();
   /**@return a number between -1, 1 representing where the Right joystick is (up and down) */
  public double getRightY();

  /**@return a trigger that is called when up is pressed on the D-pad. */
  public Trigger onDPadUp();
  /**@return a trigger that is called when up-right is pressed on the D-pad. */
  public Trigger onDPadUpRight();
  /**@return a trigger that is called when right is pressed on the D-pad. */
  public Trigger onDPadRight();
  /**@return a trigger that is called when down-right is pressed on the D-pad. */
  public Trigger onDPadDownRight();
  /**@return a trigger that is called when down is pressed on the D-pad. */
  public Trigger onDPadDown();
  /**@return a trigger that is called when down-left is pressed on the D-pad. */
  public Trigger onDPadDownLeft();
  /**@return a trigger that is called when left is pressed on the D-pad. */
  public Trigger onDPadLeft();
  /**@return a trigger that is called when up-left is pressed on the D-pad. */
  public Trigger onDPadUpLeft();
  /**@return a trigger that is called when nothing is pressed on the D-pad */
  public Trigger onDPadNull();
  /**@return which way the directional pad is being pressed
   * up: 0; up-right:45, left:90, etc
   * not being press returns -1
   */
  public int getDpadAngle();
}