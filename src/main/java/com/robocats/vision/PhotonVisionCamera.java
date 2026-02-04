package com.robocats.vision;

import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;

public class PhotonVisionCamera implements AprilCamera {

    private PhotonPoseEstimator estimator;
    private PhotonCamera camera;
    List<PhotonPipelineResult> pipelineResults;

    public PhotonVisionCamera(String cameraName, AprilTagFieldLayout fieldLayout, Transform3d cameraOffset) {
        camera = new PhotonCamera(cameraName);
        estimator = new PhotonPoseEstimator(fieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, cameraOffset);
    }

    public PhotonPipelineResult getBestResult() {
        //return the most recent result
        return pipelineResults.get(pipelineResults.size() - 1);
    }

    public Pose2d getRobotPose() {
        Optional<EstimatedRobotPose> pose = estimator.update(getBestResult());

        return pose.isEmpty() ? null : pose.get().estimatedPose.toPose2d();
    }

    public double faceTag() {
        return faceTagController.calculate(getBestResult().getBestTarget().yaw, 0);
    }

    public void screenshot() {
        camera.takeInputSnapshot();
    }

    public void periodic() {
        pipelineResults = camera.getAllUnreadResults();
    }
    
}
