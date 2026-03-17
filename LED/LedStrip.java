package com.robocats.LED;

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
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
    //TODO
    Once we are done with writing this class, we should move it to the library.
    I moved the class to the main project so that it is easier to write code. 
    The implementation will be the same, just different imports

    LEDPatterns seem to be the easiet way to program complex patterns while simple or smaller patterns will be used with setLED
*/
public class LedStrip extends SubsystemBase {
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;
    private double globalBrightness = 50; // 0 to 100 as a percentage


    /**
     * @param port The pwm port that the LED Strip is connected to
     * @param length How many LEDs are on the strip
     */

    //------------------------base stuff--------------------------------------
    public LedStrip(int port, int length) {
        led = new AddressableLED(port);
        ledBuffer = new AddressableLEDBuffer(length);
        led.setLength(length);

        led.start();
    }

     public void applyActiveLEDPattern(LEDPattern newPattern) {



    }

    //@Override
    //public void periodic() {
    //    // Runs every 20ms to update the animation frames
    //    activePattern.applyTo(ledBuffer);
    //    led.setData(ledBuffer);
    //}
    
    //------------------------------------more stuff------------------------------------------

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

    public void setGlobalBrightness(double brightness) {
    globalBrightness = MathUtil.clamp(brightness, 0.0, 1.0);
    }

    public Color COLOR(int rOne, int gOne, int bOne){
        Color color = new Color(rOne, gOne, bOne);
        return color;
    }

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



    public void two_color_mask() {

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
        pattern.applyTo(ledBuffer);
        led.setData(ledBuffer);
    }

    //Not for use outside of here I think
    private LEDPattern applyBrightness(LEDPattern pattern) {

        LEDPattern pattern_with_brightness = pattern.atBrightness(Percent.of(globalBrightness));

        return pattern_with_brightness;


    }

    /** stops output to the led strip*/
    public void stop() { led.stop();}

    public void setAll(int r, int g, int b) {

        for (int index = 0; index < ledBuffer.getLength(); index++) {
            ledBuffer.setRGB(index, r, g, b);

        }
        led.setData(ledBuffer);
    }

    public LEDPattern reverse(LEDPattern base) {
        LEDPattern pattern = base.reversed();
        
        return applyBrightness(pattern);
    }

    public LEDPattern scroll(LEDPattern base, double percentage_of_strip) {
        LEDPattern pattern = base.scrollAtRelativeSpeed(Percent.per(Second).of(percentage_of_strip));
        return applyBrightness(pattern);
    }

    public LEDPattern progress_mask(LEDPattern base, DoubleSupplier percentage) {
    LEDPattern mask = LEDPattern.progressMaskLayer(percentage);
    LEDPattern pattern = base.mask(mask);
    return applyBrightness(pattern);
    }

    public void setRainbow() {}
    // todo find a better name for this
    // should make a line that moves up the led strip
    //ex:
    // _____====__________
    // _________====______
    // _____________====__
    // ...
//MUST BE PUT INTO A PERIODIC FUCTION OR SOMETHING IDK MAN 

//YEAH, I MADE IT ONE LINE AS A JOKE, BUT LEFT IT THAT WAY BECAUSE IT WAS FUNNIER, LMAO
//It used to be 4 lines, I saved 3 WHOLE lines that I WILL be WASTING somewhere else
// 5 lines now, saved 4 lines*
// 4 lines now, saved 3 lines*
    public LEDPattern make_gradient(Color... colors){LEDPattern pattern = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors); return applyBrightness(pattern);}

    //SYMMETRICAL:
    public LEDPattern make_blinking_gradient(double breathingTime, Color... colors){
   
        // Create an LED pattern that displays a red-to-blue gradient, breathing at a 2 second period (0.5 Hz)
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        LEDPattern pattern =  base.blink(Seconds.of(breathingTime));
        return applyBrightness(pattern);
    }

    //ASYMMETRICAL:
    public LEDPattern make_blinking_gradient(double ON, double OFF, Color... colors){
        // Create an LED pattern that displays a red-to-blue gradient, breathing at a 2 second period (0.5 Hz)
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        LEDPattern pattern =  base.blink(Seconds.of(ON), Seconds.of(OFF));

        return applyBrightness(pattern);


    }

    //FOR SYNCHONIZATION:
    public LEDPattern make_blinking_gradient(BooleanSupplier THE_TRUTH, Color... colors){
        // Create an LED pattern that displays a red-to-blue gradient, breathing at a 2 second period (0.5 Hz)
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        //CAN BE USED WITH SOMETHING LIKE: synchronizedBlink(RobotController::getRSLState) to sync with the RSL
        LEDPattern pattern =  base.synchronizedBlink(THE_TRUTH);

        return applyBrightness(pattern);


    }

    //Pretty self explanetory
    public LEDPattern make_breathing_gradient(double breathingTime, Color... colors){

    // Create an LED pattern that displays a red-to-blue gradient, breathing at a 2 second period (0.5 Hz)
        LEDPattern base = LEDPattern.gradient(LEDPattern.GradientType.kDiscontinuous, colors);
        LEDPattern pattern =  base.breathe(Seconds.of(breathingTime));

        return applyBrightness(pattern);

    }

    public void setLoading() {}

    public LEDPattern sethalf(Color color_one, Color color_two){

        LEDPattern pattern = LEDPattern.steps(Map.of(0, color_one, 0.5, color_two));
        return applyBrightness(pattern);
    }
}
