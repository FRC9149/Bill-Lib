package com.robocats.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;


//TODO : finish this class

// public class DDRController extends DDR {
    // /** 
    //  * @param gamepadPort The port that the controller is on
    //  */
    // public DDRController(int gamepadPort) {
        // super(gamepadPort);
    // }
// 
    //  /** <p>All the calculations for the DPad</p>
    //  *  Created so that the function overrides are cleaner.
    //  */
    // class DPadVals {
        // private double x = 0, y = 0;
        // public void update() {
            // x = 0;
            // y = 0;
            /*If the center button is pressed, don't use Dpad, use joystick*/
            // if (raw_getCenter()) return;
// 
         /*    rotated 45 deg essentially*/
            // x += getTriangle() ? -1.0 : 0.0;
            // x += getO() ? 1.0 : 0.0;
            // y += getSquare() ? -1.0 : 0.0;
            // y += getX() ? 1.0 : 0.0;
        // }
        // /** @return The angle value of a normal POV/DPad. */
        // public int get() {
          /*  (Possible outcomes for each state [this line was wrote after codeium])
            pos Y -> 0, 45, 315 //wtf Codeium, I typed in this 1 line and it came up with the rest of this. how tf
            neg Y -> 180, 225, 135
            pos X -> 90, 45, 135
            neg X -> 270, 225, 315 // these could also be wrong I didn't check them */
            // update();
            // if ( (x == 0 && y == 0) || getCenter() ) return -1; // it didn't get this part and... //update now outputs {0,0} so the || is redundant (oh well)
            // int results = (int) Math.toDegrees(Math.atan2(x, y)); // it had x and y switched and it was a double but still wtf
            // return results += results < 0 ? 360 : 0; // Math.atan2 uses -90 instead of 270 so this fixes it
        // }
    // }
    // DPadVals DPad = new DPadVals();
// 
// 
    // /** @return A trigger that runs when you press X */
    // public Trigger onDPadUp() { return new Trigger(() -> DPad.get() == 0); }
    // /** @return A trigger that runs when you press X and O */
    // public Trigger onDPadUpRight() { return new Trigger(()-> DPad.get() == 45); }
    // /** @return A trigger that runs when you press O */
    // public Trigger onDPadRight() { return new Trigger(()-> DPad.get() == 90); }
    // /** @return A trigger that runs when you press O and ■ */
    // public Trigger onDPadDownRight() { return new Trigger(()-> DPad.get() == 135); }
    // /** @return A trigger that runs when you press ■ */
    // public Trigger onDPadDown() { return new Trigger(()-> DPad.get() == 180); }
    // /** @return A trigger that runs when you press ▲ and ■ */
    // public Trigger onDPadDownLeft() { return new Trigger(() -> DPad.get() == 225); }
    // /** @return A trigger that runs when you press ▲ */
    // public Trigger onDPadLeft() { return new Trigger(() -> DPad.get() == 270); }
    // /** @return A trigger that runs when you press ▲ and X */
    // public Trigger onDPadUpLeft() { return new Trigger(() -> DPad.get() == 315); }
    // /** @return A trigger that runs when you press none of the DPad keys (or when you're pressing the center) */
    // public Trigger onDPadNull() { return new Trigger(() -> DPad.get() == -1); }
    // public int getDpadAngle() { return DPad.get(); }
// }
// 
// 