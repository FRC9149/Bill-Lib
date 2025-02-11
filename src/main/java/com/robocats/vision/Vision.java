package com.robocats.vision;

import edu.wpi.first.math.Pair;

/**Class for processing images and finding targets
 * uses PhotonVision
 * @author El Campus - 2026
 * @since 2024-09-29
 */ 
public class Vision {
  private Camera camera, camera2;
  /**
   * @param tagLayout The Apriltag field layout for the current game
   */
  public Vision(Camera cam1, Camera cam2) {
    //aprilTags = AprilTagFields.k2025ReefScape;
    camera = cam1;
    camera2 = cam2;
  }
  public Pair<Camera, Camera> getCameras() { return new Pair<Camera, Camera>(camera, camera2); }
}