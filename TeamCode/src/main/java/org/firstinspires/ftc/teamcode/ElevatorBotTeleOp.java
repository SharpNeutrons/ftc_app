package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="5987 ElevatorBotTeleOp", group="final")
public class ElevatorBotTeleOp extends RobotControlSystemOpMode {

	@Override
	public void runOpMode () {
		super.runOpMode();

		while (opModeIsActive() && !isStopRequested()) {
			opModeLoop();
		}
	}

	@Override
	void opModeLoop () {
		super.opModeLoop();

		controlDrive();
	}

	private void controlDrive () {
		robot.driveTrain.controlOverride(-gamepad1.left_stick_y,gamepad1.left_stick_x);
	}

}
