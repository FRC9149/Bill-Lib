package com.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//TODO: change the function names to match the controller

public class Streetfightercontrol {  
    private Joystick joystick;
    public Streetfightercontrol(int port) {
        joystick = new Joystick(port);
    }

    /*
     * 7 8
     * 5 6
     * 3 4
     * 1 2
     */
    /**@return A triiger that is run when the left bottom-middle button is pressed */
    public Trigger onA() {
        return new Trigger(() -> joystick.getRawButton(3));
    }
    /**@return A triiger that is run when the right bottom-middle button is pressed */
    public Trigger onB() {
        return new Trigger(()-> joystick.getRawButton(4));
    }
    /**@return A triiger that is run when the left top-middle button is pressed */
    public Trigger onX() {
        return new Trigger(()-> joystick.getRawButton(5));
    }
    /**@return A triiger that is run when the right top-middle button is pressed */
    public Trigger onY() {
        return new Trigger(()-> joystick.getRawButton(6));
    }
    /**@return A triiger that is run when the top left button is pressed */
    public Trigger onLeftBumper() {
        return new Trigger(()-> joystick.getRawButton(7));
    }
    /**@return A triiger that is run when the top right button is pressed */
    public Trigger onRightBumper() {
        return new Trigger(()-> joystick.getRawButton(8));
    }
    public Trigger onBack() {
        return onRightBumper();
    }
    public Trigger onStart() {
        return onLeftBumper();
    }
    /**@return A triiger that is run when the bottom left button is pressed */
    public Trigger onLeftStickIn() {
        return new Trigger(()-> joystick.getRawButton(1));
    }
    /**@return A triiger that is run when the bottom right button is pressed */
    public Trigger onRightStickIn() {
        return new Trigger(()-> joystick.getRawButton(2));}
    public Trigger onLeftTrigger(double threshold) {
        return onLeftBumper();
    }
    public Trigger onRightTrigger(double threshold) {
        return onRightBumper();
    }
    public double getLeftTrigger() {
        return onLeftBumper().getAsBoolean() ? 1 : 0;
    }
    public double getRightTrigger() {
        return onRightBumper().getAsBoolean() ? 1 : 0;
    }
    /** Same as {@code onX();} 
     * @deprecated use onX()
    */
    public double getLeftX() {
        return onX().getAsBoolean() ? 1 : 0;
    }
    public double getLeftY() {
        return onY().getAsBoolean() ? 1 : 0;
    }
    public double getRightX() {
        return onA().getAsBoolean() ? 1 : 0;
    }
    public double getRightY() {
        return onB().getAsBoolean() ? 1 : 0;
    }
    public Trigger onDPadUp() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadUpRight() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadRight() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadDownRight() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadDown() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadDownLeft() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadLeft() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadUpLeft() {
        return new Trigger(()-> false);
    }
    public Trigger onDPadNull() {
        return new Trigger(()-> false);
    }
    public int getDpadAngle() {
        return -1;
    }
}
