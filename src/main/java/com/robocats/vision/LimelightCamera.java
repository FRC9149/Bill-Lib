package com.robocats.vision;

import java.util.Optional;

import com.robocats.vision.LimelightHelpers.PoseEstimate;
import com.robocats.vision.LimelightHelpers.RawFiducial;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class LimelightCamera {
    // Basic targeting data+
    private double cameraHeight;
    private double cameraAngle;
    private AprilTagFieldLayout tagLayout;
    private PoseEstimate positionEstimation;

    public LimelightCamera(double cameraHeight, double cameraAngle, AprilTagFields tagLayout, String cameraName) {
        this.cameraHeight = cameraHeight;
        this.cameraAngle = cameraAngle;
        this.tagLayout = AprilTagFieldLayout.loadField(tagLayout);
        
        Optional<Alliance> ally = DriverStation.getAlliance();
        // if we are not on a team or on blue team, use the blue pose estimate
        // if we are on a team and not blue team, use red.
        this.positionEstimation = !ally.isPresent() || ally.get() == Alliance.Blue ?
            LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName) :
            LimelightHelpers.getBotPoseEstimate_wpiRed_MegaTag2(cameraName);

        // LimelightHelpers.SetRobotOrientation(cameraName, robotAngle, 0, 0, 0, 0, 0);
    }

    public Pose3d getRobotPose() {
        RawFiducial[] fiducials = LimelightHelpers.getRawFiducials("");
        for (RawFiducial fiducial : fiducials) {
            int id = fiducial.id; // Tag ID
            double txnc = fiducial.txnc; // X offset (no crosshair)
            // double tync = fiducial.tync; // Y offset (no crosshair)
            double ta = fiducial.ta; // Target area
            double distToCamera = fiducial.distToCamera; // Distance to camera
            double distToRobot = fiducial.distToRobot; // Distance to robot
            double ambiguity = fiducial.ambiguity; // Tag pose ambiguity
        }
        return null;
    }
}
