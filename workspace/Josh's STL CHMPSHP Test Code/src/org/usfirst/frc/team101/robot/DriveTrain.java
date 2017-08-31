package org.usfirst.frc.team101.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TalonSRX;

public class DriveTrain 
{
		TalonSRX left;
		TalonSRX right;
		
		public DriveTrain(int leftPort, int rightPort)
		{
			left = new TalonSRX(leftPort);
			right = new TalonSRX(rightPort);
		}
		
		public void tankDrive(GenericHID leftStick, GenericHID rightStick)
		{
			
		}
	
	
}
