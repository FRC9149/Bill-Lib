package com.robocats.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A class for a wired ps3 controller
 * @author El Campus - grad 2026
 * @author 404.el.404@gmail.com
 * @since 2025-02-11
 */
public class Ps3 implements Controller {
    private XboxController cont;

    public Ps3(int port) {
        cont = new XboxController(port);
    }

    @Override
    public Trigger onA() {
        return new Trigger(cont::getAButton);
    }

    @Override
    public Trigger onB() {
        return new Trigger(cont::getBButton);
    }

    @Override
    public Trigger onX() {
        return new Trigger(cont::getXButton);
    }

    @Override
    public Trigger onY() {
        return new Trigger(cont::getYButton);
    }

    @Override
    public Trigger onLeftBumper() {
        return new Trigger(cont::getLeftBumperButton);
    }

    @Override
    public Trigger onRightBumper() {
        return new Trigger(cont::getRightBumperButton);
    }

    @Override
    public Trigger onBack() {
        return new Trigger(cont::getBackButton);
    }

    @Override
    public Trigger onStart() {
        return new Trigger(cont::getStartButton);
    }

    @Override
    public Trigger onLeftStickIn() {
        return new Trigger(cont::getLeftStickButton);
    }

    @Override
    public Trigger onRightStickIn() {
        return new Trigger(cont::getRightStickButton);
    }

    @Override
    public Trigger onLeftTrigger(double threshold) {
        return new Trigger(() -> cont.getLeftTriggerAxis() > threshold);
    }

    @Override
    public Trigger onRightTrigger(double threshold) {
        return new Trigger(() -> cont.getRightTriggerAxis() > threshold);
    }

    @Override
    public double getLeftTrigger() {
        return cont.getLeftTriggerAxis();
    }

    @Override
    public double getRightTrigger() {
        return cont.getRightTriggerAxis();
    }

    @Override
    public double getLeftX() {
        return -cont.getLeftX();
    }

    @Override
    public double getLeftY() {
        return cont.getLeftY();
    }

    @Override
    public double getRightX() {
        return -cont.getRightX();
    }

    @Override
    public double getRightY() {
        return cont.getRightY();
    }

    @Override
    public Trigger onDPadUp() {
        return new Trigger(() -> cont.getPOV() == 0);
    }

    @Override
    public Trigger onDPadUpRight() {
        return new Trigger(() -> cont.getPOV() == 45);
    }

    @Override
    public Trigger onDPadRight() {
        return new Trigger(() -> cont.getPOV() == 90);
    }

    @Override
    public Trigger onDPadDownRight() {
        return new Trigger(() -> cont.getPOV() == 135);
    }

    @Override
    public Trigger onDPadDown() {
        return new Trigger(() -> cont.getPOV() == 180);
    }

    @Override
    public Trigger onDPadDownLeft() {
        return new Trigger(() -> cont.getPOV() == 225);
    }

    @Override
    public Trigger onDPadLeft() {
        return new Trigger(() -> cont.getPOV() == 270);
    }

    @Override
    public Trigger onDPadUpLeft() {
        return new Trigger(() -> cont.getPOV() == 315);
    }

    @Override
    public Trigger onDPadNull() {
        return new Trigger(() -> cont.getPOV() == -1);
    }

    @Override
    public int getDpadAngle() {
        return cont.getPOV();
    }
}
