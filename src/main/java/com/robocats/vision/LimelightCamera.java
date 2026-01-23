package com.robocats.vision;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.robocats.vision.LimelightHelpers.PoseEstimate;
import com.robocats.vision.LimelightHelpers.RawFiducial;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class LimelightCamera {
    private PoseEstimate positionEstimation;
    private String cameraName;
    private DoubleSupplier robotAngle;
    private DoubleSupplier robotRate;

    /**
     * @param cameraName The broadcasting name of the camera to be used
     * @param robotAngle A supplier that gives the angle of the robot in degress
     * @param robotRate A supplier that gives the angular velocity of the robot in degress per second
     */
    public LimelightCamera(String cameraName, DoubleSupplier robotAngle, DoubleSupplier robotRate) {
        this.cameraName = cameraName;
        this.robotAngle = robotAngle;
        this.robotRate = robotRate;
    }

    public void periodic() {
        this.positionEstimation = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);

        LimelightHelpers.SetRobotOrientation(cameraName, robotAngle.getAsDouble(), robotRate.getAsDouble(), 0, 0, 0, 0);
    }

    public Pose2d getRobotPose() {
        return positionEstimation.pose;
    }
}
