package com.robocats.sensors;

import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class throughTheBoreEncoder {
    private final DutyCycleEncoder encoder;

    /** Creates a new relative throughTheBoreEncoder class
     * @param port the port that the encoder is plugged in to
     * @param inverted inverts the direction of the encoder if true
     */
    public throughTheBoreEncoder(int port, boolean inverted) {
        encoder = new DutyCycleEncoder(port);
        encoder.setInverted(inverted);
        encoder.setDutyCycleRange(1, 1024);
    }

    /**@return True if the encoder is connected */
    public boolean isConnected() { return encoder.isConnected(); }
    /** @return How many rotations the encoder has turned since the last reset. range: 0-1 */
    public double getRotations() { return isConnected() ? encoder.get()                : -1; }
    /** @return How many radians the encoder has turned since the last reset. range: 0-2pi */
    public double getRadians()   { return isConnected() ? getRotations() * 2 * Math.PI : -1; }
    /** @return How many degrees the encoder has turned since the last reset. range: 0-360 */
    public double getDegrees()   { return isConnected() ? getRotations() * 360         : -1; }
}
