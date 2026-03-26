package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShootingConstants;

public class CHATGPTShootingSubsystem extends SubsystemBase {

    private SparkFlex ShootingSystem;
    private SparkClosedLoopController shooterPID;
    private double shooterSetpoint = 0; // PID target
    public boolean ShootingOn = false;

    // ✅ Constructor
    public CHATGPTShootingSubsystem() {
        ShootingSystem = new SparkFlex(ShootingConstants.Shooter, MotorType.kBrushless);

        // Motor + PID config
        SparkFlexConfig config = new SparkFlexConfig();
        config.smartCurrentLimit(60);
        config.voltageCompensation(12);

        // PID values (tune these!)
        config.closedLoop.pidf(0.001, 0.0, 0.005, 0.001825);

        ShootingSystem.configure(
                config,
                SparkBase.ResetMode.kResetSafeParameters,
                SparkBase.PersistMode.kPersistParameters
        );

        shooterPID = ShootingSystem.getClosedLoopController();

        SmartDashboard.putBoolean("Shooting", ShootingOn);
    }

    // Toggle shooter using the setpoint for reliable PID behavior
    public void Shooting() {
        if (shooterSetpoint != 0) {      // check intended state
            shooterSetpoint = 0;          // stop PID target
            ShootingOn = false;
            System.out.println("Cease fire!");
        } else {
            shooterSetpoint = -3000;       // target RPM
            ShootingOn = true;
            System.out.println("Shots fired!");
        }
        SmartDashboard.putBoolean("Shooting", ShootingOn);
    }

    public void stopShooting() {
        shooterSetpoint = 0;
        ShootingOn = false;
        System.out.println("Cease fire!");
        SmartDashboard.putBoolean("Shooting", ShootingOn);
    }

    @Override
    public void periodic() {
        // Continuously enforce PID target
        shooterPID.setReference(
                shooterSetpoint,
                SparkBase.ControlType.kVelocity
        );
    }
}