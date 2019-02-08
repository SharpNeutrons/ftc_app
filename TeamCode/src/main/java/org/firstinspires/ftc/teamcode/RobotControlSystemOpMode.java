package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareClasses.RobotHardware;

public class RobotControlSystemOpMode extends LinearOpMode {

	CommandQueue commandQueue;
	RobotHardware robot;

	@Override
	public void runOpMode () {
		commandQueue = new CommandQueue(this);
		robot = new RobotHardware();
		robot.init(this);

		//TODO imu start
		waitForStart();
	}

	void opModeLoop () {
		commandQueue.commandLoop();
	}

}
