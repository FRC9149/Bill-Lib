package com.robocats.swerve;

import com.robocats.swerve.gyroscope.Gyro;
import com.robocats.swerve.gyroscope.AhrsGyro;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.TimedRobot;

public record SwerveConfig(
    double maxSpeedMetersPerSecond,
    double maxAngularVelocityRadiansPerSecond,
    double wheelDiameterMeters,
    double drivePeriod,
    SwerveDriveKinematics driveKinematics,
    ModuleConfig moduleConfig,
    Gyro gyroscope,
    boolean isFieldSymmetric
) {
    public static SwerveConfig generic(ModuleConfig modConfig) {
        return new SwerveConfig(4, 3*Math.PI, .1016, TimedRobot.kDefaultPeriod,
            new SwerveDriveKinematics(
                new Translation2d(-0.629 / 2, -0.629 / 2),
                new Translation2d(0.629 / 2, -0.629 / 2),
                new Translation2d(-0.629 / 2, 0.629 / 2),
                new Translation2d(0.629 / 2, 0.629 / 2)), 
            modConfig,
            new AhrsGyro(NavXComType.kMXP_SPI, Math.PI/2, false),
            false
        );
    }
}
