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

public class ShootingSubsystem extends SubsystemBase{

    private SparkFlex ShootingSystem;
    public boolean ShootingOn = false;
    
    public ShootingSubsystem(){

        ShootingSystem = new SparkFlex(ShootingConstants.Shooter, MotorType.kBrushless);
        SmartDashboard.putBoolean("Shooting", ShootingOn);

        
        
        }

        public void Shooting()
        {
           if(ShootingSystem.get() != 0)
           {
            ShootingSystem.set(0);
            ShootingOn = false;
            System.out.println("Cease fire!");
           }
           else
           {
            ShootingSystem.set(0.625);
            ShootingOn = true;
            System.out.println("Shots fired!");
           }
           SmartDashboard.putBoolean("Shooting", ShootingOn);
        }
        
        public void stopShooting() //Only for auto (I think)
        {
            ShootingSystem.set(0);
            ShootingOn = false;
            System.out.println("Cease fire!");
            SmartDashboard.putBoolean("Shooting", ShootingOn);
        }
    
    


}

