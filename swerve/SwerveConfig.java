package com.robocats.swerve;

import com.robocats.swerve.gyroscope.Gyro;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

public record SwerveConfig(
    double maxSpeedMetersPerSecond,
    double maxAngularVelocityRadiansPerSecond,
    double wheelDiameterMeters,
    double drivePeriod,
    SwerveDriveKinematics driveKinematics,
    ModuleConfig moduleConfig,
    Gyro gyroscope,
    boolean isFieldSymmetric
) {}
