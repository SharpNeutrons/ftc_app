package org.firstinspires.ftc.teamcode.HardwareClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CommandQueue;
import org.firstinspires.ftc.teamcode.HardwareClasses.DriveTrain;
import org.firstinspires.ftc.teamcode.HardwareClasses.MotorWrapper;
import org.firstinspires.ftc.teamcode.HardwareClasses.ServoWrapper;
import org.firstinspires.ftc.teamcode.RobotCommands.AddCommandToSeparateQueue;
import org.firstinspires.ftc.teamcode.RobotCommands.MoveServoToPosition;
import org.firstinspires.ftc.teamcode.RobotCommands.RobotCommand;
import org.firstinspires.ftc.teamcode.RobotCommands.RunMotorToPosition;
import org.firstinspires.ftc.teamcode.RobotCommands.WaitMillis;
import org.firstinspires.ftc.teamcode.SynchronousCommandGroup;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hangervator {

	private MotorWrapper motor;
	private ServoWrapper latch;
	public static final HashMap<String, Integer> HANG_MOTOR_POSITIONS =
			new HashMap<String, Integer>() {{
		put("min", 0);
		put("max", 999999999);
		put("minLatch", 999999999);
		put("minUnlatch", 999999999);
		put("maxNotLatched", 999999);
	}};

	private final double LATCH_LOCK = 0.19;
	private final double LATCH_UNLOCK = 0.088;

	public Hangervator (LinearOpMode opMode) {
		HardwareMap hardwareMap = opMode.hardwareMap;
		motor = new MotorWrapper(hardwareMap.get(DcMotor.class, "hangMotor"));
		motor.init();
		motor.addEncoderValuesToMap(HANG_MOTOR_POSITIONS);

		latch = new ServoWrapper(hardwareMap.get(Servo.class, "hangLatch"), LATCH_LOCK);
		latch.addPositionsToMap(new HashMap<String, Double>() {{
			put("lock", LATCH_LOCK);
			put("unlock", LATCH_UNLOCK);
		}});
	}

	void controlHangervator (double power) {
		if (motor.setPower(power) && power != 0) {
			latch.setPosition("unlock");
		} else if (motor.getPower() == 0) {
			latch.setPosition("lock");
		}
	}

	public SynchronousCommandGroup deploy (final CommandQueue commandQueue, final DriveTrain driveTrain) {
		SynchronousCommandGroup commands = new SynchronousCommandGroup();
		ConcurrentLinkedQueue<RobotCommand> queue = new ConcurrentLinkedQueue<RobotCommand>() {{
			add(new RunMotorToPosition(motor, HANG_MOTOR_POSITIONS.get("min"), 1, 200));
			add(new MoveServoToPosition(latch, latch.getPosFromMap("unlatch")));
			add(new WaitMillis(100));
			add(new RunMotorToPosition(motor, HANG_MOTOR_POSITIONS.get("minUnlatch"), 1));
			add(driveTrain.driveDistanceMM(50, 1));

			//the optional "finishing" commands that can be run at the same time as other things
			//they retract the hangervator and lock the servo
			add(new AddCommandToSeparateQueue(commandQueue,
					new RunMotorToPosition(motor, HANG_MOTOR_POSITIONS.get("min"), 1),
					new MoveServoToPosition(latch, latch.getPosFromMap("latch"))));
		}};
		commands.addAllToQueue(queue);
		return commands;
	}

}
