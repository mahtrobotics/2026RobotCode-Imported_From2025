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
    private SparkMax KickerSystem;
    private boolean BedRollerOn = false;
    private SparkMax BedRollerSystem;

    public TransferSubsystem()
    {
        KickerSystem = new SparkMax(ShootingConstants.Kicker, MotorType.kBrushless);
        SmartDashboard.putBoolean("Kicker", KickerOn);
        BedRollerSystem = new SparkMax(ShootingConstants.BedRoller, MotorType.kBrushless);
        SmartDashboard.putBoolean("Bed Roller", BedRollerOn);
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
                KickerSystem.set(0.1);
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
                BedRollerOn = false;
            }
            else
            {
                BedRollerSystem.set(0.1);
                BedRollerOn = true;
            }
            SmartDashboard.putBoolean("Bed Roller", BedRollerOn);
        }
        public void StopBedRoller() //Only for auto (I think)
        {
            BedRollerSystem.set(0);
            BedRollerOn = false;
            SmartDashboard.putBoolean("Bed Roller", BedRollerOn);
        }

}
