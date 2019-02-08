package org.firstinspires.ftc.teamcode.HardwareClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.CommandQueue;
import org.firstinspires.ftc.teamcode.RobotCommands.AddCommandToSeparateQueue;
import org.firstinspires.ftc.teamcode.RobotCommands.RunMotorToPosition;
import org.firstinspires.ftc.teamcode.SynchronousCommandGroup;

import java.util.HashMap;
import java.util.Map;

public class Cratervator {

	private MotorWrapper extendMotor;
	private MotorWrapper yawMotor;
	private MotorWrapper intakeMotor;
	private final int MAX_EXTENSION = 99999999;
	private HashMap<String, Integer> EXTEND_MOTOR_POSITIONS =
			new HashMap<String, Integer>() {{
		put("min", 0);
		put("max", MAX_EXTENSION);
		put("rev", RobotHardware.HD_20_COUNTS_PER_REV);
	}};
	private boolean autonomous = false;
	private final HashMap<String, Integer> YAW_MOTOR_POSITIONS =
			new HashMap<String, Integer>() {{
				put("min", -9999);
				put("max", 99999);
				put("rev", RobotHardware.HD_40_COUNTS_PER_REV);
			}};
	private final double WINCH_RADIUS = 50;//the radius of the small side of the extension winch, in mm
	private final double YAW_GEAR_RATIO = 4;//revs of motor:revs of cratervator

	/**
	 *
	 * @param opMode used to get a hardwaremap
	 * @param autonomous is the opmode given autonomous or teleop,
	 *                      used to decide what motor positions to use
	 */
	public Cratervator(LinearOpMode opMode, boolean autonomous) {
		this.autonomous = autonomous;
		HardwareMap hardwareMap = opMode.hardwareMap;
		extendMotor = new MotorWrapper(hardwareMap.get(DcMotor.class, "craterExtendMotor"));
		extendMotor.init();
		if (!autonomous) {
			invertExtendPositions();
		}
		extendMotor.addEncoderValuesToMap(EXTEND_MOTOR_POSITIONS);

		yawMotor = new MotorWrapper(hardwareMap.get(DcMotor.class, "craterYawMotor"));
		yawMotor.init();
		yawMotor.addEncoderValuesToMap(YAW_MOTOR_POSITIONS);

		intakeMotor = new MotorWrapper(hardwareMap.get(DcMotor.class, "intakeMotor"));
		intakeMotor.init();
	}

	private void invertExtendPositions () {
		int modificationDirection = autonomous ? 1:-1;
		for(Map.Entry<String, Integer> pos:EXTEND_MOTOR_POSITIONS.entrySet()) {
			pos.setValue(pos.getValue() + modificationDirection * MAX_EXTENSION);
		}
	}

	private void invertExtendPositions (HashMap<String, Integer> map) {
		int modificationDirection = autonomous ? 1:-1;
		for(Map.Entry<String, Integer> pos:EXTEND_MOTOR_POSITIONS.entrySet()) {
			map.put(pos.getKey(), pos.getValue() + modificationDirection * MAX_EXTENSION);
		}
	}

	void manualControl (double extension, double yaw, double intake) {
		extendMotor.setPower(extension);
		yawMotor.setPower(yaw);
		intakeMotor.setPower(intake);
	}

	void strafeControl (double x, double y, double intake) {
		intakeMotor.setPower(intake);



	}

	/**
	 *
	 * @return the change in length of the cratervator from the start of the opmode
	 *
	 */
	double getLength () {
		int truePos = extendMotor.getCurrentPosition();
		if (!autonomous) {
			truePos += MAX_EXTENSION;
		}
		return truePos / extendMotor.getEncoderValFromMap("rev") * WINCH_RADIUS;
	}

	double getYaw () {
		double motorRevs = yawMotor.getCurrentPosition()/yawMotor.getEncoderValFromMap("rev");
		double craterRevs = motorRevs / YAW_GEAR_RATIO;
//		return craterRevs * 360;//degrees
		return craterRevs * 2 * Math.PI;//radians
	}


	/**Functions to move the cratervator with encoders
	 */

	RunMotorToPosition setExtendPos (String key, double speed) {
		return extendMotor.runMotorToPosition(key, speed);
	}

	RunMotorToPosition setExtendPos (String key, double speed, double timeoutMillis) {
		return extendMotor.runMotorToPosition(key, speed, timeoutMillis);
	}

	RunMotorToPosition setYawPos (String key, double speed) {
		return yawMotor.runMotorToPosition(key, speed);
	}

	RunMotorToPosition setYawPos (String key, double speed, double timeoutMillis) {
		return yawMotor.runMotorToPosition(key, speed, timeoutMillis);
	}

	SynchronousCommandGroup setExtendYawPos (CommandQueue queue, String extKey, double extSpeed,
											 String yawKey, double yawSpeed) {
		SynchronousCommandGroup group = new SynchronousCommandGroup();
		group.addNewCommand(new AddCommandToSeparateQueue(queue,
				setYawPos(yawKey, yawSpeed)));
		group.addNewCommand(setExtendPos(extKey, extSpeed));

		return group;
	}

	SynchronousCommandGroup setExtendYawPos (CommandQueue queue, String extKey, double extSpeed,
											 String yawKey, double yawSpeed, double timeoutMillis) {
		SynchronousCommandGroup group = new SynchronousCommandGroup();
		group.addNewCommand(new AddCommandToSeparateQueue(queue,
				setYawPos(yawKey, yawSpeed, timeoutMillis)));
		group.addNewCommand(setExtendPos(extKey, extSpeed, timeoutMillis));

		return group;
	}

	RunMotorToPosition setExtendPos (int pos, double speed) {
		return extendMotor.runMotorToPosition(pos, speed);
	}

	RunMotorToPosition setExtendPos (int pos, double speed, double timeoutMillis) {
		return extendMotor.runMotorToPosition(pos, speed, timeoutMillis);
	}

	RunMotorToPosition setYawPos (int pos, double speed) {
		return yawMotor.runMotorToPosition(pos, speed);
	}

	RunMotorToPosition setYawPos (int pos, double speed, double timeoutMillis) {
		return yawMotor.runMotorToPosition(pos, speed, timeoutMillis);
	}

	SynchronousCommandGroup setExtendYawPos (CommandQueue queue, int extPos, double extSpeed,
											 int yawPos, double yawSpeed) {
		SynchronousCommandGroup group = new SynchronousCommandGroup();
		group.addNewCommand(new AddCommandToSeparateQueue(queue,
				setYawPos(yawPos, yawSpeed)));
		group.addNewCommand(setExtendPos(extPos, extSpeed));

		return group;
	}

	SynchronousCommandGroup setExtendYawPos (CommandQueue queue, int extPos, double extSpeed,
											 int yawPos, double yawSpeed, double timeoutMillis) {
		SynchronousCommandGroup group = new SynchronousCommandGroup();
		group.addNewCommand(new AddCommandToSeparateQueue(queue,
				setYawPos(yawPos, yawSpeed, timeoutMillis)));
		group.addNewCommand(setExtendPos(extPos, extSpeed, timeoutMillis));

		return group;
	}

}
