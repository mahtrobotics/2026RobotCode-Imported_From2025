package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShootingConstants;

public class ShootingSubsystem extends SubsystemBase {

    private SparkFlex shootingSystem;

    public boolean ShootingOn = false;
/* 
    public final GenericEntry SPEED_TABLE = Shuffleboard.getTab("Configuration")
            .add("Shooter Speed Table", new double[] {
                    0.300,
                    0.375, // last value tested
                    0.400,
                    0.500
            }).getEntry();*/
    
    // put the shooter speed on the dashboard so we don't have to rebuild to try a new value
    public final GenericEntry SHOOTER_VELOCITY = Shuffleboard.getTab("Configuration")
            .add("Shooter Velocity", 0.375) // the number here is a default for the display
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();

    public ShootingSubsystem() {

        shootingSystem = new SparkFlex(ShootingConstants.Shooter, MotorType.kBrushless);

        SparkFlexConfig shooterConfig = new SparkFlexConfig();
        shooterConfig.smartCurrentLimit(40);
        shooterConfig.voltageCompensation(12); //
        shooterConfig.idleMode(SparkBaseConfig.IdleMode.kCoast); // XXX or .kBrake?
        /*
         * Consider adding closedloopcontroller to help the motor maintain
         * the set speed. See, for example, PSU 2026-swerve's Robot.java in
         * github.com/Ri3D-PSU/2026-swerve.
         * 
         * shooterConfig.closedLoop.pidf (0.001, 0.0, 0.00001, 0.000330); //XXX TUNE
         * EncoderConfig encoderConfig = new EncoderConfig();
         * // XXX Insert encoder config settings
         * // encoderConfig.velocityConversionFactor(...)
         * // encoderConfig.positionConverstionFactor(...)
         * shooterConfig.apply(encoderConfig);
         * shooterPID = .getClosedLoopController();
         */
        SmartDashboard.putBoolean("Shooting", ShootingOn);
        // TODO add SmartDashboard config for speed, to make it easy to experiment with
        // tuning

    }

    public void Shooting() {
        if (shootingSystem.get() != 0) {
            shootingSystem.set(0);
            ShootingOn = false;
            System.out.println("Cease fire!");
        } else {
            double configSpeed = SHOOTER_VELOCITY.getDouble(0.375); // 0.375 is the default if the dashboard doesn't have a value
            shootingSystem.set(configSpeed);
            // Need to add a delay statement here, thread.sleep may cause problems
            ShootingOn = true;
        }
        SmartDashboard.putBoolean("Shooting", ShootingOn);
    }

    public void stopShooting() // Only for auto (I think)
    {
        shootingSystem.set(0);
        ShootingOn = false;
        SmartDashboard.putBoolean("Shooting", ShootingOn);
    }

}
