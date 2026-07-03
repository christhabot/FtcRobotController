package org.firstinspires.ftc.teamcode.tankDrive.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.tankDrive.robot.driveTrain;
import org.firstinspires.ftc.teamcode.tankDrive.robot.intakeOuttake;

@TeleOp(name = "Tank Drive Full with intake-outtake", group = "TeleOp")
public class driveWithIntakeOuttake extends LinearOpMode {

    @Override
    public void runOpMode() {
        driveTrain drivetrain = new driveTrain(hardwareMap, telemetry);
        intakeOuttake intakeouttake = new intakeOuttake(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            drivetrain.loop(gamepad1, getRuntime());
            intakeouttake.loop(gamepad1, gamepad2, getRuntime());
            telemetry.update();
        }
    }
}