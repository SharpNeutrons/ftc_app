package org.firstinspires.ftc.teamcode.RobotCommands;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareClasses.ServoWrapper;

public class MoveServoToPosition implements RobotCommand {

	private ServoWrapper servo;
	private double targetPos;

	public MoveServoToPosition(ServoWrapper servo, double targetPos) {
		this.servo = servo;
		this.targetPos = targetPos;
	}

	@Override
	public void runCommand () {
		servo.autoControlActive = true;
		servo.setPositionOverride(targetPos);
	}

	@Override
	public boolean commandLoop (LinearOpMode opMode) {
		return false;
	}

	@Override
	public void endCommand () {servo.autoControlActive = false;}

}
