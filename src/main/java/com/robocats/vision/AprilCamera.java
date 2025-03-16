package com.robocats.vision;

import java.util.Arrays;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;
import org.photonvision.estimation.VisionEstimation;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class AprilCamera extends Camera {
  AprilTagFieldLayout tagLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeAndyMark);
  PhotonPoseEstimator estimator = new PhotonPoseEstimator(tagLayout, PhotonPoseEstimator.PoseStrategy.AVERAGE_BEST_TARGETS, super.transform );

  public AprilCamera(String cameraBroadcastingName, Transform3d cameraTransform) {
    super(cameraBroadcastingName, cameraTransform);
  }

  /**
   * @param id The id of the tag you want to align to (-1 for best target)
   * @param pid The pid controller to control the movement. leave null for default
   * @return The directional speed that you should turn to get to the target
   */
  public double alignWithTag(int id, PIDController pid) {
    super.update();
    double yaw = super.getYaw();
    // System.out.println(yaw);

    if (pid == null) {
      pid = new PIDController(0.039, 0.059, 0);
    }
    return -pid.calculate(yaw, 0);
  }

  public double tagHeight() {
    if (tagLayout == null) { return -1; }
    return tagLayout.getTagPose(3).get().getTranslation().getZ();
  }

  public Pose3d getRobotPose() {
    super.update();
    var result = estimator.update(super.result);
    return result.isPresent() ? result.get().estimatedPose : null;
  }
  public Pose3d getTargetPose() {
    return tagLayout.getTagPose(3).get();
  }

  public double getDistance() {
    Pose3d robotPose = getRobotPose();
    if (robotPose == null) { return -1; }
    return PhotonUtils.getDistanceToPose(robotPose.toPose2d(), getTargetPose().toPose2d());
  }

  /**
   * Used to make sure that you are looking for a certain tag
   * 
   * @param id The id of the April tag you're looking for
   * @return The Tracked Target of a april tag with a certain Id; null if these
   *         are not the tags you are looking for
   *         {@link https://www.youtube.com/watch?v=532j-186xEQ}
   */
  public PhotonTrackedTarget getTargetWithId(int id) {
    if (!result.hasTargets()) {
      return null;
    }
    for (PhotonTrackedTarget target : result.getTargets()) {
      if (target.fiducialId == id) {
        return target;
      }
    }
    return null;
  }
}