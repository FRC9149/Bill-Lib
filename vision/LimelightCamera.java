package com.robocats.vision;

import static edu.wpi.first.units.Units.Meters;

import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.robocats.vision.LimelightHelpers.PoseEstimate;
import com.robocats.vision.LimelightHelpers.RawFiducial;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimelightCamera implements AprilCamera {
    private PoseEstimate positionEstimation;
    private String cameraName;
    private Supplier<Rotation2d> robotAngle;
    private DoubleSupplier robotRate;

    /**
     * @param cameraName The broadcasting name of the camera to be used
     * @param robotAngle A supplier that gives the angle of the robot
     * @param robotRate A supplier that gives the angular velocity of the robot in degrees per second (can be null)
     */
    public LimelightCamera(String cameraName, Supplier<Rotation2d> robotAngle, DoubleSupplier robotRate) {
        this.cameraName = cameraName;
        this.robotAngle = robotAngle;
        //If the robot rate isn't supplied, then return 0
        this.robotRate = robotRate == null ? () -> 0 : robotRate;
    }


    public void periodic() {
        LimelightHelpers.SetIMUMode(cameraName, DriverStation.isEnabled() ? 0 : 0);
        LimelightHelpers.SetRobotOrientation(cameraName, robotAngle.get().getDegrees(), robotRate.getAsDouble(), 0, 0, 0, 0);
        this.positionEstimation = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);

        if(getRobotPose() != null) {
            SmartDashboard.putNumber("PoseX", getRobotPose().getMeasureX().in(Meters));
            SmartDashboard.putNumber("PoseY", getRobotPose().getMeasureY().in(Meters));
        }
    }

    public Pose2d getRobotPose() {
        return positionEstimation == null ? null : positionEstimation.pose;
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
