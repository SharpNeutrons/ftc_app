package org.firstinspires.ftc.teamcode.RobotCommands;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.HardwareClasses.DriveTrain;

/**
 * This class takes a motor array and moves them all by a given amount of counts
 * useful for drivetrains when more than one motor has to move at the same time to the same position
 */
public class RotateRobotByDegrees implements RobotCommand {

	private DriveTrain driveTrain;
	private BNO055IMU imu;
	private double degrees;
	private double speed;
	private boolean shouldTimeout;
	private double timeoutMillis;
	private ElapsedTime elapsedTime = new ElapsedTime();
	private double minError;
	private final double SLOWDOWN_RANGE = 20;//if the robot is within X degrees, slow down
	private final double SLOWDOWN_SPEED = 0.1;//to this speed
	private double startDegrees;

	public RotateRobotByDegrees(DriveTrain driveTrain, BNO055IMU imu, double degrees, double speed,
								double minError) {
		this.driveTrain = driveTrain;
		this.imu = imu;
		this.degrees = degrees;
		this.speed = speed;
		this.minError = Math.abs(minError);
		this.shouldTimeout = false;
	}

	public RotateRobotByDegrees(DriveTrain driveTrain, BNO055IMU imu, double degrees, double speed,
								double minError, double timeoutMillis) {
		this.driveTrain = driveTrain;
		this.imu = imu;
		this.degrees = degrees;
		this.speed = speed;
		this.minError = Math.abs(minError);
		this.shouldTimeout = true;
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public void runCommand () {
		driveTrain.setAutocontrolActive(true);
		driveTrain.controlOverride(0, this.speed * -Math.signum(degrees));
		startDegrees = imu.getAngularOrientation(
				AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
		driveTrain.setAutocontrolActive(true);
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
			double difference = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
					AngleUnit.DEGREES).firstAngle - startDegrees;
			//if the difference is less than the minError, it will stop
			if (Math.abs(difference) > minError) {
				//if the heading is within the target range, slow it down
				if (Math.abs(difference) < SLOWDOWN_RANGE) {
					driveTrain.controlOverride(0, SLOWDOWN_SPEED * -Math.signum(difference));
				}
				//if the bot is turning so that the difference increases, reverse the direction
				if (Math.signum(difference * driveTrain.getTurnSpeed()) == 1) {
					driveTrain.controlOverride(0, driveTrain.getTurnSpeed() * -1);
				}
				return elapsedTime.milliseconds() < timeoutMillis || !shouldTimeout;
			}
		}
		return false;
	}

	@Override
	public void endCommand () {
		driveTrain.controlOverride(0, 0);
		driveTrain.setAutocontrolActive(false);
	}

}
