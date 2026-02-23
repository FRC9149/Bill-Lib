package com.robocats.swerve;

/**
 * Record of which port the modules are connected to, their offset, and if they're reversed.
 * <p>Record of:</p>
 * <ul>
 *      <li>Drive motor CAN ports</li>
 *      <li>Turning motor CAN ports</li>
 *      <li>Absolute encoder ports</li>
 *      <li>Whether the drive encoders are reversed</li>
 * </ul>
 */
public record ModuleConfig(
    int frontLeftDrivePort,
    int backLeftDrivePort,
    int frontRightDrivePort,
    int backRightDrivePort,
    
    int frontLeftTurningPort,
    int backLeftTurningPort,
    int frontRightTurningPort,
    int backRightTurningPort,

    int frontLeftEncoderPort,
    int backLeftEncoderPort,
    int frontRightEncoderPort,
    int backRightEncoderPort,

    boolean frontLeftEncoderReversed,
    boolean backLeftEncoderReversed,
    boolean frontRightEncoderReversed,
    boolean backRightEncoderReversed
) {
    
}
