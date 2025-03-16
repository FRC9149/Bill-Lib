package com.robocats.swerve.gyroscope;

import edu.wpi.first.math.geometry.Rotation2d;

public interface Gyro {
    public double getRadians();
    public double getRate();
    public Rotation2d getRotation2d();
    public void zero();
}
