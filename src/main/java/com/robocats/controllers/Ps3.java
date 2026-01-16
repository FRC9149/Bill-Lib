package com.robocats.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

//TODO: change the function names to match the controller

/**
 * A class for a wired ps3 controller
 * @author El Campus
 */
public class Ps3 {
    private XboxController cont;

    public Ps3(int port) {
        cont = new XboxController(port);
    }

    public Trigger onA() {
        return new Trigger(cont::getAButton);
    }

    public Trigger onB() {
        return new Trigger(cont::getBButton);
    }

    public Trigger onX() {
        return new Trigger(cont::getXButton);
    }

    public Trigger onY() {
        return new Trigger(cont::getYButton);
    }

    public Trigger onLeftBumper() {
        return new Trigger(cont::getLeftBumperButton);
    }

    public Trigger onRightBumper() {
        return new Trigger(cont::getRightBumperButton);
    }

    public Trigger onBack() {
        return new Trigger(cont::getBackButton);
    }

    public Trigger onStart() {
        return new Trigger(cont::getStartButton);
    }

    public Trigger onLeftStickIn() {
        return new Trigger(cont::getLeftStickButton);
    }

    public Trigger onRightStickIn() {
        return new Trigger(cont::getRightStickButton);
    }

    public Trigger onLeftTrigger(double threshold) {
        return new Trigger(() -> cont.getLeftTriggerAxis() > threshold);
    }

    public Trigger onRightTrigger(double threshold) {
        return new Trigger(() -> cont.getRightTriggerAxis() > threshold);
    }

    public double getLeftTrigger() {
        return cont.getLeftTriggerAxis();
    }

    public double getRightTrigger() {
        return cont.getRightTriggerAxis();
    }

    public double getLeftX() {
        return -cont.getLeftX();
    }

    public double getLeftY() {
        return cont.getLeftY();
    }

    public double getRightX() {
        return -cont.getRightX();
    }

    public double getRightY() {
        return cont.getRightY();
    }

    public Trigger onDPadUp() {
        return new Trigger(() -> cont.getPOV() == 0);
    }

    public Trigger onDPadUpRight() {
        return new Trigger(() -> cont.getPOV() == 45);
    }

    public Trigger onDPadRight() {
        return new Trigger(() -> cont.getPOV() == 90);
    }

    public Trigger onDPadDownRight() {
        return new Trigger(() -> cont.getPOV() == 135);
    }

    public Trigger onDPadDown() {
        return new Trigger(() -> cont.getPOV() == 180);
    }

    public Trigger onDPadDownLeft() {
        return new Trigger(() -> cont.getPOV() == 225);
    }

    public Trigger onDPadLeft() {
        return new Trigger(() -> cont.getPOV() == 270);
    }

    public Trigger onDPadUpLeft() {
        return new Trigger(() -> cont.getPOV() == 315);
    }

    public Trigger onDPadNull() {
        return new Trigger(() -> cont.getPOV() == -1);
    }

    public int getDpadAngle() {
        return cont.getPOV();
    }
}
