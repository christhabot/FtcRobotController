package org.firstinspires.ftc.teamcode.tankDrive.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.tankDrive.robot.driveTrain;

@TeleOp(name = "Tank Drive Full", group = "TeleOp")
public class drive extends LinearOpMode {
    private driveTrain drivetrain;

    @Override
    public void runOpMode() {
        drivetrain = new driveTrain(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            drivetrain.loop(gamepad1, getRuntime());
            telemetry.update();
        }
    }
}