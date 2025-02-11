package com.robocats.vision;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

/** A simplified Photon Camera that holds additonal use
 * @author El Campus - grad year 2026
 * Time rewrote because I didn't think it through: 2
 * @since 1/11/2025
 */
public class Camera {
  private PhotonCamera camera;
  private double height, pitch;
  PhotonPipelineResult result = new PhotonPipelineResult(); // empty initlaize so that we can still call hasTargets()

  /**
   * @param cameraBroadcastingName The name of the camera (found in Photon client settings top right)
   * @param camreaHeightMeters     how high the camera is off the ground
   * @param cameraPitchRadians     The Pitch that the camera is at on the robot
   */
  public Camera(String cameraBroadcastingName, double camreaHeightMeters, double cameraPitchRadians) {
    camera = new PhotonCamera(cameraBroadcastingName);
    height = camreaHeightMeters;
    pitch = cameraPitchRadians;
  }

// {{ getBest
  /**
   * based on
   * {@link https://docs.limelightvision.io/docs/docs-limelight/tutorials/tutorial-estimating-distance}
   * 
   * @return The distance from the robot camera to the target given that it's a tag; -1 if there is no tag
   */
  public double getDistanceMeters() {
    if (!result.hasTargets()) { return -1; }
    int tagId = result.getBestTarget().fiducialId;

    double tagAngle = getTaggedPitch(tagId);
    if (tagAngle == -1) { return -1; }

    return PhotonUtils.calculateDistanceToTargetMeters(
      // debug 1.651 meters tag Height
      getCameraHeight(), .4064 /* TODO find tagHeight */, getCameraPitch(), tagAngle
    );
  }
  /** @return The pitch of the current best target */
  public double getPitch() {
    if (!result.hasTargets()) { System.out.println("no targets" + camera.isConnected()); return -1; }
    return result.getBestTarget().getPitch();
  }
  /** @return The Area that the current best target takes up */
  public double getArea() {
    if (!result.hasTargets()) { return -1; }
    return result.getBestTarget().getArea();
  }
  /** @return The skew of the current best target */
  public double getSkew() {
    if (!result.hasTargets()) { return -1; }
    return result.getBestTarget().getSkew();
  }
  /** @return The yaw of the current best target; -1 if there are no targets */
  public double getYaw() {
    if (!result.hasTargets()) { return -1; }
    return result.getBestTarget().getYaw();
  }
  /** @returns The id of the best target. -1 if there is none */
  public int getAprilTagId() {
    if (!result.hasTargets()) { return -1; }
    return result.getBestTarget().fiducialId;
  }
// }} getBest
// {{ get
  /** @return The height that the camera is off the ground */
  public double getCameraHeight() { return height; }
  /** @return The pitch of the camera relative to the ground */
  public double getCameraPitch() { return pitch; }
  /**
   * Used to make sure that you are looking for a certain tag
   * 
   * @param id The id of the April tag you're looking for
   * @return The Tracked Target of a april tag with a certain Id; null if these are not the tags you are looking for
   *           {@link https://www.youtube.com/watch?v=532j-186xEQ}
   */
  public PhotonTrackedTarget getTargetWithId(int id) {
    if (!result.hasTargets()) { return null; }
    for (PhotonTrackedTarget target : result.getTargets()) {
      if (target.fiducialId == id) { return target; }
    }
    return null;
  }
// }} get
// {{ getTagged
  /**
   * @param id The id of the tag you're looking for
   * @return The pitch of the tag; -1 if the tag isn't visible
   */
  public double getTaggedPitch(int id) {
    if (!result.hasTargets()) { return -1; }
    PhotonTrackedTarget target = getTargetWithId(id);
    return target == null ? -1 : target.getPitch();
  }
  /**
   * @param id The id of the tag you're looking for
   * @return The Yaw of the tag; -1 if the tag isn't visible
   */
  public double getTaggedYaw(int id) {
    if (!result.hasTargets()) { return -1; }
    PhotonTrackedTarget target = getTargetWithId(id);
    return target == null ? -1 : target.getYaw();
  }
  /**
   * @param id The id of the tag you're looking for
   * @return The Skew of the tag; -1 if the tag isn't visible
   */
  public double getTaggedSkew(int id) {
    if (!result.hasTargets()) { return -1; }
    PhotonTrackedTarget target = getTargetWithId(id);
    return target == null ? -1 : target.getSkew();
  }
  /**
   * @param id The id of the tag you're looking for
   * @return The area of the tag; -1 if the tag isn't visible
   */
  public double getTaggedArea(int id) {
    if (!result.hasTargets()) { return -1; }
    PhotonTrackedTarget target = getTargetWithId(id);
    return target == null ? -1 : target.getArea();
  }
  /**
  * based on
  * {@link https://docs.limelightvision.io/docs/docs-limelight/tutorials/tutorial-estimating-distance}
  * 
  * @param tagId The id of the tag you're looking for
  * @return The distance from the robot camera to the corresponding tag; -1 if the tag is not present
  */
  public double getTaggedDistanceMeters(int tagId) {
    if (!result.hasTargets()) { return -1; }
  
    double tagAngle = getTaggedPitch(tagId);
    if (tagAngle == -1) { return -1; }
  
    return PhotonUtils.calculateDistanceToTargetMeters(
      getCameraHeight(), 1.6891 /* TODO find tagHeight */, getCameraPitch(), tagAngle
    );
  }
// }} getTagged

  /**
   * Updates the camera with all the results
   * Should be called after each cycle (loop of higher function)
   * Aka: Should be called before using data (but not while parsing data)
   */
  public void update() { result = camera.getLatestResult(); }

  /** Takes a screenshot can be found at the bottom of the settings menu (10.91.49.11) */
  public void screenshot() { camera.takeInputSnapshot(); System.out.println("You took a screenshot!");}

}
