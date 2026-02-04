package com.robocats.vision;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.robocats.vision.LimelightHelpers.PoseEstimate;
import com.robocats.vision.LimelightHelpers.RawFiducial;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class LimelightCamera implements AprilCamera {
    private PoseEstimate positionEstimation;
    private String cameraName;
    private DoubleSupplier robotAngle;
    private DoubleSupplier robotRate;

    /**
     * @param cameraName The broadcasting name of the camera to be used
     * @param robotAngle A supplier that gives the angle of the robot in degress
     * @param robotRate A supplier that gives the angular velocity of the robot in degrees per second (can be null)
     */
    public LimelightCamera(String cameraName, DoubleSupplier robotAngle, DoubleSupplier robotRate) {
        this.cameraName = cameraName;
        this.robotAngle = robotAngle;
        //If the robot rate isn't supplied, then return 0
        this.robotRate = robotRate == null ? () -> 0 : robotRate;
    }

    public void periodic() {
        this.positionEstimation = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);

        LimelightHelpers.SetRobotOrientation(cameraName, robotAngle.getAsDouble(), robotRate.getAsDouble(), 0, 0, 0, 0);
    }

    public Pose2d getRobotPose() {
        return positionEstimation.pose;
    }

    public double faceTag() {
        RawFiducial[] fiducials = LimelightHelpers.getRawFiducials(cameraName);

        RawFiducial closest = fiducials[0];
        for(RawFiducial f : fiducials) {
            if(Math.abs(f.txnc) > Math.abs(closest.txnc)) {
                continue;
            }
            closest = f;
        }

        return faceTagController.calculate(closest.txnc, 0);
    }

    public void screenshot() {
        LimelightHelpers.takeSnapshot(cameraName, cameraName + "_snapshot");
    }
}
