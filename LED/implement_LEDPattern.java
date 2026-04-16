
package com.robocats.LED;

import java.util.ArrayList;
import java.util.List;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import static edu.wpi.first.units.Units.Seconds;

import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Second;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;





public class implement_LEDPattern {


  //I just like the uppercase. This already exists, but let's call this bonus DLC
    private static Color[] COLOR(int... rgb) {
        List<Color> colors = new ArrayList<Color>();
        if(rgb.length % 3 != 0) {
            return new Color[0];
        }

        for (int i = 0; i < rgb.length; i += 3) {
            colors.add(new Color(rgb[i], rgb[i + 1], rgb[i + 2]));
        }
        return colors.toArray(new Color[colors.size()]);
    }

  //======================implement options===================================================
    //A---
    //B---
    public static LEDPattern implement_blinking(double blinkingTime, LEDPattern base) {
        return base.blink(Seconds.of(blinkingTime));
    }

    public static LEDPattern implement_blinking(double onTime, double offTime, LEDPattern base) {
        return base.blink(Seconds.of(onTime), Seconds.of(offTime));
    }

    public static LEDPattern implement_blinking(BooleanSupplier signal, LEDPattern base){
        return base.synchronizedBlink(signal);
    }
  
    public static LEDPattern implement_breathing(double breathingTime, LEDPattern base) {
        return base.breathe(Seconds.of(breathingTime));
    }

    //C-O---
    //P---
    public static LEDPattern implement_progressMask(DoubleSupplier percentage, LEDPattern base) {
        LEDPattern mask = LEDPattern.progressMaskLayer(percentage);
        return base.mask(mask);
    }
  
    //R---
    public static LEDPattern implement_reverse(LEDPattern base) {
        return base.reversed();
     }

    //S---
     public static LEDPattern implement_scroll(double percentage_of_strip, LEDPattern base) {
        return base.scrollAtRelativeSpeed(Percent.per(Second).of(percentage_of_strip));
    }
    

}


