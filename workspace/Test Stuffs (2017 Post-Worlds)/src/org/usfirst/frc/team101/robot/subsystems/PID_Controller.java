package org.usfirst.frc.team101.robot.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class PID_Controller extends PIDController {

	public PID_Controller(double Kp, double Ki, double Kd, double Kf, PIDSource encoder, PIDOutput output) {
		super(Kp, Ki, Kd, Kf, encoder, output);
		// TODO Auto-generated constructor stub
	}


}
