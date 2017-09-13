package org.usfirst.frc.team101.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	CANTalon frontLeft;
	CANTalon centerLeft;
	CANTalon backLeft;
	
	CANTalon frontRight;
	CANTalon centerRight;
	CANTalon backRight;
	
	ADXRS450_Gyro gyroscope;
	
	final double FEET_PER_SECOND = 8.375;
	final double SECONDS_PER_FOOT = 1 / FEET_PER_SECOND;
	double currentThingy = 0;
	double nextThingy = 0;
	double power = 0;
	
	Joystick driveOne;
	Joystick driveTwo;
	
	SendableChooser<String> functionChooser = new SendableChooser<String>();
	
	String function;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		frontLeft = new CANTalon(0);
		centerLeft = new CANTalon(1);
		backLeft = new CANTalon(2);
		
		frontRight = new CANTalon(3);
		centerRight = new CANTalon(4);
		backRight = new CANTalon(5);
		
		gyroscope = new ADXRS450_Gyro();
		
		driveOne = new Joystick(0);
		driveTwo = new Joystick(1);
		
		functionChooser.addObject("turn", "turn");
		functionChooser.addObject("drive", "drive");
		functionChooser.addObject("drive and turn", "drive and turn");
		
		SmartDashboard.putData("Function", functionChooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
		function = functionChooser.getSelected();
		
		if(function.equals("drive"))
		{
			driveDistance(10, 0.10, true);
			turnAngle(180, 0.10, true);
			stopMotors(1);
		}
	}
		

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		frontLeft.set(driveOne.getY());
		centerLeft.set(driveOne.getY());
		backLeft.set(driveOne.getY());
		
		frontRight.set(driveTwo.getY());
		centerLeft.set(driveTwo.getY());
		backLeft.set(driveTwo.getY());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
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
					frontLeft.set(power);
					centerLeft.set(power);
					backLeft.set(power);
					
					frontRight.set(-power);
					centerLeft.set(-power);
					backLeft.set(-power);
					
					System.out.println("FOWADS A THIGN");
				}
		
				else
				{
					frontLeft.set(-power);
					centerLeft.set(-power);
					backLeft.set(-power);
					
					frontRight.set(power);
					centerLeft.set(power);
					backLeft.set(power);
					
					System.out.println("BACKWADS WARKS");
				}
			}
			
			frontLeft.set(0);
			centerLeft.set(0);
			backLeft.set(0);
			
			frontRight.set(0);
			centerLeft.set(0);
			backLeft.set(0);
	
			System.out.println("Driving dids a thing");
	}
	
	public void turnAngle(double angle, double speed, boolean reset)
	{
		
		if(reset)
			gyroscope.reset();
		//Left
		while (gyroscope.getAngle() < angle)
		{
			frontLeft.set(speed);
			centerLeft.set(speed);
			backLeft.set(speed);
			
			frontRight.set(speed);
			centerLeft.set(speed);
			backLeft.set(speed);
		}
		//Right
		while (gyroscope.getAngle() >  angle)
		{
			frontLeft.set(-power);
			centerLeft.set(-power);
			backLeft.set(-power);
			
			frontRight.set(-power);
			centerLeft.set(-power);
			backLeft.set(-power);
		}

		frontLeft.set(0);
		centerLeft.set(0);
		backLeft.set(0);
		
		frontRight.set(0);
		centerLeft.set(0);
		backLeft.set(0);
	}
	
	public void stopMotors(double seconds)
	{
		Timer aTimer = new Timer();
		aTimer.start();
		
		currentThingy = aTimer.get();
		nextThingy = currentThingy + seconds;
		
		while(aTimer.get() >= currentThingy && aTimer.get() < nextThingy)
		{
			frontLeft.set(0);
			centerLeft.set(0);
			backLeft.set(0);
			
			frontRight.set(0);
			centerLeft.set(0);
			backLeft.set(0);
			
			System.out.println("ITS STAPZ");
		}
	}
}
