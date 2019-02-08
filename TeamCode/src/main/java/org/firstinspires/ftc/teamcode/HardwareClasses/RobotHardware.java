package org.firstinspires.ftc.teamcode.HardwareClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RobotHardware {

	public DriveTrain driveTrain;
	public Hangervator hangervator;

	public static final int HD_40_COUNTS_PER_REV = 2240;
	public static final int HD_20_COUNTS_PER_REV = 1120;

	public RobotHardware () {}

	public void init (LinearOpMode opMode) {
		driveTrain = new DriveTrain(opMode);
		hangervator = new Hangervator(opMode);
	}
}
