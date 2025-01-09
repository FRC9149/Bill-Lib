package frc.robocats;

import java.util.List;
import java.util.function.Function;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.controller.PIDController;

/**Class for processing images and finding targets
 * uses PhotonVision
 * @author El Campus - 2026
 * @since 2024-09-29
 */ 
public class Vision {
  PhotonCamera camera;
  double cameraHeight, cameraPitch, maxSpeed, maxAngularSpeed;
  PIDController pid;
  AprilTagFieldLayout aprilTags;
  List<PhotonPipelineResult> results;
  /**
   * @param cameraBroadcastingName the broadcasting name that the coprossesor is using
   * @param camreaHeight_Meters how high the camera is off the ground
   * @param cameraPitch_Radians The Pitch that the camera is at on the robot
   * @param turningPid the pid controller that should be applied to the rotation functions
   * @param tagLayout The Apriltag field layout for the current game
   */
  public Vision(String cameraBroadcastingName, double camreaHeight_Meters, double cameraPitch_Radians, PIDController turningPid) {
    //aprilTags = AprilTagFields.k2025ReefScape;
    cameraHeight = camreaHeight_Meters;
    cameraPitch = cameraPitch_Radians;
    pid = turningPid;
    camera = new PhotonCamera(cameraBroadcastingName);
  }

  public double getPitch() {
    return forResults( (result) -> {return result.getBestTarget().getPitch();} );
  }
  public double getArea() {
    return forResults( (result) -> {return result.getBestTarget().getArea();} );
  }
  public double getSkew() {
    return forResults( (result) -> {return result.getBestTarget().getSkew(); } );
  }
  public double getYaw() {
    return forResults( (result) -> {return result.getBestTarget().getYaw();} );
  }

  public boolean update() {
    results = camera.getAllUnreadResults();
    return !results.isEmpty();
  }
  public PhotonTrackedTarget getBestResult() {
    return forResults( (result) -> {return result.getBestTarget();} );
  }
  public <T> T forResults(Function<PhotonPipelineResult, T> func) {
    if (!update()) { return null; }
    for (PhotonPipelineResult result : results) {
      if (!result.hasTargets()) {continue;}
      return func.apply(result);
    }
    return null;
  }

  /**@returns the id of the best april tag. -1 if there is none */
  public int getAprilTagId() {
    if (!update()) { return -1; }
    return getBestResult().getFiducialId();
  }

  /**takes a screenshot. can be found inside the Photon config/settings and exporting. */
  public void screenshot() { camera.takeInputSnapshot(); }
}