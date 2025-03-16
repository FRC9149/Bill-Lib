package com.robocats.swerve;

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

    double frontLeftEncoderOffset,
    double backLeftEncoderOffset,
    double frontRightEncoderOffset,
    double backRightEncoderOffset,

    boolean frontLeftEncoderReversed,
    boolean backLeftEncoderReversed,
    boolean frontRightEncoderReversed,
    boolean backRightEncoderReversed
) {
    
}
