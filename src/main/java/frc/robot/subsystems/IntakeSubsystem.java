package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShootingConstants;

public class IntakeSubsystem extends SubsystemBase{

    private SparkFlex IntakeSystem;
    public boolean IntakeOn = false;
    private SparkMax IntakeArmSystem;
    public boolean IntakeArmOn = false;
    double gearRatio = 6.0;

    //Limit Switch
    private DigitalInput bottomSwitch = new DigitalInput(0);
    
    public IntakeSubsystem(){

        IntakeSystem = new SparkFlex(ShootingConstants.Intake, MotorType.kBrushless);
        SmartDashboard.putBoolean("Intake", IntakeOn);
        IntakeArmSystem = new SparkMax(ShootingConstants.IntakeArm, MotorType.kBrushless);
        
        SmartDashboard.putBoolean("IntakeArm", IntakeArmOn);

        
       

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
        public void IntakeArmUp()
        {
            RelativeEncoder encoder = IntakeArmSystem.getEncoder();
            double position = (encoder.getPosition() / 6);
            double velocity = encoder.getVelocity();
            double angleRadians = encoder.getPosition() * 2 * Math.PI;
            IntakeArmSystem.setVoltage(2 * Math.cos(angleRadians));
        }
        public void IntakeArmDown()
        {
            IntakeArmSystem.setVoltage(1);
        }
        public void StopIntakeArm()
        {
            IntakeArmSystem.setVoltage(0);
        }
    
    


}
