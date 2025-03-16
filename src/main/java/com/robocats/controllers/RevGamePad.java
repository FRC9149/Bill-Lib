package com.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RevGamePad implements Controller {
    private final Joystick joystick;
    public RevGamePad(int port) {
        joystick = new Joystick(port);
    }

    /**The X Button */
    public Trigger onA() { return new Trigger(() -> joystick.getRawButton(1)); }
    /**The O Button */
    public Trigger onB() { return new Trigger(() -> joystick.getRawButton(2)); }
    /**The Sqr Button */
    public Trigger onX() { return new Trigger(() -> joystick.getRawButton(3)); }
    /**The ▲ Button */
    public Trigger onY() { return new Trigger(() -> joystick.getRawButton(4)); }
    public Trigger onLeftBumper() { return new Trigger(() -> joystick.getRawButton(5)); }
    public Trigger onRightBumper() { return new Trigger(()-> joystick.getRawButton(6)); }
    public Trigger onBack() { return new Trigger(()-> joystick.getRawButton(7)); }
    public Trigger onStart() { return new Trigger(()-> joystick.getRawButton(8)); }
    public Trigger onLeftStickIn() { return new Trigger(()-> joystick.getRawButton(9)); }
    public Trigger onRightStickIn() { return new Trigger(()-> joystick.getRawButton(10)); }
    public Trigger onLeftTrigger(double threshold) { return new Trigger(() -> joystick.getRawAxis(2) >= threshold); }
    public Trigger onRightTrigger(double threshold) { return new Trigger(() -> joystick.getRawAxis(3) >= threshold); }
    public double getLeftTrigger() {
        return joystick.getRawAxis(2);
    }
    public double getRightTrigger() {
        return joystick.getRawAxis(3);
    }
    public double getLeftX() {
        return joystick.getRawAxis(0);
    }
    public double getLeftY() {
        return -joystick.getRawAxis(1);
    }
    public double getRightX() {
        return joystick.getRawAxis(4);
    }
    public double getRightY() {
        return -joystick.getRawAxis(5);
    }
    public Trigger onDPadUp() {
        return new Trigger(() -> joystick.getPOV() == 0);
    }
    public Trigger onDPadUpRight() {
        return new Trigger(() -> joystick.getPOV() == 45);
    }
    public Trigger onDPadRight() {
        return new Trigger(() -> joystick.getPOV() == 90);
    }
    public Trigger onDPadDownRight() {
        return new Trigger(() -> joystick.getPOV() == 135);
    }
    public Trigger onDPadDown() {
        return new Trigger(() -> joystick.getPOV() == 180);
    }
    public Trigger onDPadDownLeft() {
        return new Trigger(() -> joystick.getPOV() == 225);
    }
    public Trigger onDPadLeft() {
        return new Trigger(() -> joystick.getPOV() == 270);
    }
    public Trigger onDPadUpLeft() {
        return new Trigger(() -> joystick.getPOV() == 315);
    }
    public Trigger onDPadNull() {
        return new Trigger(() -> joystick.getPOV() == -1);
    }
    public int getDpadAngle() {
        return joystick.getPOV();
    }
}
