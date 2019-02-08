package org.firstinspires.ftc.teamcode.HardwareClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CommandQueue;
import org.firstinspires.ftc.teamcode.RobotCommands.AddCommandToSeparateQueue;
import org.firstinspires.ftc.teamcode.RobotCommands.MoveServoToPosition;
import org.firstinspires.ftc.teamcode.RobotCommands.RobotCommand;
import org.firstinspires.ftc.teamcode.RobotCommands.RunMotorToPosition;
import org.firstinspires.ftc.teamcode.RobotCommands.WaitMillis;
import org.firstinspires.ftc.teamcode.SynchronousCommandGroup;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Landervator {

	private MotorWrapper extendMotor;
	private MotorWrapper skiLiftMotor;
	private static final HashMap<String, Integer> EXTEND_MOTOR_POSITIONS =
			new HashMap<String, Integer>() {{
		put("min", 0);
		put("max", 999999999);
	}};
	private static final HashMap<String, Integer> SKI_LIFT_MOTOR_POSITIONS =
			new HashMap<String, Integer>() {{
				put("min", 0);
				put("max", 999999999);
				put("score", 99999);
				put("load", 999);
			}};

	public Landervator(LinearOpMode opMode) {
		HardwareMap hardwareMap = opMode.hardwareMap;
		extendMotor = new MotorWrapper(hardwareMap.get(DcMotor.class, "landerExtendMotor"));
		extendMotor.init();
		extendMotor.addEncoderValuesToMap(EXTEND_MOTOR_POSITIONS);

		skiLiftMotor = new MotorWrapper(hardwareMap.get(DcMotor.class, "skiLiftMotor"));
		skiLiftMotor.init();
		skiLiftMotor.addEncoderValuesToMap(SKI_LIFT_MOTOR_POSITIONS);
	}

	void controlExtension (double power) {
		extendMotor.setPower(power);
		//TODO figure out skiLift tensioning as this happens
	}

	void controlSkiLift (double power) {skiLiftMotor.setPower(power);}

	//TODO figure out skiLift tensioning as this happens
	RunMotorToPosition setLandervatorPos (String key, double speed) {
		return extendMotor.runMotorToPosition(key, speed);
	}

	RunMotorToPosition setLandervatorPos (String key, double speed, double timeoutMillis) {
		return extendMotor.runMotorToPosition(key, speed, timeoutMillis);
	}

	RunMotorToPosition setSkiLiftPos (String key, double speed) {
		return skiLiftMotor.runMotorToPosition(key, speed);
	}

	RunMotorToPosition setSkiLiftPos (String key, double speed, double timeoutMillis) {
		return skiLiftMotor.runMotorToPosition(key, speed, timeoutMillis);
	}

}
