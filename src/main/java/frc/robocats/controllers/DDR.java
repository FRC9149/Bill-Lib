package frc.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class DDR implements Controller {
    private Joystick gamepad;

    /** A class that allows for input with a DDR controller
     * {@link https://www.amazon.com/YtotY-Dancing-Controller-Fitness-Building/dp/B0D7JLQ5F3/ref=sr_1_1_sspa?crid=15CNVFJ4696VF&dib=eyJ2IjoiMSJ9.yN8-2aycoSmPBkqCMkmmuw9h4iztoJHV1An5-wXCpONUXwywjgVuIap96OqADIVT2RubsZ115H_NWCY8MPXjBafYzB9TVUzwwPofR6N88gI58Em3dvoT-GCI46HTo5cJt5M1XputcQYE2biKKmQlhAICC7MWbcFiPxlzxTLHm7sa8fQUDTm413GYPLqUTmEZFnIqg3uEcHusjPGoli-1DfCC9ENmpMMjknWEj8WRff0.7KC0BrPsT6eCqVX4nXqTjFdCHfqAMf8Z7SdeFaqpfL8&dib_tag=se&keywords=dance+pad+pc&qid=1737413280&sprefix=dance+pad+pc%2Caps%2C176&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1}
     * @param gamepadPort The port that the controller is on
     */
    public DDR(int gamepadPort) {
        gamepad = new Joystick(gamepadPort);
    }

    public boolean raw_getUp() { return gamepad.getRawButton(3); }
    public boolean raw_getRight() { return gamepad.getRawButton(4); }
    public boolean raw_getDown() { return gamepad.getRawButton(2); }
    public boolean raw_getLeft() { return gamepad.getRawButton(1); }
    public boolean raw_getX() { return gamepad.getRawButton(7); }
    public boolean raw_getO() { return gamepad.getRawButton(8); }
    public boolean raw_getTri() { return gamepad.getRawButton(5); }
    public boolean raw_getSqr() { return gamepad.getRawButton(6); }
    public boolean raw_getSelect() { return gamepad.getRawButton(9); }
    public boolean raw_getStart() { return gamepad.getRawButton(10); }
    public boolean raw_getCenter() { return gamepad.getRawAxis(4) > 0.5; }



    /**
     * @return A trigger that runs when you press the Triangle and center Circle
     */
    public Trigger onA() {
        return new Trigger(() -> raw_getTri() && raw_getCenter());
    }

    /**
     * @return A trigger that runs when you press the Square and center Circle
     */
    public Trigger onB() {
        return new Trigger(() -> raw_getSqr() && raw_getCenter());
    }

    /**
     * @return A trigger that runs when you press the X and center Circle
     */
    public Trigger onX() {
        return new Trigger(() -> raw_getX() && raw_getCenter());
    }

    /**
     * @return A trigger that runs when you press the O and center Circle
     */
    public Trigger onY() {
        return new Trigger(() -> raw_getO() && raw_getCenter());
    }

    /**
     * @return A trigger that runs when you press Select and center Circle
     */
    public Trigger onLeftBumper() {
        return new Trigger(() -> raw_getSelect() && raw_getCenter());
    }

    /**
     * @return A trigger that runs when you press Start and center Circle
     */
    public Trigger onRightBumper() {
        return new Trigger(() -> raw_getStart() && raw_getCenter());
    }

    /**
     * @return A trigger that runs when you press Select
     */
    public Trigger onBack() {
        return new Trigger(() -> raw_getSelect());
    }

    /**
     * @return A trigger that runs when you press Start
     */
    public Trigger onStart() {
        return new Trigger(() -> raw_getStart());
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public Trigger onLeftStickIn() {
        throw new UnsupportedOperationException("Unimplemented method 'onLeftStickIn'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public Trigger onRightStickIn() {
        throw new UnsupportedOperationException("Unimplemented method 'onRightStickIn'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public Trigger onLeftTrigger(double threshold) {
        throw new UnsupportedOperationException("Unimplemented method 'onLeftTrigger'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public Trigger onRightTrigger(double threshold) {
        throw new UnsupportedOperationException("Unimplemented method 'onRightTrigger'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    @Override
    public double getLeftTrigger() {
        throw new UnsupportedOperationException("Unimplemented method 'getLeftTrigger'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public double getRightTrigger() {
        throw new UnsupportedOperationException("Unimplemented method 'getRightTrigger'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public double getLeftX() {
        throw new UnsupportedOperationException("Unimplemented method 'getLeftX'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public double getLeftY() {
        throw new UnsupportedOperationException("Unimplemented method 'getLeftY'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public double getRightX() {
        throw new UnsupportedOperationException("Unimplemented method 'getRightX'");
    }

    /** unimplemented
     * @deprecated will throw UnsupportedOperationException
     */
    public double getRightY() {
        throw new UnsupportedOperationException("Unimplemented method 'getRightY'");
    }

    /**
     * @return A trigger that runs when you press the Up Arrow
     */
    public Trigger onDPadUp() {
        return new Trigger(() -> raw_getUp());
    }

    /**
     * @return A trigger that runs when you press the Up Arrow and Right Arrow
     */
    public Trigger onDPadUpRight() {
        return new Trigger(() -> raw_getUp() && raw_getRight());
    }

    /**
     * @return A trigger that runs when you press the Right Arrow
     */
    public Trigger onDPadRight() {
        return new Trigger(() -> raw_getRight());
    }

    /**
     * @return A trigger that runs when you press the Down Arrow and Right Arrow
     */
    public Trigger onDPadDownRight() {
        return new Trigger(() -> raw_getDown() && raw_getRight());
    }

    /**
     * @return A trigger that runs when you press the Down Arrow
     */
    public Trigger onDPadDown() {
        return new Trigger(() -> raw_getDown());
    }

    /**
     * @return A trigger that runs when you press the Down Arrow and Left Arrow
     */
    public Trigger onDPadDownLeft() {
        return new Trigger(() -> raw_getDown() && raw_getLeft());
    }

    /**
     * @return A trigger that runs when you press the Left Arrow
     */
    public Trigger onDPadLeft() {
        return new Trigger(() -> raw_getLeft());
    }

    /**
     * @return A trigger that runs when you press the Up Arrow and Left Arrow
     */
   public Trigger onDPadUpLeft() {
        return new Trigger(() -> raw_getUp() && raw_getLeft());
    }

    /**
     * @return A trigger that runs when you don't press any arrows
     */
    public Trigger onDPadNull() {
        return new Trigger(() -> !raw_getUp() && !raw_getRight() && !raw_getDown() && !raw_getLeft());
    }

    public int getDpadAngle() {
             if (raw_getUp())                       {return 0;}
        else if (raw_getUp() && raw_getRight())     {return 45;}
        else if (raw_getRight())                    {return 90;}
        else if (raw_getDown() && raw_getRight())   {return 135;}
        else if (raw_getDown())                     {return 180;}
        else if (raw_getDown() && raw_getLeft())    {return 225;}
        else if (raw_getLeft())                     {return 270;}
        else if (raw_getUp() && raw_getLeft())      {return 315;}
        else                                        {return -1;}
    }
}
