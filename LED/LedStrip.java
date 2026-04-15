package com.robocats.LED;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import static edu.wpi.first.units.Units.Seconds;

import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Second;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class LedStrip extends SubsystemBase {
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;
    private double globalBrightness = .5; // 0 to 1 as a percentage


    /**
     * @param port The pwm port that the LED Strip is connected to
     * @param length How many LEDs are on the strip
     */
    public LedStrip(int port, int length) {
        led = new AddressableLED(port);
        ledBuffer = new AddressableLEDBuffer(length);
        led.setLength(length);

        led.start();
    }

    /** Sets a led to a certain color
     * @param index The index of the led you want to change
     * @param r The red value 0-255
     * @param g The Green value 0-255
     * @param b The Blue value 0-255
     */
    public void setLed(int index, int r, int g, int b) {
        ledBuffer.setRGB(index, r, g, b);
        led.setData(ledBuffer);
    }

    /**
     * Changes the brightness of all LedPatterns
     * @param brightness The percent brightness from 0 to 1, where 0 is off and 1 is full brightness
     */
    public void setGlobalBrightness(double brightness) {
        globalBrightness = MathUtil.clamp(brightness, 0.0, 1.0);
    }

    /**
     * Turns rgb values into a Color object that is usable by LedPatterns
     * @param rgb A list of rgb values in groups of 3 that represent 1 color in the Color array
     * @return An array of Color objects that is 1/3 the length of the input array
     */
    private Color[] rgbToColor(int... rgb) {
        List<Color> colors = new ArrayList<Color>();
        if(rgb.length % 3 != 0) {
            return new Color[0];
        }

        for (int i = 0; i < rgb.length; i += 3) {
            colors.add(new Color(rgb[i], rgb[i + 1], rgb[i + 2]));
        }
        return colors.toArray(new Color[colors.size()]);
    }


    /**
     * Sets the leds that have in index equal to a number in the fibonacci sequence to a certain color
     */
    public void fibonacciSequence(int r, int g, int b) {
        setAll(0, 0, 0);
        int a = 0;
        int c = 1;
        
        for(; c < ledBuffer.getLength(); ) {
            int next = a + c;
            a = c;
            c = next;

            setLed(c, r, g, b);
        }

    }


    /** Takes in a BiConsumer that gets applied to all the led pixels.
     * @param f The BiConsumer that takes in the index of the pixel and the rgb values of the pixel
     */
    public void foreach(BiConsumer<Integer, int[]> f) {
        //for all the led's in the class
        for (int i = 0; i < ledBuffer.getLength(); i++) {
            //consume the index and rgb value of the current led
            f.accept(
                i, 
                //new array of each color
                new int[]{
                    ledBuffer.getRed(i), 
                    ledBuffer.getGreen(i), 
                    ledBuffer.getBlue(i)
                }
            );
        }
    }

    /** Applys a pattern to the led buffer
     * @param pattern The led pattern you wish to apply
     */
    public void applyLEDPattern(LEDPattern pattern) {
        applyBrightness(pattern);
        pattern.applyTo(ledBuffer);
        led.setData(ledBuffer);
    }

    private LEDPattern applyBrightness(LEDPattern pattern) {
        return pattern.atBrightness(Percent.of(globalBrightness * 100));
    }

    /** stops output to the led strip*/
    public void stop() { led.stop();}

    /**
     * sets all the leds to a certain color
     */
    public void setAll(int r, int g, int b) {
        for (int index = 0; index < ledBuffer.getLength(); index++) {
            ledBuffer.setRGB(index, r, g, b);
        }
        led.setData(ledBuffer);
    }

    public LEDPattern progressMask(LEDPattern base, DoubleSupplier percentage) {
        LEDPattern mask = LEDPattern.progressMaskLayer(percentage);
        return base.mask(mask);
    }

    public LEDPattern gradient(Color... colors){ 
        return LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
    }

    public LEDPattern blinkingGradient(double breathingTime, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.blink(Seconds.of(breathingTime));
    }

    public LEDPattern blinking(LEDPattern base, double blinkingTime) {
        return base.blink(Seconds.of(blinkingTime));
    }

    public LEDPattern blinkingGradient(double onTime, double offTime, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.blink(Seconds.of(onTime), Seconds.of(offTime));
    }

    /**
     * @param signal When this supplier returns true, the gradient will be on, and off when false
     * @param colors a set of colors that will be used in the gradient
     * @return a pattern that will blink the given gradient based on the signal
     */
    public LEDPattern blinkingGradient(BooleanSupplier signal, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.synchronizedBlink(signal);
    }

    public LEDPattern breathingGradient(double breathingTime, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.breathe(Seconds.of(breathingTime));
    }

    public LEDPattern sethalf(Color color_one, Color color_two){
        return LEDPattern.steps(Map.of(0, color_one, 0.5, color_two));
    }

    public void setAllianeColor(Color defaultColor) {
        var alliance = DriverStation.getAlliance();
        if (!alliance.isPresent()) {
            setAll((int)(defaultColor.red * 255), (int)(defaultColor.green * 255), (int)(defaultColor.blue * 255));
            return;
        }
        switch(alliance.get()) {
            case Red:
                setAll(255, 0, 0);
                break;
            case Blue:
                setAll(0, 0, 255);
                break;
            default:
                setAll((int)(defaultColor.red * 255), (int)(defaultColor.green * 255), (int)(defaultColor.blue * 255));
                break;
        }
        
    }

    
//============================================================================================================
//=======================MACROS=========================================================================
//============================================================================================


/*
                           
                          .' `'.__
                         /      \ `'"-,
        .-''''--...__..-/ .     |      \
      .'               ; :'     '.  ʘ   |
     /                 | :.       \     =\
    ;                   \':.      /  ,-.__;.-;`
   /|     .              '--._   /-.7`._..-;`
  ; |       '                |`-'      \  =|
  |/\        .   -' /     /  ;         |  =/
  (( ;.       ,_  .:|     | /     /\   | =|
   ) / `\     | `""`;     / |    | /   / =/
     | ::|    |      \    \ \    \ `--' =/
    /  '/\    /       )    |/     `-...-`
   /    | |  `\    /-'    /;
   \  nn/ |    \   D    .'  \
    `""`   \  nnh  D_.-'L__nnh
            `"""`
*/


    public void prettyColors() {
        LEDPattern pattern = blinking(
            breathingGradient(9.0, rgbToColor(127,255,0, 75, 83, 31)).scrollAtRelativeSpeed(Percent.per(Second).of(11.786)), 
            3.78654321
        );
        applyLEDPattern(pattern);
    }
    public void flash() {
        LEDPattern pattern = blinkingGradient(.1, rgbToColor(255,255,255));
        applyLEDPattern(pattern);
    }
    public void solidNavy() {
        setAll(0,0,128);
    }
    public void solidVermilion() {
        setAll(227,66,52);
    }
    public void solidIvyGreen() {
        setAll(0,106,91);
    }

}
