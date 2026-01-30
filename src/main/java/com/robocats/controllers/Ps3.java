package com.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//TODO: change the function names to match the controller

/**
 * A class for a wired ps3 controller
 * @author El Campus
 */
public class Ps3 {
    private Joystick controller;

    public Ps3(int port) {
        controller = new Joystick(port);
    }

    public Trigger onX() {
        return new Trigger(() -> controller.getRawButton(1));
    }

    public Trigger onO () {
        return new Trigger(() -> controller.getRawButton(2));
    }

    public Trigger onSquare() {
        return new Trigger(() -> controller.getRawButton(3));
    }

    public Trigger onTriangle() {
        return new Trigger(() -> controller.getRawButton(4));
    }
    public Trigger onLeftBumper() {
        return new Trigger(() -> controller.getRawButton(5));
    }
    public Trigger onRightBumper() {
        return new Trigger(() -> controller.getRawButton(6));
    }
    public Trigger onSelect() {
        return new Trigger(() -> controller.getRawButton(7));
    }
    public Trigger onStart() {
        return new Trigger(() -> controller.getRawButton(8));
    }
    public Trigger onLeftStickIn() {
        return new Trigger(() -> controller.getRawButton(9));
    }
    public Trigger onRightStickIn() {
        return new Trigger(() -> controller.getRawButton(10));
    }


    public Trigger onLeftTrigger(double threshold) {
        return new Trigger(() -> controller.getRawAxis(2) > threshold);
    }


    public double getLeftX() {
        return controller.getRawAxis(0);
    }

    public double getLeftY() {
        return controller.getRawAxis(1);
    }
    public Trigger onRightTrigger(double threshold) {
        return new Trigger(() -> controller.getRawAxis(3) > threshold);
    }

    public double getLeftTrigger() {
        return controller.getRawAxis(2);
    }

    public double getRightTrigger() {
        return controller.getRawAxis(3);
    }

    public double getRightX() {
        return -controller.getRawAxis(4);
    }

    public double getRightY() {
        return controller.getRawAxis(5);
    }

    public Trigger onDPadUp() {
        return new Trigger(() -> controller.getPOV() == 0);
    }

    public Trigger onDPadUpRight() {
        return new Trigger(() -> controller.getPOV() == 45);
    }

    public Trigger onDPadRight() {
        return new Trigger(() -> controller.getPOV() == 90);
    }

    public Trigger onDPadDownRight() {
        return new Trigger(() -> controller.getPOV() == 135);
    }

    public Trigger onDPadDown() {
        return new Trigger(() -> controller.getPOV() == 180);
    }

    public Trigger onDPadDownLeft() {
        return new Trigger(() -> controller.getPOV() == 225);
    }

    public Trigger onDPadLeft() {
        return new Trigger(() -> controller.getPOV() == 270);
    }

    public Trigger onDPadUpLeft() {
        return new Trigger(() -> controller.getPOV() == 315);
    }

    public Trigger onDPadNull() {
        return new Trigger(() -> controller.getPOV() == -1);
    }

    public int getDpadAngle() {
        return controller.getPOV();
    }
}
