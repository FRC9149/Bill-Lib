package com.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** <p>A class that allows for input with a DancePad</p>
 * <a href="https://www.amazon.com/YtotY-Dancing-Controller-Fitness-Building/dp/B0D7JLQ5F3/ref=sr_1_1_sspa?crid=15CNVFJ4696VF&dib=eyJ2IjoiMSJ9.yN8-2aycoSmPBkqCMkmmuw9h4iztoJHV1An5-wXCpONUXwywjgVuIap96OqADIVT2RubsZ115H_NWCY8MPXjBafYzB9TVUzwwPofR6N88gI58Em3dvoT-GCI46HTo5cJt5M1XputcQYE2biKKmQlhAICC7MWbcFiPxlzxTLHm7sa8fQUDTm413GYPLqUTmEZFnIqg3uEcHusjPGoli-1DfCC9ENmpMMjknWEj8WRff0.7KC0BrPsT6eCqVX4nXqTjFdCHfqAMf8Z7SdeFaqpfL8&dib_tag=se&keywords=dance+pad+pc&qid=1737413280&sprefix=dance+pad+pc%2Caps%2C176&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1">
 * Link to Amazon product</a>
 * @author El Campus
 */
public class DancePad {
    private Joystick gamepad;

    /** 
     * @param gamepadPort The port that the controller is on
     */
    public DancePad(int gamepadPort) {
        gamepad = new Joystick(gamepadPort);
    }

    // these functions return true if the given button is pressed
    public boolean getUp() { return gamepad.getRawButton(3); }
    public boolean getRight() { return gamepad.getRawButton(4); }
    public boolean getDown() { return gamepad.getRawButton(2); }
    public boolean getLeft() { return gamepad.getRawButton(1); }
    public boolean getX() { return gamepad.getRawButton(7); }
    public boolean getO() { return gamepad.getRawButton(8); }
    public boolean getTriangle() { return gamepad.getRawButton(5); }
    public boolean getSquare() { return gamepad.getRawButton(6); }
    public boolean getSelect() { return gamepad.getRawButton(9); }
    public boolean getStart() { return gamepad.getRawButton(10); }
    public boolean getCenter() { return gamepad.getRawAxis(4) > 0.5; } // the center circle is an axis for some reason

    //these functions return triggers that activate when the given button is pressed
    public Trigger onUp() { return new Trigger(() -> getUp()); }
    public Trigger onRight() { return new Trigger(() -> getRight()); }
    public Trigger onDown() { return new Trigger(() -> getDown()); }
    public Trigger onLeft() { return new Trigger(() -> getLeft()); }
    public Trigger onX() { return new Trigger(() -> getX()); }
    public Trigger onO() { return new Trigger(() -> getO()); }
    public Trigger onTri() { return new Trigger(() -> getTriangle()); }
    public Trigger onSqr() { return new Trigger(() -> getSquare()); }
    public Trigger onSelect() { return new Trigger(() -> getSelect()); }
    public Trigger onStart() { return new Trigger(() -> getStart()); }
    public Trigger onCenter() { return new Trigger(() -> getCenter()); }
}