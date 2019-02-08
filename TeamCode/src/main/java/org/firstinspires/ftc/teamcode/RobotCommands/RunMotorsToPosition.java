package org.firstinspires.ftc.teamcode.RobotCommands;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareClasses.MotorWrapper;

/**
 * This class takes a motor array and moves them all by a given amount of counts
 * useful for drivetrains when more than one motor has to move at the same time to the same position
 */
public class RunMotorsToPosition implements RobotCommand {

	private MotorWrapper[] motors;
	private int deltaCounts;
	private double speed;
	private boolean shouldTimeout;
	private double timeoutMillis;
	private ElapsedTime elapsedTime = new ElapsedTime();

	public RunMotorsToPosition(MotorWrapper[] motors, int deltaCounts, double speed) {
		this.motors = motors;
		this.deltaCounts = deltaCounts;
		this.speed = speed;
		this.shouldTimeout = false;
	}

	public RunMotorsToPosition(MotorWrapper[] motors, int deltaCounts, double speed, double timeoutMillis) {
		this.motors = motors;
		this.deltaCounts = deltaCounts;
		this.speed = speed;
		this.shouldTimeout = true;
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public void runCommand () {
		for (MotorWrapper motor:motors) {
			motor.setTargetPostion(motor.getCurrentPosition() + deltaCounts);
			motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			motor.setPowerOverride(speed);
		}
		if (shouldTimeout) {
			elapsedTime.reset();
		}
	}

	/**
	 * The method will return false
	 * @param opMode used to reference if opmode is still running and to give telemetry
	 * @return
	 */
	@Override
	public boolean commandLoop (LinearOpMode opMode) {
		int motorsBusy = 0;
		if (opMode.opModeIsActive()) {
			for (MotorWrapper motor : motors) {
				if (motor.isBusy()) {
					motorsBusy++;
				}
			}
			//if more than half of the motors are still busy, return what the timeout says
			//if more than half are done, will return false
			if ((motorsBusy / motors.length) >= 0.5) {
				return elapsedTime.milliseconds() < timeoutMillis || !shouldTimeout;
			}
		}
		return false;
	}

	@Override
	public void endCommand () {
		for (MotorWrapper motor:motors) {
			motor.setPowerOverride(0);
			motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}

}
