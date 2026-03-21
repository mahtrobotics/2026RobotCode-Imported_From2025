package frc.robot;


import frc.robot.constants.AutoConstants;
import frc.robot.constants.BasicConstants.ControllerConstants;
import frc.robot.constants.MechanismConstants;
import frc.robot.constants.ShootingConstants;
import frc.robot.constants.SwerveConstants.SwerveDriveConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.MechanismSubsystem;
import frc.robot.subsystems.PathSubsystem;
import frc.robot.subsystems.ShootingSubsystem;
import frc.robot.subsystems.TransferSubsystem;
import frc.robot.subsystems.VisionSubsystem;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.concurrent.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    //Smart Dashboard -----------
    private final SendableChooser<Command> m_chooser = new SendableChooser<>();
    private final SendableChooser<Command> Anglechooser = new SendableChooser<>();


    //Controllers
    CommandXboxController m_driverController = new CommandXboxController(ControllerConstants.DRIVE_REMOTE_PORT);
    CommandXboxController m_mechanismController = new CommandXboxController(ControllerConstants.MECHANISM_REMOTE_PORT);

    //Subsystems
    private final DriveSubsystem m_robotDrive = new DriveSubsystem();
    private final PathSubsystem m_robotPath = new PathSubsystem(m_robotDrive);
    private final MechanismSubsystem m_robotMechanisms = new MechanismSubsystem();
    private final TransferSubsystem m_TransferSubsystem = new TransferSubsystem();
    private final ShootingSubsystem m_ShootingSubsystem = new ShootingSubsystem(m_TransferSubsystem);
    private final IntakeSubsystem m_IntakeSubsystem = new IntakeSubsystem();
    //private final VisionSubsystem m_robotVision = new VisionSubsystem(m_robotDrive);
    
    
    DigitalInput opticalSensor = new DigitalInput(MechanismConstants.SENSOR_DIO_PORT);
    Trigger opticalTrigger = new Trigger(opticalSensor::get);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();
     //   configureAutoCommands();
        configureSmartDashboard();
        

        //Setting the default commands for subsystems. These run repeatedly but only when the subsystem is NOT RUNNING A DIFFERENT COMMAND
        m_robotDrive.setDefaultCommand(
            // Forward joystick values are negative, must be inverted
            // https://docs.wpilib.org/en/stable/docs/software/basic-programming/coordinate-system.html
            new RunCommand(() -> m_robotDrive.manualDrive(-m_driverController.getLeftY(), -m_driverController.getLeftX(), -m_driverController.getRightX()), m_robotDrive));

        // m_robotVision.setDefaultCommand(
        //     new RunCommand(() -> m_robotVision.visionPeriodic(), m_robotVision));
        
        m_robotMechanisms.setDefaultCommand(
           new RunCommand(() -> m_robotMechanisms.wrist(m_mechanismController.getLeftY()), m_robotMechanisms));

        
    }

    /*
     * Understanding Command Syntax
     * 
     * () -> methodCall() turns any method call into a "Runnable".
     * Passing a "Runnable" into Commands.runOnce() gives you a command.
     * This allows any methods written in subsystems to be easily used as commands
     * 
     * We could also consider writing methods in subsystems that return commands, wpilib has some examples like this
     */
    private void configureBindings() {

        //Driver Commands
        m_driverController.rightBumper().onTrue(Commands.runOnce(() -> m_robotDrive.toggleFieldOriented()));
        m_driverController.povUp().onTrue(Commands.runOnce(() -> m_robotDrive.restrictDriving(true)));
        m_driverController.povUp().onFalse(Commands.runOnce(() -> m_robotDrive.restrictDriving(false)));
        m_driverController.a().onTrue(Commands.runOnce(() -> m_robotPath.followpath(true)));
        m_driverController.b().onTrue(Commands.runOnce(() -> m_robotPath.followpath(false)));


        // NEW 2026 Mechanism Controls below
        /* 
            ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
                Runnable task = () -> Commands.runOnce(() -> m_TransferSubsystem.Kicker());
                Runnable task1 = () -> Commands.runOnce(() -> m_TransferSubsystem.BedRoller());

         m_mechanismController.a().onTrue(Commands.runOnce(()-> {

                .withTimeout(3000)
                scheduler.schedule(task, 3, TimeUnit.SECONDS);
                scheduler.schedule(task1, 3, TimeUnit.SECONDS);
                m_shootingMechanism.Shooting();
        }));
        */
         m_mechanismController.x().onTrue(Commands.runOnce(() -> m_IntakeSubsystem.Intake()));
         m_mechanismController.b().onTrue(Commands.runOnce(() -> m_TransferSubsystem.BedRoller()));
        // m_mechanismController.b().onTrue(Commands.runOnce(() -> m_TransferSubsystem.BedRoller2()));
        // m_mechanismController.b().onTrue(Commands.runOnce(() -> m_TransferSubsystem.test()));
        // m_mechanismController.y().onTrue(Commands.runOnce(() -> m_shootingMechanism.Shooting()));
          m_driverController.y().whileTrue(Commands.run(
            () -> {
                boolean isAtSpeed = m_ShootingSubsystem.isAtSpeed(-3200);
                m_ShootingSubsystem.setShooterSpeed(-3200, false);
                m_ShootingSubsystem.setFiring(isAtSpeed);
            }, m_ShootingSubsystem));
            m_driverController.povDown().whileTrue(Commands.run(
            () -> {
                m_ShootingSubsystem.setShooterSpeed(500, false);
            }, m_ShootingSubsystem));

         m_mechanismController.a().onTrue(Commands.runOnce(() -> m_ShootingSubsystem.Kicker()));

        opticalTrigger.onFalse(new SequentialCommandGroup(Commands.waitSeconds(0.2), Commands.runOnce(() -> m_robotMechanisms.stopCoral())));
    }

    /**
     * Sets up mechanism command groups to be used in path planner
     * these must all be registered as Named Commands
     */

     /* 
    private void configureAutoCommands() {
        //Remember Commands.waitSeconds() is a thing
        NamedCommands.registerCommand("Trough", new SequentialCommandGroup(Commands.runOnce(() -> m_robotElevator.trough()), Commands.waitSeconds(3)));
        NamedCommands.registerCommand("Low", new SequentialCommandGroup(Commands.runOnce(() -> m_robotElevator.low()), Commands.waitSeconds(3)));
        NamedCommands.registerCommand("Middle", new SequentialCommandGroup(Commands.runOnce(() -> m_robotElevator.middle()), Commands.waitSeconds(3)));
        NamedCommands.registerCommand("High", new SequentialCommandGroup(Commands.runOnce(() -> m_robotElevator.high()), Commands.waitSeconds(3)));
        NamedCommands.registerCommand("Run Coral", Commands.runOnce(() -> m_robotMechanisms.slowCoral()));
        NamedCommands.registerCommand("Run Coral Fast", Commands.runOnce (() -> m_robotMechanisms.fastCoral()));
        NamedCommands.registerCommand("Stop Coral", Commands.runOnce(() -> m_robotMechanisms.stopCoral()));
        NamedCommands.registerCommand("Align Right", Commands.runOnce(() -> m_robotPath.followpath(false)));
        NamedCommands.registerCommand("Align Left", Commands.runOnce(() -> m_robotPath.followpath(true)));
    */
    private void configureAutoCommands() {
        NamedCommands.registerCommand("Shoot", new SequentialCommandGroup(Commands.runOnce(() -> m_ShootingSubsystem.Shooting())));
        NamedCommands.registerCommand("StopShooting", new SequentialCommandGroup(Commands.runOnce(() -> m_ShootingSubsystem.stopShooting())));


    }
        


    

    private void configureSmartDashboard() {
        SmartDashboard.putData("Auto choices", m_chooser);
        //SmartDashboard.putData("Angle choices", Anglechooser);

        m_chooser.addOption("Test", new PathPlannerAuto("Test Auto"));
        m_chooser.setDefaultOption("Test", new PathPlannerAuto("Test Auto"));

        m_chooser.addOption("Middle blue top left", new PathPlannerAuto("Middle Blue Barge to Left Top Reef"));
        m_chooser.addOption("One Coral Straight", new PathPlannerAuto("One Coral Straight"));
        m_chooser.addOption("Close Top", new PathPlannerAuto("Close Top"));
        m_chooser.addOption("Bottom Close", new PathPlannerAuto("Bottom Close"));
        m_chooser.addOption("Shoot", new PathPlannerAuto("Shoot"));

    }


   /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
    public Command getAutonomousCommand() {
        return new SequentialCommandGroup(Commands.waitSeconds(0.5), m_chooser.getSelected());
    }

    public void printOutput(){
       
    }
}
