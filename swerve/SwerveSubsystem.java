package com.robocats.swerve;
//Maple sim documentation:
// https://shenzhen-robotics-alliance.github.io/maple-sim/


import static edu.wpi.first.units.Units.Rotation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.Transform;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.controllers.PathFollowingController;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPoint;
import com.robocats.vision.AprilCamera;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
    // Controls how fast the robot spins to match a certain heading
    private PIDController turnController = new PIDController(0.2, 0.0, 0.05);
    private PIDController translationController = new PIDController(1, 0, 0.0);
    private LinearFilter turnFilter = LinearFilter.singlePoleIIR(.01, 0.02);
    private RobotConfig robotConfig;
    public final SwerveConfig swerveConfig;
    private SwerveModule frontLeft;
    private SwerveModule backLeft;
    private SwerveModule frontRight;
    private SwerveModule backRight;
    private ArrayList<AprilCamera> camera;
    private final Field2d m_field;
    // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-odometry.html
    SwerveDriveOdometry odometry;

    //Example code


    /**
     * @param config        The SwerveConfig record that holds information such as
     *                      dimensions, max speed, and gyroscope
     * @param pidController temporary test to congifure the pidControllers for the
     *                      turning motors
     * @param setupPathPlanner Defines if pathplanner should be setup automatically. Otherwise you can call `setupPathPlanner();` yourself
     */
    public SwerveSubsystem(SwerveConfig config, boolean setupPathPlanner, Field2d field) {
        swerveConfig = config;
        this.m_field = field;
        
        turnController.enableContinuousInput(0,  2*Math.PI);
        turnController.setTolerance(Math.PI / 45);

        initalizeSwerveModules();
        try {
            // This is the dimensions recieved from the pathplanner application
            this.robotConfig = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(setupPathPlanner) {
            setupPathPlanner();
        }
    }

    public void addCamera(int i, AprilCamera camera) {
        camera.add(i, camera);
    }

    private void initalizeSwerveModules() {
        frontLeft = new SwerveModule(
                "fl",
                swerveConfig.moduleConfig().frontLeftDrivePort(),
                swerveConfig.moduleConfig().frontLeftTurningPort(),
                swerveConfig.moduleConfig().frontLeftEncoderPort(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxSpeedMetersPerSecond(),
                swerveConfig.moduleConfig().frontLeftEncoderReversed());

        backLeft = new SwerveModule(
                "bl",
                swerveConfig.moduleConfig().backLeftDrivePort(),
                swerveConfig.moduleConfig().backLeftTurningPort(),
                swerveConfig.moduleConfig().backLeftEncoderPort(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxSpeedMetersPerSecond(),
                swerveConfig.moduleConfig().backLeftEncoderReversed());

        frontRight = new SwerveModule(
                "fr",
                swerveConfig.moduleConfig().frontRightDrivePort(),
                swerveConfig.moduleConfig().frontRightTurningPort(),
                swerveConfig.moduleConfig().frontRightEncoderPort(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxSpeedMetersPerSecond(),
                swerveConfig.moduleConfig().frontRightEncoderReversed());

        backRight = new SwerveModule(
                "br",
                swerveConfig.moduleConfig().backRightDrivePort(),
                swerveConfig.moduleConfig().backRightTurningPort(),
                swerveConfig.moduleConfig().backRightEncoderPort(),
                swerveConfig.wheelDiameterMeters(),
                swerveConfig.maxSpeedMetersPerSecond(),
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
        
        Rotation2d rot = getRotation();
        SmartDashboard.putNumber("gyro", rot.getRadians());
        frontLeft.periodic();
        frontRight.periodic();
        backLeft.periodic();
        backRight.periodic();

        odometry.update(
                rot,
                new SwerveModulePosition[] {
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition()
                });

        if(camera.size() > 0) {
            for(var cam : camera) cam.periodic();
        }
        m_field.setRobotPose(getPose());
    }
   
    /**
     * @return the best pose estimate for the robot, goes through the list i=0->∞ until it finds a pose that works, otherwises uses wheel odometry.
     */
    public Pose2d getPose() {
        for(var cam : camera) {
            if(cam == null) continue;
            Pose2d pose = cam.getRobotPose();
            if(pose == null || (pose.getX() == 0 && pose.getY() == 0)) continue;
            return pose;
        }
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

    public void resetAutoBuilder() {
        AutoBuilder.resetOdom(getPose());
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

        SmartDashboard.putNumber("DriveX", xSpeed);
        SmartDashboard.putNumber("DriveY", ySpeed);

        //----------------The Great El's awesome code that I currently have commented out and marked for easy finding--------------------------

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
                        turnController.calculate((turnFilter.calculate(getHeading())), headingAngle),
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
        return new RunCommand(() -> {
        Pose2d currentPose = getPose();
         if (currentPose == null || (currentPose.getX() == 0 && currentPose.getY() == 0)) {
            System.out.println("currentpose is null");
            this.drive(0, 0, 0, true);
            return;
        }
         
        double x = -translationController.calculate(currentPose.getX(), pose.getX());
        double y = -translationController.calculate(currentPose.getY(), pose.getY());
        double r = -turnController.calculate(getHeading(), pose.getRotation().getRadians());

        this.drive(y, x, r, true);
        
    }, this).until(() -> {
        Pose2d currentPose = getPose();
        if (currentPose == null) return false;
        
        boolean xOnTarget = Math.abs(currentPose.getX() - pose.getX()) < 0.05;
        boolean yOnTarget = Math.abs(currentPose.getY() - pose.getY()) < 0.05;
        boolean rotOnTarget = Math.abs(getHeading() - pose.getRotation().getRadians()) < 0.05;
        return xOnTarget && yOnTarget && rotOnTarget;
    });

/*
        Pose2d currentPose = getPose();
        System.out.println(currentPose);
         if (currentPose == null) {
            System.out.println("currentpose is null");
            return new RunCommand(()->System.out.println(currentPose));
        }
        PathConstraints constraints = new PathConstraints(
           swerveConfig.maxSpeedMetersPerSecond(), 
           0.5,
           swerveConfig.maxAngularVelocityRadiansPerSecond(), 
           0.5
        );

        // Command c = AutoBuilder.pathfindToPose(pose, constraints);
        // c.addRequirements(this);
        ArrayList<PathPoint> listPose = new ArrayList<PathPoint>();
        listPose.add(new PathPoint(new Translation2d(currentPose.getX(), currentPose.getY())));
        listPose.add(new PathPoint(new Translation2d(pose.getX(), pose.getY())));
        PathPlannerPath path = PathPlannerPath.fromPathPoints(listPose, constraints, new GoalEndState(0, pose.getRotation()));
    //    return c;
        return new InstantCommand(()->System.out.println("STARTED"), this).andThen( new FollowPathCommand(
            path,
            this::getPose,
            this::getChassisSpeeds,
            (speeds, feedforwards) -> drive(speeds, false),
            new PPHolonomicDriveController(new PIDConstants(0.5, 0, 0), new PIDConstants(0.5, 0, 0)),
            robotConfig,
            ()->{var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent() && !swerveConfig.isFieldSymmetric()) {//!swerveConfig.isFieldSymmetric()_is_the_culprit_I_think, I ran out of time to investigate
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;},
            this
        ));*/
    }

    /**
     * Sets the swerve ModuleStates.
     * 
     * @param desiredStates The desired SwerveModule states.
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {          
        SwerveModuleState fl = desiredStates[0]; //2
        SwerveModuleState fr = desiredStates[1]; //3
        SwerveModuleState bl = desiredStates[2]; //0
        SwerveModuleState br = desiredStates[3]; //1

        frontLeft.setDesiredState(fl);
        frontRight.setDesiredState(fr);
        backLeft.setDesiredState(bl);
        backRight.setDesiredState(br);
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
        return swerveConfig.gyroscope().getRotation2d();
    }

    /**
     * @return the robot's heading in radians, from 0 to 2PI
     */
    public double getHeading() {
        return swerveConfig.gyroscope().getRadians();
    }

    /**
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
                    if (alliance.isPresent() && !swerveConfig.isFieldSymmetric()) {//!swerveConfig.isFieldSymmetric()_is_the_culprit_I_think, I ran out of time to investigate
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this
                //First of all, this comment doesn't explain what I am looking at.
                //secondly, I think i figured it out, but we already have that (it's just written differently)
                //If you are saying we need to delete the part before "this" and just make it false, then I agree
                //actually we just need to make isFieldSymmetric false

                //4EST NOTE: I think the fact that is FieldSymmetric is true
                //means that it's not passing into the math, so yes, as you mentioned,
                //we should make isFieldSymmetric false, or clarify it
                //since the field literally is symmetric, but since we are saying it is true,
                //the code isn't entering that If statement that I believe is neccesary fo the code working.

                //The code below is just another, seemingly smoother way to write it, but it 
                //didn't do anything at all to change the controls when I tested it.
            
                //-----------WE SHOULD LOOK INTO THIS------------------,_I_THink_it_may_work_with_some_adjustment,_not_my_code
                //// Reference to this subsystem to set requirements
                //robotConfig,
   // () -> DriverStation.getAlliance()
        //.map(a -> a == DriverStation.Alliance.Red)
       // .orElse(false),
       //         this // Reference to this subsystem to set requirements
       // );
        );
    }
}
