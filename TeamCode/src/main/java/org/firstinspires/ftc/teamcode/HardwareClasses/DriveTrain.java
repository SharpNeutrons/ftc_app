package org.firstinspires.ftc.teamcode.HardwareClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.HardwareClasses.MotorWrapper;
import org.firstinspires.ftc.teamcode.RobotCommands.RunMotorsToPosition;

public class DriveTrain {

	private MotorWrapper leftDrive;
	private MotorWrapper rightDrive;

	private final int COUNTS_PER_REV = 1120;
	private final double WHEEL_RADIUS = 50.8;
	private final int COUNTS_PER_MM = (int)(COUNTS_PER_REV/ (WHEEL_RADIUS * 2 * Math.PI));


	public DriveTrain (LinearOpMode opMode) {
		leftDrive = new MotorWrapper(opMode.hardwareMap.get(DcMotor.class, "leftDrive"));
		rightDrive = new MotorWrapper(opMode.hardwareMap.get(DcMotor.class, "rightDrive"));

		leftDrive.init();
		rightDrive.init();

		rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
	}

	/**
	 *
	 * @param speed + for forward
	 * @param turn + for clockwise
	 */
	public void controlOverride(double speed, double turn) {
		double leftPow = speed + turn;
		double rightPow = speed - turn;

		//this makes sure the powers for both are less than one
		//the abs values make sure the signs are conserved
		if (Math.abs(leftPow) > 1 || Math.abs(rightPow) > 1) {
			if (Math.abs(leftPow) > Math.abs(rightPow)) {
				leftPow = Math.signum(leftPow);
				rightPow /= Math.abs(leftPow);
			}else {
				rightPow = Math.signum(rightPow);
				leftPow /= Math.abs(rightPow);
			}
		}
		leftDrive.setPowerOverride(leftPow);
		rightDrive.setPowerOverride(rightPow);
	}

	public double getTurnSpeed () {
		return (leftDrive.getPower() - rightDrive.getPower())/2;
	}

	/**
	 * Note this does not actually execute the command, it just returns it so it can be used later
	 * @param mm how many millimeters the robot should drive
	 * @param speed the speed the motor should run with
	 * @return the execute command, used by adding to a SynchronousCommandQueue
	 */
	public RunMotorsToPosition driveDistanceMM (double mm, double speed) {
		int counts = (int)(COUNTS_PER_MM * mm);
		return new RunMotorsToPosition(
				new MotorWrapper[]{leftDrive, rightDrive}, counts, speed);
	}

	/**
	 * Note this does not actually execute the command, it just returns it so it can be used later
	 * @param mm how many millimeters the robot should drive
	 * @param speed the speed the motor should run with
	 * @param timeout how long the command should run if the robot can't move
	 * @return the execute command, used by adding to a SynchronousCommandQueue
	 */
	public RunMotorsToPosition driveDistanceMM (double mm, double speed, double timeout) {
		int counts = (int)(COUNTS_PER_MM * mm);
		return new RunMotorsToPosition(
				new MotorWrapper[]{leftDrive,rightDrive}, counts, speed, timeout);
	}

	/**
	 * Note this does not call telemetry.update();
	 * @param opMode opMode, used to get the telemetry value
	 */
	public void logTelemetry (LinearOpMode opMode) {
		Telemetry telemetry = opMode.telemetry;
		telemetry.addData("Left Drive:", "");
		telemetry.addData("power", leftDrive.getPower());
		telemetry.addData("currPos", leftDrive.getCurrentPosition());
		telemetry.addData("targetPos", leftDrive.getTargetPosition());
		telemetry.addData("encoderMovemenActive", leftDrive.autoControlActive);

		telemetry.addData("Right Drive:", "right");
		telemetry.addData("power", rightDrive.getPower());
		telemetry.addData("currPos", rightDrive.getCurrentPosition());
		telemetry.addData("targetPos", rightDrive.getTargetPosition());
		telemetry.addData("encoderMovemenActive", rightDrive.autoControlActive);
	}


	public void setAutocontrolActive (boolean active) {
		leftDrive.autoControlActive = active;
		rightDrive.autoControlActive = active;
	}

}
