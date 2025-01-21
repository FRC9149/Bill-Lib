package frc.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** <p>A class that allows for input with a DDR controller</p>
 * <a href="https://www.amazon.com/YtotY-Dancing-Controller-Fitness-Building/dp/B0D7JLQ5F3/ref=sr_1_1_sspa?crid=15CNVFJ4696VF&dib=eyJ2IjoiMSJ9.yN8-2aycoSmPBkqCMkmmuw9h4iztoJHV1An5-wXCpONUXwywjgVuIap96OqADIVT2RubsZ115H_NWCY8MPXjBafYzB9TVUzwwPofR6N88gI58Em3dvoT-GCI46HTo5cJt5M1XputcQYE2biKKmQlhAICC7MWbcFiPxlzxTLHm7sa8fQUDTm413GYPLqUTmEZFnIqg3uEcHusjPGoli-1DfCC9ENmpMMjknWEj8WRff0.7KC0BrPsT6eCqVX4nXqTjFdCHfqAMf8Z7SdeFaqpfL8&dib_tag=se&keywords=dance+pad+pc&qid=1737413280&sprefix=dance+pad+pc%2Caps%2C176&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1">
 * Link to Amazon product</a>
 * @author El Campus - grad 2026
 * @since 2025-01-20
 */
public class DDR implements Controller {
    private Joystick gamepad;
    /** <p>All the calculations for the DPad</p>
     *  Created so that the function overrides are cleaner.
     */
    class DPadVals {
        private double x, y;
        public DPadVals() { x = 0; y = 0; }
        public void update() {
            x = 0;
            y = 0;
            if (raw_getCenter()) return; // if I had this the other way around, you would have to use your hands for the 45 degs

            //rotated 45 deg essentially
            x += raw_getTri() ? -1.0 : 0.0;
            x += raw_getO() ? 1.0 : 0.0;
            y += raw_getSqr() ? -1.0 : 0.0;
            y += raw_getX() ? 1.0 : 0.0;
        }
        /** @return The angle value of a normal POV/DPad. */
        public int get() {
            // (Possible outcomes for each state [this line was wrote after codeium])
            // pos Y -> 0, 45, 315 //wtf Codeium, I typed in this 1 line and it came up with the rest of this. how tf
            // neg Y -> 180, 225, 135
            // pos X -> 90, 45, 135
            // neg X -> 270, 225, 315 // these could also be wrong I didn't check them
            update();
            if ( (x == 0 && y == 0) || raw_getCenter() ) return -1; // it didn't get this part and... //update now outputs {0,0} so the || is redundant (oh well)
            int results = (int) Math.toDegrees(Math.atan2(x, y)); // it had x and y switched and it was a double but still wtf
            return results += results < 0 ? 360 : 0; // Math.atan2 uses -90 instead of 270 so this fixes it
        }
    }
    DPadVals DPad = new DPadVals();

    /** 
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
    public boolean raw_getCenter() { return gamepad.getRawAxis(4) > 0.5; } // the center circle is an axis for some reason



    /** @return A trigger that runs when you press the ▲ and center Circle */
    public Trigger onA() { return new Trigger(() -> raw_getTri() && raw_getCenter()); }
    /** @return A trigger that runs when you press the ■ and center Circle */
    public Trigger onB() { return new Trigger(() -> raw_getSqr() && raw_getCenter()); }
    /** @return A trigger that runs when you press the X and center Circle */
    public Trigger onX() { return new Trigger(() -> raw_getX() && raw_getCenter()); }
    /** @return A trigger that runs when you press the O and center Circle */
    public Trigger onY() { return new Trigger(() -> raw_getO() && raw_getCenter()); }

    /** @return A trigger that runs when you press Select and center Circle */
    public Trigger onLeftBumper() { return new Trigger(() -> raw_getSelect() && raw_getCenter()); }
    /** @return A trigger that runs when you press Start and center Circle */
    public Trigger onRightBumper() { return new Trigger(() -> raw_getStart() && raw_getCenter()); }

    /** @return A trigger that runs when you press Select */
    public Trigger onBack() { return new Trigger(() -> raw_getSelect() && !raw_getCenter()); }
    /** @return A trigger that runs when you press Start */
    public Trigger onStart() { return new Trigger(() -> raw_getStart() && !raw_getCenter()); }

    /** Same as {@code onX();} 
     * @deprecated use onX()
    */
    public Trigger onLeftStickIn() { return onX(); }
    /** Same as {@code onY();} 
     * @deprecated use onY()
    */
    public Trigger onRightStickIn() { return onY(); }

    /** Same as {@code onLeftBumper();} 
     * @deprecated use onLeftBumper() */
    public Trigger onLeftTrigger(double threshold) { return onLeftBumper(); }
    /** Same as {@code onRightBumper();} 
     * @deprecated use onRightBumper()
    */
    public Trigger onRightTrigger(double threshold) { return onRightBumper(); }

    /** @return 1.0 if X is pressed, 0.0 if not */
    public double getLeftTrigger() { return onLeftTrigger(0).getAsBoolean() ? 1.0 : 0.0; }
    /** @return 1.0 if O is pressed, 0.0 if not */
    public double getRightTrigger() { return onRightTrigger(0).getAsBoolean() ? 1.0 : 0.0; }

    /** <p>Uses left and right arrows as the axes.</p>
     * doesn't use them if the center button is pressed
     */
    public double getLeftX() {
        if (raw_getCenter()) return 0.0; // if the center button is pressed, return nothing (I'm tired ok)
        double result = raw_getLeft() ? -1.0 : 0.0;
        result += raw_getRight() ? 1.0 : 0.0;
        return raw_getCenter() ? 0.0 : result;
    }
    /** <p>Uses up and down arrows as the axes.</p>
     * doesn't use them if the center button is pressed
     */
    public double getLeftY() {
        if (raw_getCenter()) return 0.0;
        double result = raw_getUp() ? 1.0 : 0.0;
        result += raw_getDown() ? -1.0 : 0.0;
        return raw_getCenter() ? 0.0 : result;
    }

    /** <p>Uses up and down arrows as the axes.<\p>
     * Only works if the center is being pressed
     */
    public double getRightX() {
        if (!raw_getCenter()) return 0.0;
        double result = raw_getRight() ? 1.0 : 0.0;
        result += raw_getLeft() ? -1.0 : 0.0;
        return !raw_getCenter() ? 0.0 : result;
    }
    /** <p>Uses left and right arrows as the axes.</p>
     * Only works if the center is being pressed
     */
    public double getRightY() {
        if (!raw_getCenter()) return 0.0; 
        double result = raw_getUp() ? 1.0 : 0.0;
        result += raw_getDown() ? -1.0 : 0.0;
        return !raw_getCenter() ? 0.0 : result;
    }

    /** @return A trigger that runs when you press X and  */
    public Trigger onDPadUp() { return new Trigger(() -> DPad.get() == 0); }
    /** @return A trigger that runs when you press X and O */
    public Trigger onDPadUpRight() { return new Trigger(()-> DPad.get() == 45); }
    /** @return A trigger that runs when you press O */
    public Trigger onDPadRight() { return new Trigger(()-> DPad.get() == 90); }
    /** @return A trigger that runs when you press O and ■ */
    public Trigger onDPadDownRight() { return new Trigger(()-> DPad.get() == 135); }
    /** @return A trigger that runs when you press ■ */
    public Trigger onDPadDown() { return new Trigger(()-> DPad.get() == 180); }
    /** @return A trigger that runs when you press ▲ and ■ */
    public Trigger onDPadDownLeft() { return new Trigger(() -> DPad.get() == 225); }
    /** @return A trigger that runs when you press ▲ */
    public Trigger onDPadLeft() { return new Trigger(() -> DPad.get() == 270); }
    /** @return A trigger that runs when you press ▲ and X */
    public Trigger onDPadUpLeft() { return new Trigger(() -> DPad.get() == 315); }
    /** @return A trigger that runs when you press none of the DPad keys (or when you're pressing the center) */
    public Trigger onDPadNull() { return new Trigger(() -> DPad.get() == -1); }
    public int getDpadAngle() { return DPad.get(); }
}