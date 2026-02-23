package com.robocats.swerve.gyroscope;

import edu.wpi.first.math.geometry.Rotation2d;

public interface Gyro {
    // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-odometry.html
    // 0 degrees / radians represents the robot angle when the robot is facing directly toward your opponent’s alliance station. 
    // As your robot turns to the left, your gyroscope angle should increase. 
    // See Coordinate System for more information about the coordinate system.
    // https://docs.wpilib.org/en/stable/docs/software/basic-programming/coordinate-system.html

    /**
     * @return The current angle of the robot (radians 0->2pi)
     */
    public double getRadians();
    /**
     * @return The angular velocity of the robot (TODO add units)
     */
    public double getRate();
    /**
     * @return The rotation of the robot as a Rotation2d object
     */
    public Rotation2d getRotation2d();
    /**
     * Zeros the gyroscope so that the current direction is 0 radians
     */
    public void zero();
}
