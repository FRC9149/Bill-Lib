package com.robocats.swerve;

import edu.wpi.first.math.controller.PIDController;
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
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.sim.CANcoderSimState;

public class SwerveModule {

    private final String name;

    private final SparkMax driveMotor; // rotates the wheel to make it spin
    private final RelativeEncoder driveEncoder;
    private SparkMaxConfig driveConfig = new SparkMaxConfig();

    private final SparkMax turnMotor; // rotates the wheel to change direction
    private final CANcoder absoluteEncoder;
    private final PIDController turnController = new PIDController(0.5, 0.1, 0.1);
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
            boolean motorReversed) {

        this.name = name;
        this.maxSpeedMetersPerSecond = maxSpeedMetersPerSecond;
        this.wheelDiameterMeters = wheelDiameterMeters;

        driveMotor = new SparkMax(driveMotorPort, MotorType.kBrushless);
        driveEncoder = driveMotor.getEncoder();

        driveConfig.idleMode(IdleMode.kBrake);
        driveConfig.inverted(motorReversed);
        driveMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        turnMotor = new SparkMax(turningMotorPort, MotorType.kBrushless);
        absoluteEncoder = new CANcoder(encoderPort);

        turnConfig.idleMode(IdleMode.kBrake);
        turnMotor.configure(turnConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        turnController.enableContinuousInput(0, 2 * Math.PI);
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

    /**
     * @return The current angle of the module relative to its zero (radians)
     */
    private double getTurnDistance() {
        return (absoluteEncoder.getAbsolutePosition().getValueAsDouble()) * 2 * Math.PI;
    }

    /**
     * @return The current state of the module.
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(getTurnDistance()));
    }

    /**
     * @return The current position of the module.
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDriveDistance(), new Rotation2d(getTurnDistance()));
    }

    /**
     * Sets the desired state for the module.
     *
     * @param desiredState Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState desiredState) {
        Rotation2d encoderRotation = new Rotation2d(getTurnDistance());

        // Optimize the reference state to avoid spinning further than 90 degrees
        desiredState.optimize(encoderRotation);

        // Scale speed by cosine of angle error. This scales down movement perpendicular
        // to the desired direction of travel that can occur when modules change
        // directions. This results in smoother driving.
        desiredState.cosineScale(encoderRotation);

        final double turnOutput = turnController.calculate(getTurnDistance(), desiredState.angle.getRadians());

        driveMotor.set(desiredState.speedMetersPerSecond / maxSpeedMetersPerSecond);
        turnMotor.set(turnOutput);
    }

    /**
     * Zeroes the driving encoder.
     */
    public void resetEncoders() {
        driveEncoder.setPosition(0);
    }

}
