package org.firstinspires.ftc.teamcode.robot.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.subsystems.IntakeOuttake;
import org.firstinspires.ftc.teamcode.robot.subsystems.drivetrain.TankDriveTrain;

@TeleOp(name = "Tank Drive Full with intake-outtake", group = "TeleOp")
public class DriveWithIntakeOuttake extends LinearOpMode {

    @Override
    public void runOpMode() {
        TankDriveTrain tankDriveTrain = new TankDriveTrain(hardwareMap, telemetry);
        IntakeOuttake intakeOuttake = new IntakeOuttake(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            tankDriveTrain.loop(gamepad1, getRuntime());
            intakeOuttake.loop(gamepad1, gamepad2, getRuntime());
            telemetry.update();
        }
    }
}