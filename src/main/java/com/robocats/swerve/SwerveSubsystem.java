package com.robocats.swerve;

import static edu.wpi.first.units.Units.Meter;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
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
    private final PIDController m_turnController = new PIDController(0.5, 0.0, 0.0);
    private RobotConfig config;
    public final SwerveConfig swerveConfig;

    private SwerveModule m_frontLeft;
    private SwerveModule m_backLeft;
    private SwerveModule m_frontRight;
    private SwerveModule m_backRight;

    // Odometry class for tracking robot pose
    SwerveDriveOdometry m_odometry;
    // PIDController test;
    

    /** Creates a 
     * @param config
     */
    public SwerveSubsystem(SwerveConfig config, PIDController test) {
        // this.test = test;
        swerveConfig = config;
        initalizeSwerveModules(test);
        m_turnController.enableContinuousInput(0, 2 * Math.PI);

        try {
          this.config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    private void initalizeSwerveModules(PIDController test) {
        m_frontLeft = new SwerveModule(
        "fl",
            swerveConfig.module_config().frontLeftDrivePort(),
            swerveConfig.module_config().frontLeftTurningPort(),
            swerveConfig.module_config().frontLeftEncoderPort(),
            swerveConfig.module_config().frontLeftEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.module_config().frontLeftEncoderReversed()
            ,test
        );

        m_backLeft = new SwerveModule(
        "bl",
            swerveConfig.module_config().backLeftDrivePort(),
            swerveConfig.module_config().backLeftTurningPort(),
            swerveConfig.module_config().backLeftEncoderPort(),
            swerveConfig.module_config().backLeftEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.module_config().backLeftEncoderReversed()
            ,test
        );

        m_frontRight = new SwerveModule(
        "fr",
            swerveConfig.module_config().frontRightDrivePort(),
            swerveConfig.module_config().frontRightTurningPort(),
            swerveConfig.module_config().frontRightEncoderPort(),
            swerveConfig.module_config().frontRightEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.module_config().frontRightEncoderReversed()
            ,test
        );

        m_backRight = new SwerveModule(
        "br",
            swerveConfig.module_config().backRightDrivePort(),
            swerveConfig.module_config().backRightTurningPort(),
            swerveConfig.module_config().backRightEncoderPort(),
            swerveConfig.module_config().backRightEncoderOffset(),
            swerveConfig.wheelDiameterMeters(),
            swerveConfig.maxAngularVelocityRadiansPerSecond(),
            swerveConfig.module_config().backRightEncoderReversed()
            ,test
        );

        m_odometry = new SwerveDriveOdometry(
            swerveConfig.drive_kinematics(),
            Rotation2d.fromRadians(0),
            new SwerveModulePosition[] {
                    m_frontLeft.getPosition(),
                    m_frontRight.getPosition(),
                    m_backLeft.getPosition(),
                    m_backRight.getPosition()
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
        m_frontLeft.periodic();
        m_frontRight.periodic();
        m_backLeft.periodic();
        m_backRight.periodic();
        
        m_odometry.update(
                getRotation(),
                new SwerveModulePosition[] {
                        m_frontLeft.getPosition(),
                        m_frontRight.getPosition(),
                        m_backLeft.getPosition(),
                        m_backRight.getPosition()
                });
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    public ChassisSpeeds getChassisSpeeds() {
        return swerveConfig.drive_kinematics().toChassisSpeeds(new SwerveModuleState[] {
            m_frontLeft.getState(),
            m_frontRight.getState(),
            m_backLeft.getState(),
            m_backRight.getState()
        });
    }

    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        m_odometry.resetPosition(
                getRotation(),
                new SwerveModulePosition[] {
                        m_frontLeft.getPosition(),
                        m_frontRight.getPosition(),
                        m_backLeft.getPosition(),
                        m_backRight.getPosition()
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

        var swerveModuleStates = swerveConfig.drive_kinematics().toSwerveModuleStates(
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
                m_turnController.calculate(getHeading(), headingAngle),
            true
        );
    }

    public void drive(ChassisSpeeds speeds, boolean fieldRelative) {
        drive(speeds.vyMetersPerSecond, speeds.vxMetersPerSecond, speeds.omegaRadiansPerSecond, fieldRelative);
    }

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

        m_frontLeft.setDesiredState(desiredStates[0]);
        m_frontRight.setDesiredState(desiredStates[1]);
        m_backLeft.setDesiredState(desiredStates[2]);
        m_backRight.setDesiredState(desiredStates[3]);
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        m_frontLeft.resetEncoders();
        m_backLeft.resetEncoders();
        m_frontRight.resetEncoders();
        m_backRight.resetEncoders();
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
            config, // The robot configuration
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
