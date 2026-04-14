package com.robocats.LED;

import java.sql.Driver;
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
    */
    private Color rgbToColor(int r, int g, int b){
        Color color = new Color(r, g, b);
        return color;
    }

    public Color COLOR(int rOne, int gOne, int bOne){
        Color color = new Color(rOne, gOne, bOne);
        return color;
    }


    /**
     * Sets the leds that have in index equal to a number in the fibonacci sequence to a certain color
     */
     public void LED_fibonacci_sequence(int r, int g, int b) {

        int i;
        for (i = 0; i <= ledBuffer.getLength(); i++) {
            int the_index;
            int a = 0;
            int c = 1;
            if (i == 0) {
                the_index = 0;
            }
            for (int n = 2; n <= i; n++) {
                int next = a + c;
                a = c;
                c = next;
            }
            the_index = c;
            if (the_index <= ledBuffer.getLength()){
            setLed(the_index, r, g, b);
        }

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

    public LEDPattern make_gradient(Color... colors){LEDPattern pattern = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors); return pattern;}


    public LEDPattern reverse(LEDPattern base) {
        LEDPattern pattern = base.reversed();
        
        return applyBrightness(pattern);
     }

     public LEDPattern scroll(LEDPattern base, double percentage_of_strip) {
        LEDPattern pattern = base.scrollAtRelativeSpeed(Percent.per(Second).of(percentage_of_strip));
        return applyBrightness(pattern);
    }

    public LEDPattern progressMask(LEDPattern base, DoubleSupplier percentage) {
        LEDPattern mask = LEDPattern.progressMaskLayer(percentage);
        return base.mask(mask);
    }

    public LEDPattern blinking(double onTime, double offTime, LEDPattern base) {
        return base.blink(Seconds.of(onTime), Seconds.of(offTime));
    }

    public LEDPattern blinking( double blinkingTime, LEDPattern base) {
        return base.blink(Seconds.of(blinkingTime));
    }
    /**
     * @param signal When this supplier returns true, the gradient will be on, and off when false
     * @param colors a set of colors that will be used in the gradient
     * @return a pattern that will blink the given gradient based on the signal
     */
    public LEDPattern blinking(BooleanSupplier signal, LEDPattern base){
        return base.synchronizedBlink(signal);
    }

    public LEDPattern breathing(double breathingTime, LEDPattern base) {
        return base.breathe(Seconds.of(breathingTime));
    }

    public LEDPattern make_blinking_gradient(double breathingTime, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.blink(Seconds.of(breathingTime));
    }
    /**
     * @param signal When this supplier returns true, the gradient will be on, and off when false
     * @param colors a set of colors that will be used in the gradient
     * @return a pattern that will blink the given gradient based on the signal
     */
    public LEDPattern make_blinking_gradient(BooleanSupplier signal, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.synchronizedBlink(signal);
    }


    public LEDPattern make_breathing_gradient(double breathingTime, Color... colors){
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        return base.breathe(Seconds.of(breathingTime));
    }

    public LEDPattern sethalf(Color color_one, Color color_two){
        return LEDPattern.steps(Map.of(0, color_one, 0.5, color_two));
    }

    public void setAllianeColor(Color defaultColor) {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            switch(alliance.get()) {
                case Red:
                    setAll(255, 0, 0);
                    break;
                case Blue:
                    setAll(0, 0, 255);
                    break;
                default:
                    setAll((int)(defaultColor.red * 255), (int)(defaultColor.green * 255), (int)(defaultColor.blue * 255));
            }
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


    public void Prettycolors() {
        LEDPattern pattern = blinking(3.78654321, scroll(make_breathing_gradient(9.0, COLOR(127,255,0, 75, 83, 32)), 11.786));
     applyLEDPattern(pattern);
    }
    public void Flash() {
        LEDPattern pattern = make_blinking_gradient(.1, COLOR(255,255,255));
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




//==========================SECRET NO NO LOOKY AREA=====================================================
public Color[] COLOR(int rOne, int gOne, int bOne, int rTwo, int gTwo, int bTwo) {
        Color color_one = new Color(rOne, gOne, bOne);
        Color color_two = new Color(rTwo, gTwo, bTwo);
        Color[] color_array = {color_one, color_two};
        return color_array;
    }

    public Color[] COLOR(int rOne, int gOne, int bOne, int rTwo, int gTwo, int bTwo, int rThree, int gThree, int bThree) {
        Color color_one = new Color(rOne, gOne, bOne);
        Color color_two = new Color(rTwo, gTwo, bTwo);
        Color color_three = new Color(rThree, gThree, bThree);
        Color[] color_array = {color_one, color_two, color_three};
        return color_array;
    }

    public Color[] COLOR(int rOne, int gOne, int bOne, int rTwo, int gTwo, int bTwo, int rThree, int gThree, int bThree, int rFour, int gFour, int bFour) {
        Color color_one = new Color(rOne, gOne, bOne);
        Color color_two = new Color(rTwo, gTwo, bTwo);
        Color color_three = new Color(rThree, gThree, bThree);
        Color color_four = new Color(rFour, gFour, bFour);
        Color[] color_array = {color_one, color_two, color_three, color_four};
        return color_array;
    }

        public Color[] COLOR(int rOne, int gOne, int bOne, int rTwo, int gTwo, int bTwo, int rThree, int gThree, int bThree, int rFour, int gFour, int bFour, int rFive, int gFive, int bFive) {
        Color color_one = new Color(rOne, gOne, bOne);
        Color color_two = new Color(rTwo, gTwo, bTwo);
        Color color_three = new Color(rThree, gThree, bThree);
        Color color_four = new Color(rFour, gFour, bFour);
        Color color_five = new Color(rFive, gFive, bFive);
        Color[] color_array = {color_one, color_two, color_three, color_four, color_five};
        return color_array;
    }

}