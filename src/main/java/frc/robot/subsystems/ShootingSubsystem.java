package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

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
    private boolean IntakeArmPose = true; //false = down, true = up

    //Shooter
    private SparkFlex shootingSystem;
    public boolean ShootingOn = false;

    //Test
    private SparkMax TestLeft;
    private SparkMax TestRight;

    //Intake Arm Dashboard vars
    private boolean IntakeArmDown = false;
    private boolean IntakeArmUp = false;


   // private final SparkClosedLoopController shooterPID;
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
       //XXX KickerSystem = new SparkFlex(ShootingConstants.Kicker, MotorType.kBrushless);
       KickerSystem = new SparkFlex(ShootingConstants.Kicker, MotorType.kBrushless);
        SmartDashboard.putBoolean("Kicker", KickerOn);

        //Shooter
        //XXX shootingSystem = new SparkFlex(ShootingConstants.Shooter, MotorType.kBrushless);
        shootingSystem = new SparkFlex(100, MotorType.kBrushless);

        SparkFlexConfig shooterConfig = new SparkFlexConfig();
        shooterConfig.smartCurrentLimit(60);
        shooterConfig.voltageCompensation(12); 
        shooterConfig.idleMode(SparkBaseConfig.IdleMode.kCoast); // XXX or .kBrake?

        //Test
        TestLeft = new SparkMax(ShootingConstants.IntakeArmLeft, MotorType.kBrushless);
        TestRight = new SparkMax(ShootingConstants.IntakeArmRight, MotorType.kBrushless);

        // Create one config object with the parameters we want and configure both left and right motors
        // with the same object.
        SparkMaxConfig lr_config = new SparkMaxConfig();
        lr_config.smartCurrentLimit(45);
        TestLeft.configure(lr_config, SparkMax.ResetMode.kNoResetSafeParameters, SparkMax.PersistMode.kNoPersistParameters);
        TestRight.configure(lr_config, SparkMax.ResetMode.kNoResetSafeParameters, SparkMax.PersistMode.kNoPersistParameters);
        SmartDashboard.putBoolean("IntakeArmGoingDown", IntakeArmDown);
        SmartDashboard.putBoolean("IntakeArmGoingUp", IntakeArmUp);
        
        /*
         * Consider adding closedloopcontroller to help the motor maintain
         * the set speed. See, for example, PSU 2026-swerve's Robot.java in
         * github.com/Ri3D-PSU/2026-swerve.
         */

         /* 
          shooterConfig.closedLoop.pidf (0.001, 0.0, 0.005, 0.001825);
          EncoderConfig encoderConfig = new EncoderConfig();
           encoderConfig.velocityConversionFactor(1.0);
           encoderConfig.positionConversionFactor(1.0);
           shooterConfig.apply(encoderConfig);
           shooterPID = shootingSystem.getClosedLoopController();
           this.setDefaultCommand(this.run(() -> 
           {
            shootingSystem.setVoltage(0);
        //    setFiring(false);
           }));
           */
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


      //Start of commented While Code XXX
      /* 
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

    */
    // End of commented while code XXX

    //Shooting Subsystem

    public void Shooter()
    {
        if(shootingSystem.get() != 0)
        {
            shootingSystem.set(0);
            ShootingOn = false;
        }
        else
        {
            shootingSystem.set(-0.5);
            ShootingOn = true;
        }
        SmartDashboard.putBoolean("Shooting", ShootingOn);
    }
    public void StopShooter()
    {
        shootingSystem.set(0);
        ShootingOn = false;
        SmartDashboard.putBoolean("Shooting", ShootingOn);
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
        
        public void testDown() {
                if(TestRight.get() != 0)
            {
                TestRight.set(0);
                TestLeft.set(0);
                IntakeArmDown = false;
                SmartDashboard.putBoolean("IntakeArmGoingDown", IntakeArmDown);
            }
            else
            {
                TestRight.set(0.1);
                TestLeft.set(0.1);
                IntakeArmDown = true;
                SmartDashboard.putBoolean("IntakeArmGoingDown", IntakeArmDown);
            }
        }
        public void testUp() {
            if(TestRight.get() != 0)
            {
                TestRight.set(0);
                TestLeft.set(0);
                IntakeArmUp = false;
                SmartDashboard.putBoolean("IntakeArmGoingUp", IntakeArmUp);
            }
            else
            {
                TestRight.set(-0.8);
                TestLeft.set(-0.8);
                IntakeArmUp = true;
                SmartDashboard.putBoolean("IntakeArmGoingUp", IntakeArmUp);
            }
        }
    

}
