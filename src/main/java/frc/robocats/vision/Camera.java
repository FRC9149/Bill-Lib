package frc.robocats.vision;

import java.util.List;
import java.util.function.Function;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;

public class Camera {
  private PhotonCamera camera;
  private double height, pitch;
  List<PhotonPipelineResult> results;

  /**
   * @param cameraBroadcastingName the broadcasting name that the coprossesor is using
   * @param camreaHeightMeters how high the camera is off the ground
   * @param cameraPitchRadians The Pitch that the camera is at on the robot
   */
  public Camera(String cameraBroadcastingName, double camreaHeightMeters, double cameraPitchRadians ) {
    camera = new PhotonCamera(cameraBroadcastingName);
    height = camreaHeightMeters;
    pitch = cameraPitchRadians;
  }

  /** based on {@link https://docs.limelightvision.io/docs/docs-limelight/tutorials/tutorial-estimating-distance} 
   *  @return The distance from the robot camera to the target
  */
  public double getDistanceMeters() {
    double a1 = getCameraPitch();
    double a2 = a1 - (180 - getPitch() - 90); 
    double h1 = getCameraHeight();
    double h2 = tagHeight; // TODO find tagHeight
    double distance = (h2 - h1) / Math.tan(a1 + a2);
    return distance;
  }

  /** @return The pitch of the current best target */
  public double getPitch() {
    return forResults( (result) -> {return result.getBestTarget().getPitch();} );
  }
  /** @return The Area that the current best target takes up */
  public double getArea() {
    return forResults( (result) -> {return result.getBestTarget().getArea();} );
  }
  /** @return The skew of the current best target */
  public double getSkew() {
    return forResults( (result) -> {return result.getBestTarget().getSkew(); } );
  }
  /** @return The yaw of the current best target */
  public double getYaw() {
    return forResults( (result) -> {return result.getBestTarget().getYaw();} );
  }
  /** @return The height that the camera is off the ground */
  public double getCameraHeight() {
    return height;
  }
  /** @return The pitch of the camera relative to the ground */
  public double getCameraPitch() {
    return pitch;
  }

  /** updates the camera with all the results
   * @return True if results is filled
   */
  public boolean update() {
    results = camera.getAllUnreadResults();
    return !results.isEmpty();
  }
  /** @return The current best result */
  public PhotonTrackedTarget getBestResult() {
    return forResults( (result) -> {return result.getBestTarget();} );
  }
  /** Takes in a function that requires a  */
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
