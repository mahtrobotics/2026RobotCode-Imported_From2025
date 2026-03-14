package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShootingConstants;

public class TransferSubsystem extends SubsystemBase{

    private boolean KickerOn = false;
    private SparkFlex KickerSystem;
    private boolean BedRollerOn = false;
    private SparkMax BedRollerSystem;
    private boolean BedRoller2On = false;
    private SparkMax BedRollerSystem2;

    public TransferSubsystem()
    {
        KickerSystem = new SparkFlex(ShootingConstants.Kicker, MotorType.kBrushless);
        SmartDashboard.putBoolean("Kicker", KickerOn);
        BedRollerSystem = new SparkMax(ShootingConstants.BedRoller, MotorType.kBrushed);
        SmartDashboard.putBoolean("Bed Roller", BedRollerOn);
        BedRollerSystem2 = new SparkMax(ShootingConstants.BedRoller2, MotorType.kBrushed);
        SmartDashboard.putBoolean( "Bed Roller2 (TEST)", BedRoller2On);
    }
    // Kicker Mechanism
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

        // Bed Roller Mechanism
        public void BedRoller()
        {
            if(BedRollerSystem.get() != 0)
            {
                BedRollerSystem.set(0);
                BedRollerSystem2.set(0);
                BedRollerOn = false;
            }
            else
            {
                BedRollerSystem.set(1.0);
                BedRollerSystem2.set(1.0);
                BedRollerOn = true;
            }
            SmartDashboard.putBoolean("Bed Roller", BedRollerOn);
        }
        public void test()
        {
            BedRollerSystem2.set(1.0);
            System.out.println("Test 1111111111111111111111");
        }
        /* 
        public void BedRoller2()
        {
            if (BedRollerSystem2.get() != 0)
            {
                BedRollerSystem2.set(0);
                BedRoller2On = false;
            }
            else{
                BedRollerSystem2.set(0.5);
                BedRoller2On = true;
            }
            SmartDashboard.putBoolean("Bed Roller2 (TEST)", BedRoller2On);
        }
        */
        public void StopBedRoller() //Only for auto (I think)
        {
            BedRollerSystem.set(0);
            BedRollerOn = false;
            SmartDashboard.putBoolean("Bed Roller", BedRollerOn);
        }

}
