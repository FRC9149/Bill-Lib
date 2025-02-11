package com.robocats.controllers;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/** <h2>An interface to apply semi-universal Controls.</h2>
 * <p>To implement a controller you must define how each button is processed.</p>
 * Each field should return a value, but some functions may be redundant.
 * @author El Campus - grad 2026
 * @author 404.el.404@gmail.com
 * @since 2025-01-20
 */
public interface Controller {
  /** A Trigger that is generally used for functionallity, such as picking up an object.
   * @return a trigger that is called when the A button is pressed. */
  public Trigger onA();
  /** A Trigger generally used as a secondary action or movement.
   * @return a trigger that is called when the B button is pressed. */
  public Trigger onB();
  /** A Trigger generally used as a secondary action.
   * @return a trigger that is called when the X button is pressed. */
  public Trigger onX();
  /** A Trigger generally used as a secondary action.
   * @return a trigger that is called when the Y button is pressed. */
  public Trigger onY();
  /** A Trigger generally used as a secondary Input or output.
   * @return a trigger that is called when the left bumper is pressed. */
  public Trigger onLeftBumper();
  /** A Trigger generally used as a secondary Input or output.
   * @return a trigger that is called when the right bumper is pressed. */
  public Trigger onRightBumper();
  /** A Trigger generally used as a misc function call, such as screenshot or zero gyro.
   * @return a trigger that is called when the back button is pressed. */
  public Trigger onBack();
  /** A Trigger generally used as a misc function call, such as screenshot or zero gyro.
   * @return a trigger that is called when the start button is pressed. */
  public Trigger onStart();
  /** A Trigger generally used for debugging as it's awkward to use.
   * @return a trigger that is called when the left stick is pressed. */
  public Trigger onLeftStickIn();
  /** A Trigger generally used for debugging as it's awkward to use.
   * @return a trigger that is called when the right stick is pressed. */
  public Trigger onRightStickIn();

  /** A Trigger that is generally used for the speed of the robot or secondary output.
   * @param threshold a number bewtween 0 and 1 (not 0). defines how far you have to press the Trigger for it to activate
   * @return A trigger that is called when that threshold is reached
   */
  public Trigger onLeftTrigger(double threshold);
  /** A Trigger that is generally used for the speed of the robot or output.
   * @param threshold a number bewtween 0 and 1 (not 0). defines how far you have to press the Trigger for it to activate
   * @return A trigger that is called when that threshold is reached
   */
  public Trigger onRightTrigger(double threshold);
  /** @return The percent of depression on the left trigger. */
  public double getLeftTrigger();
  /** @return  The percent of depression on the right trigger. */
  public double getRightTrigger();

  /**@return a number between -1, 1 representing where the Left joystick is (left to right) */
  public double getLeftX();
   /**@return a number between -1, 1 representing where the Left joystick is (up and down) */
  public double getLeftY();
   /**@return a number between -1, 1 representing where the Right joystick is (left to right) */
  public double getRightX();
   /**@return a number between -1, 1 representing where the Right joystick is (up and down) */
  public double getRightY();

  /**@return a trigger that is called when up is pressed on the D-pad. Has a value of 0. */
  public Trigger onDPadUp();
  /**@return a trigger that is called when up-right is pressed on the D-pad. Has a value of 45. */
  public Trigger onDPadUpRight();
  /**@return a trigger that is called when right is pressed on the D-pad. Has a value of 90. */
  public Trigger onDPadRight();
  /**@return a trigger that is called when down-right is pressed on the D-pad. Has a value of 135. */
  public Trigger onDPadDownRight();
  /**@return a trigger that is called when down is pressed on the D-pad. Has a value of 180.*/
  public Trigger onDPadDown();
  /**@return a trigger that is called when down-left is pressed on the D-pad. Has a value of 225.*/
  public Trigger onDPadDownLeft();
  /**@return a trigger that is called when left is pressed on the D-pad. Has a value of 270.*/
  public Trigger onDPadLeft();
  /**@return a trigger that is called when up-left is pressed on the D-pad. Has a value of 315.*/
  public Trigger onDPadUpLeft();
  /**@return a trigger that is called when nothing is pressed on the D-pad Has a value of -1.*/
  public Trigger onDPadNull();
  /** <h6>IDK what DPad would generally be used for. Add it if you want</h6>
   * @return <p>which way the directional pad is being pressed</p>
   * <p>up: 0; up-right:45, right:90, etc</p>
   * not being press returns -1
   */
  public int getDpadAngle();
}