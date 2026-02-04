package com.robocats.vision;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.struct.PhotonTrackedTargetSerde;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;

/** A interface for creating a camera that interprets April Tags.
 * @author El Campus
 * Times rewrote because I didn't think it through: 3
 */
public interface AprilCamera {
  final PIDController faceTagController = new PIDController(0.5, 0.01, 0.01);
  /**
   * @return The estimated position of the robot on the field.
   */
  public Pose2d getRobotPose();
  /**
   * @return The speed your robot should turn to face a tag. Left is negative
   */
  public double faceTag();
  /**
   * Saves and image of what the camera currently sees
   */
  public void screenshot();
  /**
  * A function that should be run every robot frame
  */
  public void periodic();
}




// public class Camera {
//   protected PhotonCamera camera;
//   protected Transform3d transform;
//   protected PhotonPipelineResult result;

//   /**
//    * @param cameraBroadcastingName The name of the camera (found in Photon client settings top right)
//    * @param camreaHeightMeters     how high the camera is off the ground
//    * @param cameraPitchRadians     The Pitch that the camera is at on the robot
//    */
//   public Camera(String cameraBroadcastingName, Transform3d cameraTransform) {
//     camera = new PhotonCamera(cameraBroadcastingName);
//     transform = cameraTransform;
//   }
  
//   /**
//    * @deprecated Doesn't work :(
//    * @return The distance from the robot camera to the target given that it's a tag; -1 if there is no tag
//    */
//   public double getDistanceMeters() {
//     if (!isResultUsable()) { return -1; }

//     double tagAngle = Units.degreesToRadians(getPitch());
//     double tagHeight = transform.getZ();

//     double distance = PhotonUtils.calculateDistanceToTargetMeters(
//      transform.getTranslation().getZ(),
//      tagHeight, 
//      transform.getRotation().getX(), 
//      tagAngle
//     );
//     return (distance);
//   }
  
//   /** @return The pitch of the current best target */
//   public double getPitch() {
//     return isResultUsable() ? result.getBestTarget().getPitch() : -1;
//   }
//   /** @return The Area that the current best target takes up */
//   public double getArea() {
//     return isResultUsable() ? result.getBestTarget().getArea() : -1;
//   }
//   /** @return The skew of the current best target */
//   public double getSkew() {
//     return isResultUsable() ? result.getBestTarget().getSkew() : -1;
//   }
//   /** @return The yaw of the current best target; -1 if there are no targets */
//   public double getYaw() {
//     return isResultUsable() ? result.getBestTarget().getYaw() : -1;
//   }

//   /**
//    * Updates the camera with all the results
//    * Should be called after each cycle (loop of higher function)
//    * Aka: Should be called before using data (but not while parsing data)
//    */
//   public void update() { 
//     // var results = camera.getAllUnreadResults().toArray(new PhotonPipelineResult[0]);
//     // result = results.length == 0 ? null : results[results.length-1];
//     result = camera.getLatestResult();
//   }

//   /**
//    * @return True if the camera can be used to get data
//    */
//   public boolean isResultUsable() {
//     // if the camera was updated
//     // and if the camera see's something
//     if(result == null || !result.hasTargets()) {
//       try{
//         System.out.println(!result.hasTargets() ? "\nCamera could not see any targets" : "");
//       } catch(Exception e) { //Can't access result if it's null, so it throws an error.
//         System.out.println("\nCamera does not have a result. Likely updated too many times");
//       } 
//       return false;
//     } else return true;
//   }

//   /** Takes a screenshot can be found at the bottom of the settings menu (10.91.49.11) */
//   public void screenshot() { camera.takeInputSnapshot(); System.out.println("You took a screenshot!"); }
// }

