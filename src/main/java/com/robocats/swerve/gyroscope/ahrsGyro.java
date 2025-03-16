package com.robocats.swerve.gyroscope;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Rotation2d;

public class ahrsGyro implements Gyro {
    private AHRS gyro;
    private double offset;
    private boolean reversed;

    public ahrsGyro(NavXComType portType, double offsetRadians, boolean reversed) {
        gyro = new AHRS(portType);
        offset = offsetRadians;
        this.reversed = reversed;
    }

    public double getRadians() { return gyro.getRotation2d().getRadians() + offset; }
    public double getRate() { return gyro.getRate() * (reversed ? -1.0 : 1.0); }
    public Rotation2d getRotation2d() { return gyro.getRotation2d(); }
    public void zero() { gyro.resetDisplacement(); }
}
