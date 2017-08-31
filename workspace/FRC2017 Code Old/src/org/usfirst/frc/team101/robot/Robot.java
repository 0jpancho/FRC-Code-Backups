
package org.usfirst.frc.team101.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functionChoosers corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	TalonSRX motorLeft;
	TalonSRX motorRight;
	
	Spark shooter;
	Relay feeder;
	Timer shooterTimer;
	boolean shooterThingy = false;
	
	Compressor compressor;
	DoubleSolenoid gearThingy;
	boolean gearTogglePressed = false;
	
	Joystick driveOne; 
	Joystick driveTwo;
	Joystick operator;
	
	Timer timer; 
	ADXRS450_Gyro gyroscope;
	
	Timer autonTimer;
	
	NetworkTable sdTable = NetworkTable.getTable("SmartDashboard");
	
	SendableChooser<String> colorChooser = new SendableChooser();
	SendableChooser<String> positionChooser = new SendableChooser();
	SendableChooser<String> functionChooser = new SendableChooser();
	
	String color;
	String position;
	String function;
	
	final double FEET_PER_SECOND = 8.375;
	final double SECONDS_PER_FOOT = 1 / FEET_PER_SECOND;
	double currentThingy = 0;
	double nextThingy = 0;
	double power = 0;
	
	/**
	 * This functionChooser is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		colorChooser.addObject("Red", "red");
		colorChooser.addObject("Blue", "blue");
		
		positionChooser.addObject("Left", "left");
		positionChooser.addObject("Middle", "middle");
		positionChooser.addObject("Right", "right");
		
		functionChooser.addObject("", "");
		
		motorLeft = new TalonSRX(1);
		motorRight = new TalonSRX(0);
		
		shooter = new Spark(2);
		feeder = new Relay(0);
		shooterTimer = new Timer();
		autonTimer = new Timer();
		
		compressor = new Compressor();
		gearThingy = new DoubleSolenoid(0, 1);
		compressor.start();
		
 		driveOne = new Joystick(0);
		driveTwo = new Joystick(1);
		operator = new Joystick(2);
		
		timer = new Timer();
		gyroscope = new ADXRS450_Gyro();
		
		CameraServer server = CameraServer.getInstance();
		server.startAutomaticCapture();
		
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("colorChooser", colorChooser);
		SmartDashboard.putData("positionChooser", positionChooser);
		SmartDashboard.putData("functionChooser", functionChooser);
		
		sdTable.putDouble("shooterPower", 0);
		
	}
	
	/**
	 * This functionChooser is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putDouble("Gyro", gyroscope.getAngle());
		SmartDashboard.putBoolean("Gear", (gearThingy.get() == DoubleSolenoid.Value.kForward ? true : false));
		
		timer.stop();
		timer.reset();
		
		SmartDashboard.putBoolean("Gear", (gearThingy.get() == DoubleSolenoid.Value.kForward ? true : false));
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		
		color = colorChooser.getSelected();
		position = positionChooser.getSelected();
		function = functionChooser.getSelected();
		
		autonTimer.stop();
		autonTimer.reset();
		autonTimer.start();
		
		gyroscope.reset();
			
		if(color.equals("red") && position.equals("middle"))
		{
			/*power = 0.725;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 4 * SECONDS_PER_FOOT / power;
			
			while(autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(-power);
				motorRight.set(power);
			}
				*/
			driveDistance(4, 0.725, true);
			stopMotors(1);
			gearThingy.set(DoubleSolenoid.Value.kReverse);
			driveDistance(2, 0.5, false);
			turnAngle(90, 0.25, true);
			driveDistance(4, 0.5, true);
			turnAngle(0, 0.25, false);
			driveDistance(6, 0.5, true);
			stopMotors(1);
			/*
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 1;
			
			power = 0;
			
			while(autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(power);
				motorRight.set(power);
			}
			
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 1;
			
			while(autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				gearThingy.set(DoubleSolenoid.Value.kReverse);	
			}
		
			power = 0.25;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 2 * SECONDS_PER_FOOT / power;
			
			while (autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(power);
				motorRight.set(-power);
			}
			
			power = 0;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 1;
			
			while (autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(-power);
				motorRight.set(power);
			}
			
			power = 0.25;
			
			while (gyroscope.getAngle() >= 0 && gyroscope.getAngle() < 90)
			{
				motorLeft.set(-power);
				motorRight.set(-power);
				
				System.out.println("Less Possibly Turning: " + gyroscope.getAngle());
			}
			
			power = 0;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 1;
			
			while (autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(0);
				motorRight.set(0);
			}
		
			power = 0.5;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 2 * SECONDS_PER_FOOT / power;
			
			while (autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(-power);
				motorRight.set(power);
			}
			
			//driveDistance(3, 1);
			
			power = 0.25;
			System.out.println("Gyroscope: " + gyroscope.getAngle());
			
			while (gyroscope.getAngle() <= 90 && gyroscope.getAngle() > 0)
			{
				motorLeft.set(power);
				motorRight.set(power);
				
				System.out.println("Turning Possibly: " + gyroscope.getAngle());
			}
			
			power = 0.5;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 1 * SECONDS_PER_FOOT / power;
			
			while (autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(-power);
				motorRight.set(power);
			}
			power = 0;
			currentThingy = autonTimer.get();
			nextThingy = currentThingy + 1;
			
			while (autonTimer.get() >= currentThingy && autonTimer.get() < nextThingy)
			{
				motorLeft.set(power);
				motorRight.set(power);
			}*/
		}
		
		else if(color.equals("red") && position.equals("left"))
		{
			
		}
		
		else if(color.equals("red") && position.equals("right"))
		{
			
		}
		
		else if(color.equals("blue") && position.equals("middle"))
		{
			
		}
		
		else if(color.equals("blue") && position.equals("left"))
		{
			
		}
		
		else if(color.equals("blue") && position.equals("right"))
		{
			
		}
		
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		
	/*	timer.stop();
		timer.reset();
		timer.start();
		
		while(timer.get() < 2)
		{
			motorLeft.set(-0.50);
			motorRight.set(0.60);
			System.out.println("Gyro reading: " + gyroscope.getAngle());
			
		}
		
			motorLeft.set(0);
			motorRight.set(0);*/
	}
	

	/**`
	 * This functionChooser is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
	
		timer.start();
	}

	/**
	 * This functionChooser is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putDouble("Gyro", gyroscope.getAngle());
		SmartDashboard.putBoolean("Gear", (gearThingy.get() == DoubleSolenoid.Value.kForward ? true : false));
		
		System.out.println("TestTimer: " + timer.get());
		System.out.println("OtherTimer: " + Timer.getFPGATimestamp());
		
	//	if(driveOne.getTrigger() && driveTwo.getTrigger())
	//	{
			motorLeft.set(driveTwo.getY());
			motorRight.set(-driveOne.getY());				
	//	}
		
		/*else if(driveOne.getTrigger())
		{
			motorLeft.set(-driveOne.getX() - driveOne.getY());
			motorRight.set(-driveOne.getX() + driveOne.getY());
		}
		
		else
		{	
			motorLeft.set(0);
			motorRight.set(0);
		}
		*/
		if(operator.getTrigger() && !gearTogglePressed)	
		{
			gearTogglePressed = true;
			
			if(gearThingy.get() == DoubleSolenoid.Value.kOff || gearThingy.get() == DoubleSolenoid.Value.kReverse)
			{
				gearThingy.set(DoubleSolenoid.Value.kForward);
			}
			
			else if(gearThingy.get() == DoubleSolenoid.Value.kForward)
			{
				gearThingy.set(DoubleSolenoid.Value.kReverse);
			}
		}
		else if(!operator.getTrigger())
		{
			gearTogglePressed = false;
		}
	
		/*if(operator.getRawButton(3))
	{
		shooter.set(0.85);
		feeder.set(Value.kReverse);
	}
	else
	{
		shooter.set(0);
		feeder.set(Value.kOff);
	}
	*/
		
	if(operator.getRawButton(3) && !shooterThingy)	
	{
		shooterThingy = true;
		shooterTimer.start();
		shooter.set(sdTable.getDouble("shooterPower"));
	}
	
	else if(operator.getRawButton(3) && shooterTimer.get() > 1.5)
	{
		feeder.set(Value.kReverse);
	}
	else if(!operator.getRawButton(3))	
	{
		shooterTimer.stop();
		shooterTimer.reset();
		shooter.set(0);
		feeder.set(Value.kOff);
		shooterThingy = false; 
	}
	
	System.out.println("Gyro reading: " + gyroscope.getAngle());
	
	}
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	public void driveDistance(double distance, double power, boolean forward)
	{
		Timer aTimer = new Timer();
		aTimer.start();
		
		currentThingy = aTimer.get();
		nextThingy = currentThingy + distance * SECONDS_PER_FOOT / power;
		
		while (aTimer.get() >= currentThingy && aTimer.get() < nextThingy)
		{
			
			if(forward)
			{
				motorLeft.set(-power);
				motorRight.set(power);
			}
			
			else
			{
				motorLeft.set(power);
				motorRight.set(-power);
			}
		}
		motorLeft.set(0);
		motorRight.set(0);
	}
	
	public void turnAngle(double angle, double speed, boolean reset)
	{
		
		if(reset)
			gyroscope.reset();
		/*Left*/
		while (gyroscope.getAngle() <  angle)
		{
			motorLeft.set(speed);
			motorRight.set(speed);
		}
		/*Right*/
		while (gyroscope.getAngle() >  angle)
		{
			motorLeft.set(-speed);
			motorRight.set(-speed);
		}

		motorLeft.set(0);
		motorRight.set(0);

	}
	public void stopMotors(double seconds)
	{
		Timer aTimer = new Timer();
		aTimer.start();
		
		currentThingy = aTimer.get();
		nextThingy = currentThingy + seconds;
		
		while(aTimer.get() >= currentThingy && aTimer.get() < nextThingy)
		{
			motorLeft.set(0);
			motorRight.set(0);
		}
	}
	
	
}