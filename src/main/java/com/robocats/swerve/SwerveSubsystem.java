package com.robocats.swerve;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;
import com.robocats.vision.LimelightCamera;

import edu.wpi.first.math.MathUtil;
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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
    // Controls how fast the robot spins to match a certain heading
    private PIDController turnController = new PIDController(0.5, 0.0, 0.0);
    private RobotConfig robotConfig;
    public final SwerveConfig swerveConfig;
    private SwerveModule frontLeft;
    private SwerveModule backLeft;
    private SwerveModule frontRight;
    private SwerveModule backRight;
    private LimelightCamera camera;

    // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-odometry.html
    SwerveDriveOdometry odometry;

    /**
     * Creates a
     * 
     * @param config        The SwerveConfig record that holds information such as
     *                      dimensions, max speed, and gyroscope
     * @param pidController temporary test to congifure the pidControllers for the
     *                      turning motors
     * @param cameraForPose If you are using a camera to detect robot pose, put it
     *                      in here. If you don't have a camera, input null
     */
    public SwerveSubsystem(SwerveConfig config, PIDController test, LimelightCamera cameraForPose, boolean setupPathPlanner) {
        swerveConfig = config;
        camera = cameraForPose;
        turnController = test;
        initalizeSwerveModules();
        turnController.enableContinuousInput(0, 2 * Math.PI);

        if(setupPathPlanner) {
            setupPathPlanner();
        }

        try {
            // I believe this gets the settings from the path planner application
            this.robotConfig = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initalizeSwerveModules() {
        frontLeft = new SwerveModule(
                "fl",
                swerveConfig.moduleConfig().frontLeftDrivePort(),
                swerveConfig.moduleConfig().frontLeftTurningPort(),
                swerveConfig.moduleConfig().frontLeftEncoderPort(),
                swerveConfig.moduleConfig().frontLeftEncoderOffset(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxAngularVelocityRadiansPerSecond(),
                swerveConfig.moduleConfig().frontLeftEncoderReversed());

        backLeft = new SwerveModule(
                "bl",
                swerveConfig.moduleConfig().backLeftDrivePort(),
                swerveConfig.moduleConfig().backLeftTurningPort(),
                swerveConfig.moduleConfig().backLeftEncoderPort(),
                swerveConfig.moduleConfig().backLeftEncoderOffset(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxAngularVelocityRadiansPerSecond(),
                swerveConfig.moduleConfig().backLeftEncoderReversed());

        frontRight = new SwerveModule(
                "fr",
                swerveConfig.moduleConfig().frontRightDrivePort(),
                swerveConfig.moduleConfig().frontRightTurningPort(),
                swerveConfig.moduleConfig().frontRightEncoderPort(),
                swerveConfig.moduleConfig().frontRightEncoderOffset(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxAngularVelocityRadiansPerSecond(),
                swerveConfig.moduleConfig().frontRightEncoderReversed());

        backRight = new SwerveModule(
                "br",
                swerveConfig.moduleConfig().backRightDrivePort(),
                swerveConfig.moduleConfig().backRightTurningPort(),
                swerveConfig.moduleConfig().backRightEncoderPort(),
                swerveConfig.moduleConfig().backRightEncoderOffset(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxAngularVelocityRadiansPerSecond(),
                swerveConfig.moduleConfig().backRightEncoderReversed());

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
        Pose2d pose = null;
        if (camera != null)
            pose = camera.getRobotPose();

        return pose == null ? odometry.getPoseMeters() : pose;
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
        double magnitude = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
        //normalize the driving inputs if they are too large
        if (magnitude > 1) {
            xSpeed /= magnitude;
            ySpeed /= magnitude;
        }
        //clamp the rotation between -1 and 1
        rot = MathUtil.clamp(rot, -1, 1);

        // apply the max speeds
        xSpeed *= swerveConfig.maxSpeedMetersPerSecond();
        ySpeed *= swerveConfig.maxSpeedMetersPerSecond();
        rot *= swerveConfig.maxAngularVelocityRadiansPerSecond();

        double rotatedX = xSpeed, rotatedY = ySpeed;
        if (fieldRelative) {
            double robotAngle = getHeading();

            rotatedX = Math.cos(robotAngle) * xSpeed - ySpeed * Math.sin(robotAngle);
            rotatedY = Math.sin(robotAngle) * xSpeed + ySpeed * Math.cos(robotAngle);
            //rotate the drive inputs based on the robot angle
        }

        SwerveModuleState[] swerveModuleStates = swerveConfig.driveKinematics().toSwerveModuleStates(
            new ChassisSpeeds(rotatedX, rotatedY, rot)
        );

        setModuleStates(swerveModuleStates);
    }

    public void drive(double xSpeed, double ySpeed, double xHeading, double yHeading) {
        double headingAngle = Math.atan2(yHeading, xHeading) + Math.PI; // in radians

        drive(
                xSpeed,
                ySpeed,
                xHeading == 0 && yHeading == 0 ? 0 : // so that when you stop pressing the right stick it'll stop
                                                     // spinning
                        turnController.calculate(getHeading(), headingAngle),
                true);
    }

    public void drive(ChassisSpeeds speeds, boolean fieldRelative) {
        drive(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond, fieldRelative);
    }

    /**
     * @apiNote Test before competition
     * @return A command that will drive the robot to the pose
     * @param pose The position you want to pathfind to
     */
    public Command driveTo(Pose2d pose) {
        AutoBuilder.resetOdom(getPose());
        PathConstraints constraints = new PathConstraints(4, 4, 4, 4);

        return AutoBuilder.pathfindToPose(pose, constraints);
    }

    /**
     * Sets the swerve ModuleStates.
     * 
     * @param desiredStates The desired SwerveModule states.
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        //front and back motors got reversed somehow
        frontLeft.setDesiredState(desiredStates[2]);
        frontRight.setDesiredState(desiredStates[3]);
        backLeft.setDesiredState(desiredStates[0]);
        backRight.setDesiredState(desiredStates[1]);
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
        return swerveConfig.gyroscope().getRadians();
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
                (speeds, feedforwards) -> drive(speeds, false), // Method that will drive the robot given ROBOT RELATIVE
                                                                // ChassisSpeeds. Also optionally outputs individual
                                                                // module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for
                                                // holonomic drive trains
//TODO figure out what these pid controllers do and configure them
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
                ),
                robotConfig, // The robot configuration
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red
                    // alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent() && !swerveConfig.isFieldSymmetric()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this // Reference to this subsystem to set requirements
        );
        // swerveConfig.gyroscope().zero(); // zero so that the robot is facing forward and not sideways
    }
}
