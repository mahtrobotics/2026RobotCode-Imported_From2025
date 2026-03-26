package frc.robot.subsystems;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShootingConstants;

public class ShootingSubsystem extends SubsystemBase {

    //Kicker
    private boolean KickerOn = false;
    private SparkFlex KickerSystem;
    private TransferSubsystem m_TransferSubsystem;

    //Shooter
    private SparkFlex shootingSystem;
    public boolean ShootingOn = false;

    //Test
    private SparkMax TestLeft;
    private SparkMax TestRight;


    private final SparkClosedLoopController shooterPID;
/* 
    public final GenericEntry SPEED_TABLE = Shuffleboard.getTab("Configuration")
            .add("Shooter Speed Table", new double[] {
                    0.300,
                    0.375, // last value tested
                    0.400,
                    0.500
            }).getEntry();*/
    
    // put the shooter speed on the dashboard so we don't have to rebuild to try a new value
    public final GenericEntry SHOOTER_VEL = Shuffleboard.getTab("Configuration")
            .add("Shooter Velocity", -1) // the number here is a default for the display
            .withWidget(BuiltInWidgets.kNumberSlider)
            .getEntry();

    public ShootingSubsystem(TransferSubsystem transferSubsystem) {

        m_TransferSubsystem = transferSubsystem;

        //Kicker
        KickerSystem = new SparkFlex(ShootingConstants.Kicker, MotorType.kBrushless);
        SmartDashboard.putBoolean("Kicker", KickerOn);

        //Shooter
        shootingSystem = new SparkFlex(ShootingConstants.Shooter, MotorType.kBrushless);

        SparkFlexConfig shooterConfig = new SparkFlexConfig();
        shooterConfig.smartCurrentLimit(40);
        shooterConfig.voltageCompensation(12); //
        shooterConfig.idleMode(SparkBaseConfig.IdleMode.kCoast); // XXX or .kBrake?

        //Test
        TestLeft = new SparkMax(ShootingConstants.IntakeArmLeft, MotorType.kBrushless);
        TestRight = new SparkMax(ShootingConstants.IntakeArmRight, MotorType.kBrushless);

        
        /*
         * Consider adding closedloopcontroller to help the motor maintain
         * the set speed. See, for example, PSU 2026-swerve's Robot.java in
         * github.com/Ri3D-PSU/2026-swerve.
         */
          shooterConfig.closedLoop.pidf (0.001, 0.0, 0.005, 0.001825);
          EncoderConfig encoderConfig = new EncoderConfig();
           encoderConfig.velocityConversionFactor(1.0);
           encoderConfig.positionConversionFactor(1.0);
           shooterConfig.apply(encoderConfig);
           shooterPID = shootingSystem.getClosedLoopController();
           this.setDefaultCommand(this.run(() -> 
           {
            shootingSystem.setVoltage(0);
            setFiring(false);
           }));
        SmartDashboard.putBoolean("Shooting", ShootingOn);
        // TODO add SmartDashboard config for speed, to make it easy to experiment with
        // tuning

    }


    /* XXX
    public void Shooting() {
        if (shootingSystem.get() != 0) {
            shootingSystem.set(0);
            ShootingOn = false;
            System.out.println("Cease fire!");
        } else {
            double configSpeed = SHOOTER_VEL.getDouble(0.375); // 0.375 is the default if the dashboard doesn't have a value
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
      XXX  */

    public void setFiring(boolean isFiring)
    {
        if(isFiring)
        {
            m_TransferSubsystem.BedRoller();
            m_TransferSubsystem.Feeder();
            KickerSystem.set(0.5);
        } else{
            m_TransferSubsystem.StopBedRoller();
            m_TransferSubsystem.StopFeeder();
            KickerSystem.set(0);
        }
        
    }

    public void setShooterSpeed(double speed, boolean firingBoost) {
        double ff = 0;
        if (firingBoost) {
      //      ff = FIRE_BOOST_VOLTAGE.getDouble(0);
        }

        if (Math.abs(speed) < 1) {
            shootingSystem.setVoltage(0);
        }

        var configValue = SHOOTER_VEL.getDouble(-1);
        if (configValue != -1) {
            speed = SHOOTER_VEL.getDouble(-1);
        }

      //  Logger.recordOutput("Shooter/Velocity Setpoint", speed);
        shooterPID.setReference(speed, SparkBase.ControlType.kVelocity, ClosedLoopSlot.kSlot0,
            ff, SparkClosedLoopController.ArbFFUnits.kVoltage);
    }

    public boolean isAtSpeed(double speed) {
       final double SHOOTER_VELOCITY_RANGE = 100;
        var configValue = SHOOTER_VEL.getDouble(-1);
        if (configValue != -1) {
            speed = SHOOTER_VEL.getDouble(-1);
        }
        return Math.abs(getShooterVelocity() - speed) < SHOOTER_VELOCITY_RANGE;

    }

     public double getShooterVelocity() {
        return shootingSystem.getEncoder().getVelocity();
    }

    //Shooting Commands for auto

    public void Shooting() {
        
        while(!isAtSpeed(-3200))
        {
            boolean isAtSpeed = isAtSpeed(-3200);
            setShooterSpeed(-3200, false);
            setFiring(isAtSpeed);
        }
    }

    public void stopShooting() // Only for auto (I think)
    {
        setShooterSpeed(0, false);
        setFiring(false);
    }

    // Kicker Subystem

    public void Kicker()
        {
            if(KickerSystem.get() != 0)
            {
                KickerSystem.set(0);
                KickerOn = false;
            }
            else
            {
                KickerSystem.set(0.5);
                KickerOn = true;
            }
            SmartDashboard.putBoolean("Kicker", KickerOn);
        }
    public void StopKicker() //Only for auto (I think)
        {
            KickerSystem.set(0);
            KickerOn = false;
            SmartDashboard.putBoolean("Kicker", KickerOn);
        }
        public void testLeft1() {
            if(TestLeft.get() != 0)
            {
                TestLeft.set(0);
            }
            else
            {
                TestLeft.set(-1);
            }
        }
        public void testRight1() {
            if(TestRight.get() != 0)
            {
                TestRight.set(0);
            }
            else
            {
                TestRight.set(-1);
                
            }
        }
    

}
