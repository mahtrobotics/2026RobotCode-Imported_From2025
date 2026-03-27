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

    private boolean BedRollerOn = false;
    private SparkMax BedRollerSystem;
    private boolean FeederOn = false;
    private SparkMax FeederSystem;

    public TransferSubsystem()
    {
        BedRollerSystem = new SparkMax(ShootingConstants.BedRoller, MotorType.kBrushed);
        SmartDashboard.putBoolean("Bed Roller", BedRollerOn);

        FeederSystem = new SparkMax(ShootingConstants.Feeder, MotorType.kBrushless);
        SmartDashboard.putBoolean("Feeder", FeederOn);
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
                BedRollerSystem.set(-0.7);
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

        public void Feeder()
        {
            if(FeederSystem.get() != 0)
            {
                FeederSystem.set(0);
                FeederOn = false;
            }
            else
            {
                FeederSystem.set(0.3);
                FeederOn = true;
            }
            SmartDashboard.putBoolean("Feeder", FeederOn);
        }

        public void StopFeeder()
        {
            FeederSystem.set(0);
            FeederOn = false;
            SmartDashboard.putBoolean("Feeder", FeederOn);
        }

}
