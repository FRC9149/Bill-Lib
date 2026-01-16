package com.robocats.swerve;

import static edu.wpi.first.units.Units.Meter;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.robotConfig.PIDConstants;
import com.pathplanner.lib.robotConfig.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
    // Robot swerve modules
    private final PIDController turnController = new PIDController(0.5, 0.0, 0.0);
    private RobotConfig robotConfig;
    public final SwerveConfig swerveConfig;

    private SwerveModule frontLeft;
    private SwerveModule backLeft;
    private SwerveModule frontRight;
    private SwerveModule backRight;

    // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-odometry.html
    SwerveDriveOdometry odometry;
    

    /** Creates a 
     * @param robotConfig the SwerveConfig record that holds information such as dimensions, max speed, and gyroscope
     */
    public SwerveSubsystem(SwerveConfig robotConfig, PIDController pidController) {
        swerveConfig = robotConfig;
        initalizeSwerveModules(pidController);
        turnController.enableContinuousInput(0, 2 * Math.PI);

        try {
            //ngl, no idea what this does
            //AI says it loads a robotConfig from something like smartdashboard or another interface 
            //This sounds right, but we don't have anything like that. so...
          this.robotConfig = RobotConfig.fromGUISettings();
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    private void initalizeSwerveModules(PIDController pidController) {
        frontLeft = new SwerveModule(
        "fl",
            swerveConfig.moduleConfig().frontLeftDrivePort(),
            swerveConfig.moduleConfig().frontLeftTurningPort(),
            swerveConfig.moduleConfig().frontLeftEncoderPort(),
            swerveConfig.moduleConfig().frontLeftEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.moduleConfig().frontLeftEncoderReversed()
        );

        backLeft = new SwerveModule(
        "bl",
            swerveConfig.moduleConfig().backLeftDrivePort(),
            swerveConfig.moduleConfig().backLeftTurningPort(),
            swerveConfig.moduleConfig().backLeftEncoderPort(),
            swerveConfig.moduleConfig().backLeftEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.moduleConfig().backLeftEncoderReversed()
        );

        frontRight = new SwerveModule(
        "fr",
            swerveConfig.moduleConfig().frontRightDrivePort(),
            swerveConfig.moduleConfig().frontRightTurningPort(),
            swerveConfig.moduleConfig().frontRightEncoderPort(),
            swerveConfig.moduleConfig().frontRightEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.moduleConfig().frontRightEncoderReversed()
        );

        backRight = new SwerveModule(
        "br",
            swerveConfig.moduleConfig().backRightDrivePort(),
            swerveConfig.moduleConfig().backRightTurningPort(),
            swerveConfig.moduleConfig().backRightEncoderPort(),
            swerveConfig.moduleConfig().backRightEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.moduleConfig().backRightEncoderReversed()
        );

        odometry = new SwerveDriveOdometry(
            swerveConfig.driveKinematics(),
            getRotation(),
            new SwerveModulePosition[] {
                    frontLeft.getPosition(),
                    frontRight.getPosition(),
                    backLeft.getPosition(),
                    backRight.getPosition()
                });
    }
/*  Don't ask...
His name is Jeremy...




                    Silent streams                        silent streams
                and whispering winds                   and whispering winds
              shadows dance beneath the moon       shadows dance beneath the moon
             echoes of the nightingale's song     echoes of the nightingale's song
              stars align in cosmic harmony        stars align in cosmic harmony
                   dreams awaken softly                dreams awaken softly



                   
              beneath the twilight                          beneath the twilight
       mysteries unfold in the night's embrace        mysteries unfold in the night's embrace
              secrets whispered among the trees secrets whispered among the trees
                    the forest breathes deeply the forest breathes deeply
                            ------------------------------------
                                A poem crafted from Chat GPT
                                    visuals by humans




(P.S. He says hello.)
*/
    @Override
    public void periodic() {
        // Update the odometry in the periodic block
        SmartDashboard.putNumber("gyro", getHeading());
        frontLeft.periodic();
        frontRight.periodic();
        backLeft.periodic();
        backRight.periodic();
        
        odometry.update(
                getRotation(),
                new SwerveModulePosition[] {
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition()
                });
    }

    /** 
     *
     * @return the currently-estimated pose of the robot.
     */
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public ChassisSpeeds getChassisSpeeds() {
        return swerveConfig.driveKinematics().toChassisSpeeds(new SwerveModuleState[] {
            frontLeft.getState(),
            frontRight.getState(),
            backLeft.getState(),
            backRight.getState()
        });
    }

    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(
                getRotation(),
                new SwerveModulePosition[] {
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition()
                },
                pose);
    }

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed        Speed of the robot in the x direction (forward) (between
     *                      -1 and 1).
     * @param ySpeed        Speed of the robot in the y direction (sideways).
     *                      (between -1 and 1)
     * @param rot           Angular rate of the robot. (between -1 and 1)
     * @param fieldRelative Whether the provided x and y speeds are relative to
     *                      the field.
     */
    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
        double magnitude = Math.sqrt(xSpeed*xSpeed + ySpeed*ySpeed);
        if (Math.abs(xSpeed) > 1 || Math.abs(ySpeed) > 1) {
            xSpeed /= magnitude;
            ySpeed /= magnitude;
        }
        rot = Math.min(rot, 1);
        rot = Math.max(rot, -1);

        // apply the max speeds
        xSpeed *= swerveConfig.maxSpeedMetersPerSecond();
        ySpeed *= swerveConfig.maxSpeedMetersPerSecond();
        rot *= swerveConfig.maxAngularVelocityRadiansPerSecond();
        
        double[] transformedX, transformedY, finalTransformed;
        if(fieldRelative) {
            double robotAngle = getHeading();
            transformedX = new double[]{Math.cos(robotAngle) * xSpeed, Math.sin(robotAngle) * xSpeed}; //rotation matrix
            transformedY = new double[]{-Math.sin(robotAngle)* ySpeed, Math.cos(robotAngle) * ySpeed};
            finalTransformed = new double[]{transformedX[0] + transformedY[0], transformedX[1] + transformedY[1]}; //combine into 1
        } else {
            finalTransformed = new double[]{xSpeed, ySpeed};
        }

        var swerveModuleStates = swerveConfig.driveKinematics().toSwerveModuleStates(
            ChassisSpeeds.discretize(
                new ChassisSpeeds(finalTransformed[0], finalTransformed[1], rot),
                swerveConfig.DrivePeriod()
            )
        );

        setModuleStates(swerveModuleStates);
    }

    public void drive(double xSpeed, double ySpeed, double xHeading, double yHeading) {
        double headingAngle = Math.atan2(yHeading, xHeading) ;  // in radians

        drive (
            xSpeed,
            ySpeed,
            xHeading == 0 && yHeading == 0 ? 0 : // so that when you stop pressing the right stick it'll stop spinning
                turnController.calculate(getHeading(), headingAngle),
            true
        );
    }

    public void drive(ChassisSpeeds speeds, boolean fieldRelative) {
        drive(speeds.vyMetersPerSecond, speeds.vxMetersPerSecond, speeds.omegaRadiansPerSecond, fieldRelative);
    }

    /**
     * @deprecated I believe this doens't work atm
     */
    public void driveTo(Pose2d pose) {
        Pose2d currentPose = getPose();
        // System.out.println(currentPose.getX());
        double x = pose.getMeasureX().minus(currentPose.getMeasureX()).in(Meter);
        double y = pose.getMeasureY().minus(currentPose.getMeasureY()).in(Meter);
        double r = pose.getRotation().minus(currentPose.getRotation()).getRadians();
        // creates new x,y,and rotation values that define the translation from the robot to the desired point
        double rotationX = Math.cos(r);
        double rotationY = Math.sin(r);
        double distance = Math.sqrt((x*x) + (y*y));
        drive(x/distance, y/distance, rotationX, rotationY);
        // drive(pose.getMeasureX().in(Meter), pose.getMeasureY().in(Meter), pose.getRotation().getRadians(), true);
    }

    /**
     * Sets the swerve ModuleStates.
     * @param desiredStates The desired SwerveModule states.
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(
            desiredStates, swerveConfig.maxSpeedMetersPerSecond());

        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStates[2]);
        backRight.setDesiredState(desiredStates[3]);
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        frontLeft.resetEncoders();
        backLeft.resetEncoders();
        frontRight.resetEncoders();
        backRight.resetEncoders();
    }

    public Rotation2d getRotation() {
        // if(swerveConfig == null) {Rotation2d.fromRadians(0);}
        return swerveConfig.gyroscope().getRotation2d();
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading in radians, from 0 to 2PI
     */
    public double getHeading() {
        return getRotation().getRadians();
    }

    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        return swerveConfig.gyroscope().getRate();
    }

    /**
   * Setup AutoBuilder for PathPlanner.
   */
  public void setupPathPlanner() {
    AutoBuilder.configure(
            this::getPose, // Robot pose supplier
            this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
            this::getChassisSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            (speeds, feedforwards) -> drive(speeds, false), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
            new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                    new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                    new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
            ),
            robotConfig, // The robot configuration
            () -> {
            //   Boolean supplier that controls when the path will be mirrored for the red alliance
            //   This will flip the path being followed to the red side of the field.
            //   THE ORIGIN WILL REMAIN ON THE BLUE SIDE

              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent() && !swerveConfig.isFieldSymmetric()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this // Reference to this subsystem to set requirements
    );
    swerveConfig.gyroscope().zero(); // zero so that the robot is facing forward and not sideways
  }
}
