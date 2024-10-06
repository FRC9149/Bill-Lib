package frc.robocats;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
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
  /**
   * @param cameraBroadcastingName the broadcasting name that the coprossesor is using
   * @param camreaHeight_Meters how high the camera is off the ground
   * @param cameraPitch_Radians The Pitch that the camera is at on the robot
   * @param turningPid the pid controller that should be applied to the rotation functions
   * @param tagLayout The Apriltag field layout for the current game
   */
  public Vision(String cameraBroadcastingName, double camreaHeight_Meters, double cameraPitch_Radians, PIDController turningPid, AprilTagFields tagLayout) {
    aprilTags = tagLayout.loadAprilTagLayoutField();
    cameraHeight = camreaHeight_Meters;
    cameraPitch = cameraPitch_Radians;
    pid = turningPid;
    camera = new PhotonCamera(cameraBroadcastingName);
  }

  /** Find how to turn to align with a certain apriltag.
   * Should be applied continuously
   * @param tagId the id of the April tag to turn to
   * @return the turning speed to align with the tag
   */
  public double turnToTag(int tagId) {
    PhotonPipelineResult result = camera.getLatestResult();
    //check if there are no targets (for readability sake)
    if(!result.hasTargets()) return Double.MAX_VALUE;

    //for each target, if the id is correct, start turning towards that one
    for(PhotonTrackedTarget target : result.getTargets()) {
      if(target.getFiducialId() == tagId) {
        return pid.calculate(-1.0 * target.getYaw() * maxAngularSpeed);
      }
    }
    return Double.MAX_VALUE;
  }

  public double turnToTarget() {
    PhotonPipelineResult result = camera.getLatestResult();
    if(result.hasTargets()) {
      return pid.calculate(-1 * result.getBestTarget().getYaw() * maxAngularSpeed);
    }
    return Double.MAX_VALUE;

  }


  /**@returns the id of the best april tag. -1 if there is none */
  public int getAprilData() {
    PhotonPipelineResult result = camera.getLatestResult();
    if(result.hasTargets()) return result.getBestTarget().getFiducialId();
    return -1;
  }

  /**takes a screenshot. can be found inside the Photon config/settings and exporting. */
  public void screenshot() { camera.takeInputSnapshot(); }
  public double getLatency() { return camera.getLatestResult().getLatencyMillis() / 1000; }
}