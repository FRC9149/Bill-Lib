package com.robocats.swerve;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.ctre.phoenix6.hardware.CANcoder;

public class SwerveModule {

    private final String name;
    //rotates the wheel to make it spin
    private final SparkMax driveMotor;
    //rotates the wheel to change direction
    private final SparkMax turningMotor;
    private final SparkMaxConfig config = new SparkMaxConfig();

    private final RelativeEncoder driveEncoder;

    private final CANcoder absoluteEncoder;

    private final double absoluteOffset;
    private final boolean motorReversed;
    private final double maxSpeedMetersPerSecond;
    private final double wheelDiameterMeters;

    public void periodic() {
        SmartDashboard.putNumber(name + " turn encoder", getTurnDistance());
    }

    /**
     * @return The distance that the module has driven since reset (meters)
     */
    private double getDriveDistance() {
        // if(driveMotor.getDeviceId() == 2) System.out.println(driveEncoder.getPosition());

        return driveEncoder.getPosition() * (Math.PI * Math.pow(wheelDiameterMeters / 2, 2));
        // multiple the rotation amount by the circumfrence to get the distance traveled [PI(r^2)]
    }

    /**
     * @return The current angle of the module relative to its zero (radians)
     */
    private double getTurnDistance() {
        double rotationRad = (absoluteEncoder.getAbsolutePosition().getValueAsDouble() - absoluteOffset) * 2 * Math.PI;
        // rotationRad = rotationRad > Math.PI ? rotationRad - 2 * Math.PI : rotationRad;
        // rotationRad = rotationRad < -Math.PI ? rotationRad + 2 * Math.PI : rotationRad; // bounds the angle to -PI-PI instead of 0-2PI

        return rotationRad;
    }

    //TODO configure PID controllers
    private final PIDController drivePIDController = new PIDController(0.5, 0, 0);
    private PIDController turningPIDController = new PIDController(
            .743,
            .385,
            .015
    //, new TrapezoidProfile.Constraints(
    // ModuleConstants.kMaxModuleAngularSpeedRadiansPerSecond,
    // ModuleConstants.kMaxModuleAngularAccelerationRadiansPerSecondSquared)
    );

    /**
     * Constructs a SwerveModule.
     *
     * @param name The name that will be printed to the dashboard.
     * @param driveMotorPort The port of the drive motor.
     * @param turningMotorPort The port of the turning motor.
     * @param encoderPort The port of the absolute encoder.
     * @param encoderOffset The offset of the absolute encoder as defined by pheonix.
     * @param absoluteEncoderReversed Whether or not the absolute encoder is reversed.
     */
    public SwerveModule(
            String name,
            int driveMotorPort,
            int turningMotorPort,
            int encoderPort,
            double encoderOffset,
            double wheelDiameterMeters,
            double maxSpeedMetersPerSecond,
            boolean motorReversed,
            PIDController test) {
        this.turningPIDController = new PIDController(test.getP(), test.getI(), test.getD());
        this.name = name;
        this.maxSpeedMetersPerSecond = maxSpeedMetersPerSecond;
        this.wheelDiameterMeters = wheelDiameterMeters;
        driveMotor = new SparkMax(driveMotorPort, MotorType.kBrushless);
        turningMotor = new SparkMax(turningMotorPort, MotorType.kBrushless);

        driveEncoder = driveMotor.getEncoder();

        //TODO possibly turn the absolute encoder into a class/interface so we can swap them dynamically
        //We could also bake the offset and reversed data into the encoder
        absoluteEncoder = new CANcoder(encoderPort);

        absoluteOffset = encoderOffset;
        this.motorReversed = motorReversed;
        config.idleMode(IdleMode.kBrake);

        driveMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        turningMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

        // Limit the PID Controller's input range between -pi and pi and set the input
        // to be continuous.
        turningPIDController.enableContinuousInput(0, 2 * Math.PI);
    }

    /**
     * Returns the current state of the module.
     *
     * @return The current state of the module.
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(
                (motorReversed ? -1 : 1) * driveEncoder.getVelocity(),
                new Rotation2d(getTurnDistance()));
        // original ↓↓↓

        // return new SwerveModuleState(
        // driveEncoder.getRate(), new Rotation2d(m_turningEncoder.getDistance()));
    }

    /**
     * Returns the current position of the module.
     *
     * @return The current position of the module.
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
            getDriveDistance(),
            new Rotation2d(getTurnDistance())
        );
        // original ↓↓↓

        // return new SwerveModulePosition(
        // driveEncoder.getDistance(), new
        // Rotation2d(m_turningEncoder.getDistance()));
    }

    /**
     * Sets the desired state for the module.
     *
     * @param desiredState Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState desiredState) {
        var encoderRotation = new Rotation2d(getTurnDistance());

        // Optimize the reference state to avoid spinning further than 90 degrees
        desiredState.optimize(encoderRotation);

        // Scale speed by cosine of angle error. This scales down movement perpendicular
        // to the desired
        // direction of travel that can occur when modules change directions. This
        // results in smoother
        // driving.
        desiredState.cosineScale(encoderRotation);

        // double velocity = driveEncoder.getVelocity() 
        //   * 60 
        //   * 2 * Math.PI * Math.pow(wheelDiameterMeters/2, 2);
        //Convert rpm to m/s
        // * 60 to get into seconds
        // * 2PI(r^2) for meters
        // Calculate the turning motor output from the turning PID controller.
        double velocity = driveEncoder.getVelocity() * 60 * Math.PI * wheelDiameterMeters;

        final double turnOutput = turningPIDController.calculate(getTurnDistance(), desiredState.angle.getRadians());
        final double driveOutput = drivePIDController.calculate(velocity, desiredState.speedMetersPerSecond);
        // turningPIDController.calculate(m_turningEncoder.getDistance(),
        // desiredState.angle.getRadians());

        // Calculate the turning motor output from the turning PID controller.
        driveMotor.set(desiredState.speedMetersPerSecond);
        turningMotor.set(turnOutput);
    }

    /**
     * Zeroes all the SwerveModule encoders.
     */
    public void resetEncoders() {
        driveEncoder.setPosition(0);
    }
}
