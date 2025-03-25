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
  private final SparkMax m_driveMotor;
  private final SparkMax m_turningMotor;
  private final SparkMaxConfig config = new SparkMaxConfig();
  
  private final RelativeEncoder m_driveEncoder;

  private final CANcoder m_absolute_encoder;

  private final double m_absoluteOffset;
  private final boolean m_motorReversed;
  private final double maxSpeedMetersPerSecond;
  private final double WheelDiameterMeters;

  public void periodic() {
    SmartDashboard.putNumber(name + " turn encoder", getTurnDistance());
  }

  /**
   * @return The distance that the module has driven since reset (meters)
   */
  private double getDriveDistance() {
    // if(m_driveMotor.getDeviceId() == 2) System.out.println(m_driveEncoder.getPosition());

    return m_driveEncoder.getPosition() * (Math.PI * Math.pow(WheelDiameterMeters/2, 2)) ;
    // multiple the rotation amount by the circumfrence to get the distance traveled [PI(r^2)]
  }

  /**
   * @return The current angle of the module (radians)
   */
  private double getTurnDistance() {
    double rotationRad = (m_absolute_encoder.getAbsolutePosition().getValueAsDouble() - m_absoluteOffset) * 2 * Math.PI;
    // rotationRad = rotationRad > Math.PI ? rotationRad - 2 * Math.PI : rotationRad;
    // rotationRad = rotationRad < -Math.PI ? rotationRad + 2 * Math.PI : rotationRad; // bounds the angle to -PI-PI instead of 0-2PI

    return rotationRad;
    // if(m_absolute_encoder.getDeviceID() == 15) System.out.println(m_absolute_encoder.getAbsolutePosition().getValueAsDouble());
/*
    double encoderValue = m_absolute_encoder.getAbsolutePosition().getValueAsDouble() + ((m_absoluteReversed ? -1 : 1) * m_absoluteOffset);

    encoderValue += m_motorReversed ? .5 : 0;
    while (encoderValue < 0) encoderValue += 1;
    while (encoderValue > 1) encoderValue -= 1; // 1 = 360 degrees so we're finding a coterminal angle that that is between 0 and 1 rotations
    //probably could skip this step but it's not a big deal

    return encoderValue * 2 * Math.PI; //convert to radians*/
  }

  private final PIDController m_drivePIDController = new PIDController(0.002, 1.5, 2.0);

  // Using a TrapezoidProfile PIDController to allow for smooth turning
  private final PIDController m_turningPIDController = new PIDController(
      .743,
      .385,
      .015
      //, new TrapezoidProfile.Constraints(
          // ModuleConstants.kMaxModuleAngularSpeedRadiansPerSecond,
          // ModuleConstants.kMaxModuleAngularAccelerationRadiansPerSecondSquared)
  );
  private final PIDController test;
  
    /**
     * Constructs a SwerveModule.
     *
     * @param driveMotorChannel   The channel of the drive motor.
     * @param turningMotorChannel The channel of the turning motor.
     * @param encoderChannel      The channel of the absolute encoder
     * @param encoderOffset       The offset of the absolute encoder
     * @param absoluteEncoderReversed Whether or not the absolute encoder is reversed
     */
    public SwerveModule(
        String name,
        int driveMotorChannel,
        int turningMotorChannel,
        int encoderChannel,
        double encoderOffset,
        double wheelDiameterMeters,
        double maxSpeedMetersPerSecond,
        boolean motorReversed,
        PIDController test) {
          this.test = test;
      this.name = name;
      this.maxSpeedMetersPerSecond = maxSpeedMetersPerSecond;
    this.WheelDiameterMeters = wheelDiameterMeters;
    m_driveMotor = new SparkMax(driveMotorChannel, MotorType.kBrushless);
    m_turningMotor = new SparkMax(turningMotorChannel, MotorType.kBrushless);

    m_driveEncoder = m_driveMotor.getEncoder();

    m_absolute_encoder = new CANcoder(encoderChannel);

    m_absoluteOffset = encoderOffset;
    m_motorReversed = motorReversed;
    config.idleMode(IdleMode.kBrake);

    m_driveMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    m_turningMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

    // Limit the PID Controller's input range between -pi and pi and set the input
    // to be continuous.
    test.enableContinuousInput(0, 2*Math.PI);
    m_turningPIDController.enableContinuousInput(0, 2*Math.PI);
  }

  /**
   * Returns the current state of the module.
   *
   * @return The current state of the module.
   */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
        (m_motorReversed ? -1 : 1) * m_driveEncoder.getVelocity(),
        new Rotation2d(getTurnDistance()));
    // original

    // return new SwerveModuleState(
    // m_driveEncoder.getRate(), new Rotation2d(m_turningEncoder.getDistance()));
  }

  /**
   * Returns the current position of the module.
   *
   * @return The current position of the module.
   */
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        getDriveDistance(),
        new Rotation2d(getTurnDistance()));
    // original

    // return new SwerveModulePosition(
    // m_driveEncoder.getDistance(), new
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

    // double velocity = m_driveEncoder.getVelocity() 
                    //   * 60 
                    //   * 2 * Math.PI * Math.pow(WheelDiameterMeters/2, 2);
    //Convert rpm to m/s
    // * 60 to get into seconds
    // * 2PI(r^2) for meters

    // Calculate the turning motor output from the turning PID controller.
    final double turnOutput = m_turningPIDController.calculate(getTurnDistance(), desiredState.angle.getRadians());
    // m_turningPIDController.calculate(m_turningEncoder.getDistance(),
    // desiredState.angle.getRadians());

    // Calculate the turning motor output from the turning PID controller.
    m_driveMotor.set(desiredState.speedMetersPerSecond / maxSpeedMetersPerSecond);
    m_turningMotor.set(turnOutput);
  }

  /** Zeroes all the SwerveModule encoders. */
  public void resetEncoders() {
    m_driveEncoder.setPosition(0);
  }
}
