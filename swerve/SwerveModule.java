package com.robocats.swerve;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.sim.CANcoderSimState;

public class SwerveModule {

    private final String name;

    private final SparkMax driveMotor; // rotates the wheel to make it spin
    private final RelativeEncoder driveEncoder;
    private SparkMaxConfig driveConfig = new SparkMaxConfig();
    private final SparkClosedLoopController driveController;
    // private SlewRateLimiter rateLimiter = new SlewRateLimiter(1.5);

    private final SparkMax turnMotor; // rotates the wheel to change direction
    private final CANcoder absoluteEncoder;
    private final SparkClosedLoopController turnController;
 
    // private final PIDController driveController = new PIDController(0.3, 0, 0.001);
    private final RelativeEncoder turnEncoder;
    private SparkMaxConfig turnConfig = new SparkMaxConfig();

    private final double maxSpeedMetersPerSecond;
    private final double wheelDiameterMeters;

    /**
     * Constructs a SwerveModule.
     *
     * @param name                    The name that will be printed to the
     *                                dashboard.
     * @param driveMotorPort          The port of the drive motor.
     * @param turningMotorPort        The port of the turning motor.
     * @param encoderPort             The port of the absolute encoder.
     * @param encoderOffset           The offset of the absolute encoder as defined
     *                                by pheonix.
     * @param absoluteEncoderReversed Whether or not the absolute encoder is
     *                                reversed.
     */
    public SwerveModule(
            String name,
            int driveMotorPort,
            int turningMotorPort,
            int encoderPort,
            double wheelDiameterMeters,
            double maxSpeedMetersPerSecond,
            boolean motorReversed) 
    {
        this.name = name;
        this.maxSpeedMetersPerSecond = maxSpeedMetersPerSecond;
        this.wheelDiameterMeters = wheelDiameterMeters;
        


        driveMotor = new SparkMax(driveMotorPort, MotorType.kBrushless);
        driveEncoder = driveMotor.getEncoder();
        driveController = driveMotor.getClosedLoopController();
     
   

        driveConfig.idleMode(IdleMode.kBrake);
        
        driveConfig.inverted(motorReversed);
        driveConfig.smartCurrentLimit(40, 40);
        driveConfig.closedLoopRampRate(.01);

        //ClosedLoopConfig driveControllerConfig = new ClosedLoopConfig();
        //driveControllerConfig.pid(0.0025, 0, 1);
//
        //driveConfig.closedLoop.apply(driveControllerConfig);

        driveMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        turnMotor = new SparkMax(turningMotorPort, MotorType.kBrushless);
        turnEncoder = turnMotor.getEncoder();
        // Waits up to 100ms for a valid position signal from the CAN bus
    //turnEncoder = turnMotor.getEncoder().waitForUpdate(0.1).getValueAsDouble(); //4EST NOTE: USED TO BE: turnEncoder = turnMotor.getEncoder(); IF IT IS MESSED UP, SWITCH BACK TO THIS
        absoluteEncoder = new CANcoder(encoderPort);
        turnController = turnMotor.getClosedLoopController();

        turnConfig.idleMode(IdleMode.kBrake);
        turnConfig.encoder.positionConversionFactor(1/21.42857143);
        turnConfig.encoder.velocityConversionFactor(1);
        turnConfig.smartCurrentLimit(40, 40);

        ClosedLoopConfig turnControllerConfig = new ClosedLoopConfig();
        turnControllerConfig.pid(0.5, 0, 0); //was 5, 0, 0
        turnControllerConfig.positionWrappingInputRange(-1, 1);
        turnControllerConfig.positionWrappingEnabled(true);
        turnConfig.closedLoop.apply(turnControllerConfig);


        turnMotor.configure(turnConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        turnEncoder.setPosition(absoluteEncoder.getAbsolutePosition().getValueAsDouble());
    }

    public void periodic() {
        SmartDashboard.putNumber(name + " turn encoder", getTurnDistance());
    }

    /**
     * @return The distance that the module has driven since reset (meters)
     */
    private double getDriveDistance() {
        return driveEncoder.getPosition() * (Math.PI * Math.pow(wheelDiameterMeters / 2, 2));
    }

    private double getSpeed() {
        return driveEncoder.getVelocity() / 60 * wheelDiameterMeters;
    }

    /**
     * @return The current angle of the module relative to its zero (radians)
     */
    private double getTurnDistance() {
        return turnEncoder.getPosition() * 2 * Math.PI;
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(getTurnDistance()));
    }
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDriveDistance(), new Rotation2d(getTurnDistance()));
    }
    public void setDesiredState(SwerveModuleState desiredState) {
        Rotation2d encoderRotation = new Rotation2d(getTurnDistance());
        // Optimize the reference state to avoid spinning further than 90 degrees
        desiredState.optimize(encoderRotation);

        // Scale speed by cosine of angle error. This scales down movement perpendicular
        // to the desired direction of travel that can occur when modules change
        // directions. This results in smoother driving.
        desiredState.cosineScale(encoderRotation);

        turnController.setSetpoint(desiredState.angle.getRotations(), ControlType.kPosition);
        SmartDashboard.putNumber(name + "turnSetpoint", desiredState.angle.getRotations());
        SmartDashboard.putNumber(name + "driveSpeed", desiredState.speedMetersPerSecond / maxSpeedMetersPerSecond);

         driveMotor.set(desiredState.speedMetersPerSecond / maxSpeedMetersPerSecond);
       // double speed = MathUtil.clamp(desiredState.speedMetersPerSecond, -1, 1);
        
        //driveController.setSetpoint(speed * 12, ControlType.kVelocity);

    }

    /**
     * Zeroes the driving encoder.
     */
    public void resetEncoders() {
        driveEncoder.setPosition(0);
    }

}
