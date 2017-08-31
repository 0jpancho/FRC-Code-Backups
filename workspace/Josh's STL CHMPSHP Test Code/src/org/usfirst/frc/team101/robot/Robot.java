
package org.usfirst.frc.team101.robot;

import java.util.ArrayList;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
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
	
	PowerDistributionPanel pdp = new PowerDistributionPanel(0);
	BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();
	
	Talon motorLeft;
	Talon motorRight;
	
	/*Spark shooter;
	Relay feeder;
	Timer shooterTimer;
	boolean shooterThingy = false;*/
	
	Victor hangerOne;
	Victor hangerTwo;
	
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
	
	SendableChooser<String> colorChooser = new SendableChooser<String>();
	SendableChooser<String> positionChooser = new SendableChooser<String>();
	SendableChooser<String> functionChooser = new SendableChooser<String>();
	
	String color;
	String position;
	String function;
	
	final double FEET_PER_SECOND = 8.375;
	final double SECONDS_PER_FOOT = 1 / FEET_PER_SECOND;
	double currentThingy = 0;
	double nextThingy = 0;
	double power = 0;
	
	//double motorOutPos = 0.75;
	//double motorOutNeg = -0.75;
	
	double leftMotorSpeed = 0;
	double leftMotorSpeedPrev = 0;	
	
	double rightMotorSpeed = 0;
	double rightMotorSpeedPrev = 0;
	
	//double iLeft = 0;
	//double iRight = 0;
	
	//double diffLeft = 0;
	//double diffRight = 0;
	
	CANTalon YAY;
	boolean CANTalonBrakeMode;
	boolean CANTalonPressed;
	
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
		
		//This works, I promise (DON'T TOUCH THIS)
		motorLeft = new Talon(1);
		motorRight = new Talon(0);
		
		/*shooter = new Spark(2);
		feeder = new Relay(0);
		shooterTimer = new Timer();*/
		
		autonTimer = new Timer();
		
		hangerOne = new Victor(3);
		hangerTwo = new Victor(4);
		
		compressor = new Compressor();
		gearThingy = new DoubleSolenoid(0, 1);
		//compressor.start();
		
 		driveOne = new Joystick(0);
		driveTwo = new Joystick(1);
		operator = new Joystick(2);
		
		timer = new Timer();
		gyroscope = new ADXRS450_Gyro();
		
		//CameraServer server = CameraServer.getInstance();
		//server.startAutomaticCapture();
		
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("colorChooser", colorChooser);
		SmartDashboard.putData("positionChooser", positionChooser);
		SmartDashboard.putData("functionChooser", functionChooser);
		
		//sdTable.putDouble("shooterPower", 0);
		
		leftMotorSpeed = 0;
		leftMotorSpeedPrev = 0;
		
		rightMotorSpeed = 0;
		rightMotorSpeedPrev = 0;
		
		//iLeft = 0;
		//iRight = 0;
		
		//diffLeft = 0;
		//diffRight = 0;
		
		YAY = new CANTalon(0);
	}
	
	/**
	 * This functionChooser is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		
		leftMotorSpeed = 0;
		rightMotorSpeed = 0;
		
		leftMotorSpeedPrev = 0;
		rightMotorSpeedPrev = 0;
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		
		SmartDashboard.putDouble("Gyro", gyroscope.getAngle());
		SmartDashboard.putBoolean("Gear", (gearThingy.get() == DoubleSolenoid.Value.kForward ? true : false));
		SmartDashboard.putDouble("Acceleration", accelerometer.getY());
		
		SmartDashboard.putDouble("PDP Voltage", pdp.getVoltage());
		
		leftMotorSpeed = 0;
		rightMotorSpeed = 0;
		
		leftMotorSpeedPrev = 0;
		rightMotorSpeedPrev = 0;
		
		
		
		timer.stop();
		timer.reset();
		
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
		
		YAY.enableBrakeMode(true);
		CANTalonBrakeMode = true;
		
		
		color = colorChooser.getSelected();
		position = positionChooser.getSelected();
		function = functionChooser.getSelected();
		
		autonTimer.stop();
		autonTimer.reset();
		autonTimer.start();
		
		gyroscope.reset();
			
		if(color.equals("red") && position.equals("middle"))
		{
			driveDistance(13.5, 0.35, true);
			stopMotors(1);
			gearThingy.set(DoubleSolenoid.Value.kReverse);
			stopMotors(1);
			driveDistance(2, 0.5, false);
			/*turnAngle(90, 0.25, true);
			driveDistance(4, 0.5, true);
			turnAngle(0, 0.25, false);
			driveDistance(6, 0.5, true);
			stopMotors(1); */
		}
		
		else if(color.equals("red") && position.equals("left"))
		{
			//driveDistance (8, 0.6, true);
			//stopMotors(1);
			motorLeft.set(1);
		}
		
		else if(color.equals("red") && position.equals("right"))
		{
			driveDistance(6, 0.6, true);
			stopMotors(1);
		}
		
		else if(color.equals("blue") && position.equals("middle"))
		{
			driveDistance(13.5, 0.35, true);
			stopMotors(1);
			gearThingy.set(DoubleSolenoid.Value.kReverse);
			stopMotors(1);
			driveDistance(2, 0.5, false);
			/*turnAngle(270, 0.25, false);
			driveDistance(4, 0.5, true);
			turnAngle(0, 0.25, false);
			driveDistance(6, 0.5, true);
			stopMotors(1); */
		}
		
		else if(color.equals("blue") && position.equals("left"))
		{
			driveDistance(6, 0.6, true);
			stopMotors(1);
		}
		
		else if(color.equals("blue") && position.equals("right"))
		{
			driveDistance(6, 0.6, true);
			stopMotors(1);
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
		
		YAY.enableBrakeMode(false);
		CANTalonBrakeMode = false;
	}

	/**
	 * This functionChooser is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		SmartDashboard.putDouble("Gyro", gyroscope.getAngle());
		SmartDashboard.putBoolean("Gear", (gearThingy.get() == DoubleSolenoid.Value.kForward ? true : false));
		
		SmartDashboard.putDouble("Left Motors", motorLeft.get());
		SmartDashboard.putDouble("Right Motors", motorLeft.get());
		
		SmartDashboard.putDouble("PDP Voltage", pdp.getVoltage());
		
		
		//System.out.println("TestTimer: " + timer.get());
		//System.out.println("OtherTimer: " + Timer.getFPGATimestamp());
		
		leftMotorSpeed = driveTwo.getY();
		rightMotorSpeed = -driveOne.getY();
		
		if(driveOne.getTrigger() && driveTwo.getTrigger())
		{
			if((motorLeft.get() > leftMotorSpeed) && (motorLeft.get() - leftMotorSpeed) > 0.1)
			{
				//Subtract 10% of the difference from left motor (Forward)
				leftMotorSpeedPrev = motorLeft.get();
				
				System.out.println("Left faster: " + motorLeft.get());
				
				motorLeft.set(motorLeft.get() - (motorLeft.get() - leftMotorSpeed) * 0.1);
				
				System.out.println("Left faster: " + motorLeft.get());
			
				if(motorLeft.get() < leftMotorSpeed && (motorLeft.get() - leftMotorSpeed) > 0.1)
				{
					motorLeft.set(0);
					
					System.out.println("Left Faster Stop: " + motorLeft.get());
				}
			}
			
			//If current left motor speed is more than 0.1 below target (Backwards)
			else if(motorLeft.get() < leftMotorSpeed && (motorLeft.get() + leftMotorSpeed) > 0.1)
			{
				//Add 10% of the difference to left motor
				leftMotorSpeedPrev = motorLeft.get();
				
				System.out.println("Left slower: " + motorLeft.get());
				
				motorLeft.set(motorLeft.get() + (motorLeft.get() + leftMotorSpeed) * 0.1);
				
				System.out.println("Left slower: " + motorLeft.get());
				
				if(Math.abs(motorLeft.get()) < 0.1 && Math.abs(leftMotorSpeedPrev) >= 0.1)
				{
					motorLeft.set(0);
					
					System.out.println("Left Slower Stop: " + motorLeft.get());
				}
			} 
			
			//If current right motor speed is more than 0.1 above target (Forwards)
			if(motorRight.get() > rightMotorSpeed && (motorRight.get() - rightMotorSpeed) > 0.1)
			{
				//Subtract 10% of the difference from right motor
				rightMotorSpeedPrev = motorRight.get();
				
				System.out.println("Right: " + motorRight.get());
				
				motorRight.set(motorRight.get() - (motorRight.get() - rightMotorSpeed) * 0.1);
				
				System.out.println("Right faster: " + motorRight.get());
				
				
				if(Math.abs(motorRight.get()) < 0.1 && Math.abs(rightMotorSpeedPrev) >= 0.1)
				{
					motorRight.set(0);
					System.out.println("Right Faster Stop: " + motorRight.get());
				}
			}
			
			//If current right motor speed is more than 0.1 below target (Backwards)
			else if(motorRight.get() < rightMotorSpeed && (motorRight.get() + rightMotorSpeed) > 0.1)
			{
				//Add 10% of the difference to right motor
				rightMotorSpeedPrev = motorRight.get();
				
				System.out.println("Right slower: " + motorRight.get());
				
				motorRight.set(motorRight.get() + (motorRight.get() + rightMotorSpeed) * 0.1);
				
				System.out.println("Right slower: " + motorRight.get());
				
				if(Math.abs(motorRight.get()) < 0.1 && Math.abs(rightMotorSpeedPrev) >= 0.1)
				{
					motorRight.set(0);
					System.out.println("Right Faster Stop: " + motorRight.get());
				}
			}		
		}

		else
		{
			motorLeft.set(driveTwo.getY());
			motorRight.set(-driveOne.getY());
		}

	
		if(operator.getRawButton(6))
		{
			compressor.start();
		}
		else
		{
			compressor.stop();
		}
		
		
		
	
					
		//If current left motor speed is more than 0.1 above target
		//If current is different from previous, reset the counter
		
		/*
		if(leftMotorSpeed != leftMotorSpeedPrev)
		{
			iLeft = 0;
			System.out.println("leftMotorSpeed: " + leftMotorSpeed);
			System.out.println("leftMotorSpeedPrev: " + leftMotorSpeedPrev);
			
			System.out.println("Yes" + leftMotorSpeed);
		}
		
		
		
		if(rightMotorSpeed != rightMotorSpeedPrev)
		{
			iRight = 0;
			System.out.println("Second IF");
		}
		
		
		
		//Get difference between desired speed and actual
		if(iLeft == 0)
		{
			diffLeft = leftMotorSpeed - motorLeft.get();
			System.out.println("Third IF");
		}
		
		if(iRight == 0)
		{
			diffRight = rightMotorSpeed - motorRight.get();
			System.out.println("Fourth IF");
		}
			
		//Correct difference over five loop iterations
		if(iLeft < 5)
		{
			if(diffLeft < 0)
			{
				motorLeft.set(motorLeft.get() - diffLeft / 5);
				System.out.println("Fifth IF");
			}
			
			else if(diffLeft > 0)
			{
				motorLeft.set(motorLeft.get() + diffLeft / 5);
				System.out.println("First ELSE");
			}
			iLeft++;
		}
		
		if(iRight < 5)
		{
			if(diffRight < 0)
			{
				motorRight.set(motorRight.get() - diffRight / 5);
				System.out.println("Sixth IF");
			}
			
			
			else if(diffRight > 0)
			{
				motorRight.set(motorRight.get() + diffRight / 5);
				System.out.println("Second ELSE");
			}
			
			iRight++;
		}
		*/
		/*if(driveOne.getY() > motorOutPos)
		{
			motorLeft.set(0.75);
		}
		
		if(driveOne.getY() < motorOutNeg)
		{
			motorLeft.set(0.75);
		}
		
		if(driveTwo.getY() > motorOutPos)
		{
			motorRight.set(0.75);
		}
		
		if(driveTwo.getY() < motorOutNeg)
		{
			motorRight.set(0.75);
		}*/
		
	//	if(driveOne.getTrigger() && driveTwo.getTrigger())
	//	{
				
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
		
	/*if(operator.getRawButton(3) && !shooterThingy)	
	{
		shooterThingy = true;
	]	shooterTimer.start();
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
	}*/
	
	if(operator.getRawButton(2))
	{
		hangerOne.set(1);
		hangerTwo.set(1);
	}
	else
	{
		hangerOne.set(0);
		hangerTwo.set(0);
	}
	
		
		
	
	//System.out.println("Gyro reading: " + gyroscope.getAngle());
	
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
				
				System.out.println("FOWADS A THIGN");
			}
			
			else
			{
				motorLeft.set(power);
				motorRight.set(-power);
				
				System.out.println("BACKWADS WARKS");
			}
		}
		motorLeft.set(0);
		motorRight.set(0);
		
		System.out.println("Driving dids a thing");
	}
	
	/*public void turnAngle(double angle, double speed, boolean reset)
	{
		
		if(reset)
			gyroscope.reset();
		//Left
		while (gyroscope.getAngle() < angle)
		{
			motorLeft.set(speed);
			motorRight.set(speed);
		}
		//Right
		while (gyroscope.getAngle() >  angle)
		{
			motorLeft.set(-speed);
			motorRight.set(-speed);
		}

		motorLeft.set(0);
		motorRight.set(0);
		
	}*/
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
			
			System.out.println("ITS STAPZ");
		}
		
	}
	/*public void shooting(double seconds)
	{
		Timer aTimer = new Timer();
		aTimer.start();
		
		currentThingy = aTimer.get();
		nextThingy = currentThingy + seconds;
		
		while(aTimer.get() >= currentThingy && aTimer.get() < nextThingy)
		{
			shooter.set(SmartDashboard.getDouble("shooterPower"));
			
			if(aTimer.get() > 0.5)
			{
				feeder.set(Relay.Value.kReverse);
			}
		}
	}*/
}
