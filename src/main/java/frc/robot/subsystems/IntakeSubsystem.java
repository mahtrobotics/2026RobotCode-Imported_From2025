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

public class IntakeSubsystem extends SubsystemBase{

    private SparkFlex IntakeSystem;
    public boolean IntakeOn = false;
    private SparkMax IntakeArmSystem;
    public boolean IntakeArmOn = false;
    
    public IntakeSubsystem(){

        IntakeSystem = new SparkFlex(ShootingConstants.Intake, MotorType.kBrushless);
        SmartDashboard.putBoolean("Intake", IntakeOn);
        IntakeArmSystem = new SparkMax(ShootingConstants.IntakeArm, MotorType.kBrushless);

        }

        // Intake Mechanism
        public void Intake()
        {
            if(IntakeSystem.get() != 0)
            {
                IntakeSystem.set(0);
                IntakeOn = false;
                SmartDashboard.putBoolean("Intake", IntakeOn);
            }
            else
            {
                IntakeSystem.set(-1.0);
                IntakeOn = true;
                SmartDashboard.putBoolean("Intake", IntakeOn);
            }
        }
        public void StopIntake()
        {
            IntakeSystem.set(0);
            IntakeOn = false;
            SmartDashboard.putBoolean("Intake", IntakeOn);
        }

        // Intake Arm Mechanism
        public void IntakeArm()
        {




        }
    
    


}
